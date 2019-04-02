package lotto.proxy.impl;

// https://github.com/vert-x3/vertx-examples/tree/master/web-examples/src/main/java/io/vertx/example/web

import java.util.Map.Entry;
import java.util.logging.Level;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import lotto.common.LottoException;
import lotto.common.ServiceCatalogInterface;
import lotto.common.ServiceParamsInterface;
import lotto.logger.LoggerInterface;
import lotto.proxy.DispatcherInterface;

public class Dispatcher implements DispatcherInterface {
	private Vertx vertx;
	private ServiceCatalogInterface serviceCatalog;
	private String mode = null;
	private Boolean chunk = false;

	/**
	 *
	 * @param vertx
	 * @param serviceCatalog
	 * @param serviceList
	 * @param colorList false: black, true: white
	 */
	public Dispatcher(Vertx vertx, ServiceCatalogInterface serviceCatalog, String mode, Boolean chunk) {
		this.vertx = vertx;
		this.serviceCatalog = serviceCatalog;
		this.mode = mode;
		this.chunk = chunk;
	}

	protected Future<String> getServiceId(RoutingContext originalRequest, String jwt, LoggerInterface parentLogger) {
		Future<String> fut = Future.future();
		fut.complete(originalRequest.request().getHeader("X-Service-Id"));
		return fut;
	}

	protected String getDispatchMode(RoutingContext originalRequest) {
		return this.mode;
	}

	protected boolean chunkRequest(RoutingContext originalRequest, ServiceParamsInterface serviceParams) {
		Boolean ret = null;

		String prox_chunk = originalRequest.request().getHeader("X-Proxy-Chunk");

		if(prox_chunk != null) {
			if(prox_chunk.equalsIgnoreCase("false")) {
				ret = false;
			}
			else if(prox_chunk.equalsIgnoreCase("true")) {
				ret = true;
			}
		}

		if(ret == null) {
			ret = serviceParams.isChunked();
		}

		if(ret == null) {
			ret = this.chunk;
		}

		if(ret == null) {
			ret = false;
		}

		return ret;
	}

	protected Future<Void> dispatchServiceLessRequest(RoutingContext originalRequest, String jwt, LoggerInterface parentLogger) {
		Future<Void> fut = Future.future();
		LoggerInterface logger = parentLogger.createLocalLogger("Dispatcher", "dispatchServiceLessRequest");
		logger.log(Level.WARNING, "Default: SERVICE_ID_NOT_RESOLVED");
		fut.fail(new LottoException(400, "SERVICE_ID_NOT_RESOLVED", "Service ID not resolved"));
		return fut;
	}

	@Override
	public Future<Void> dispatch(RoutingContext originalRequest, String jwt, LoggerInterface requestFactory) {
		Future<Void> fut = Future.future();
		LoggerInterface logger = requestFactory.createLocalLogger("Dispatcher", "dispatch");

		try {
			logger.log(Level.INFO, "Getting Service ID...");
			this.getServiceId(originalRequest, jwt, requestFactory).setHandler(serviceIdResult -> {
				try {
					String serviceId = serviceIdResult.result();
					if(serviceIdResult.failed()) {
						fut.fail(serviceIdResult.cause());
					}
					else if(serviceId == null){
						this.dispatchServiceLessRequest(originalRequest, jwt, requestFactory).setHandler(fut.completer());
					}
					else {
						logger.log(Level.INFO, "Getting Service with ID '" + serviceId + "'");
						this.serviceCatalog.getNextServiceParams(serviceId, requestFactory).setHandler(requestedServiceResult -> {
							try {
								ServiceParamsInterface serviceParams = requestedServiceResult.result();

								if(requestedServiceResult.failed()) {
									logger.log(Level.INFO, "(7): " + requestedServiceResult.cause().getMessage());
									fut.fail(requestedServiceResult.cause());
								}
								else if(serviceParams == null) {
									fut.fail(new LottoException(500, "SERVICE_NOT_INSTACIATED", "Service with ID '" + serviceId + "' not instanciated"));
								}
								else if(!serviceParams.isAllowed()) {
									fut.fail(new LottoException(403, "SERVICE_ID_FORBIDDEN", "Service ID forbidden"));
								}
								else {
									HttpServerRequest req = originalRequest.request();
									HttpServerResponse res = originalRequest.response();
									HttpClient httpClient = this.vertx.createHttpClient(new HttpClientOptions());

									logger.log(Level.INFO, "Service with ID '" + serviceId + "' got! " + serviceParams.toString()
										+ ", Building request: "
										+ "[" + req.method() + "] "
										+ serviceParams.getServiceAddress()
										+ serviceParams.getPrefix()
										+ req.uri()
									);

									HttpClientRequest c_req = httpClient.request(
											req.method(),
											serviceParams.getServicePort(),
											serviceParams.getServiceAddress(),
											serviceParams.getPrefix() + req.uri(),
											c_res ->
									{
										try {
											logger.log(Level.INFO, " => " + c_res.statusCode());
											res.setStatusCode(c_res.statusCode());
											res.headers().setAll(c_res.headers());

											c_res.exceptionHandler(ex -> {
												if(fut.isComplete()) {
													logger.log(Level.SEVERE, "Exception after complete (6): " + ex.getMessage());
												}
												else {
													logger.log(Level.SEVERE, "Exception before complete (6): " + ex.getMessage());
												}

												System.out.println("============================================================================");
												ex.printStackTrace(System.out);
												System.out.println("============================================================================");
											});

											switch(this.getDispatchMode(originalRequest)) {
												case "body": // Opción 1 para cuerpos pequeños
													logger.log(Level.INFO, " => Body handler");
													c_res.bodyHandler(body -> {
														httpClient.close();

														if(fut.isComplete()) {
															logger.log(Level.INFO, "[BodyHandler] => Future already completed!");
														}
														else if(res.ended()) {
															logger.log(Level.INFO, "[BodyHandler] => Timeout fired!");
															fut.complete();
														}
														else {
															res.end(body);
															logger.log(Level.INFO, "[BodyHandler] => OK!");
															fut.complete();
														}
													});
													break;

												case "default": // Opción 2 para cuerpos grandes
												default:
													logger.log(Level.INFO, " => Default handler");
													c_res.handler(data -> {
														if(res.ended()) {
															logger.log(Level.INFO, "[DataHandler] => Timeout fired!");

															if(fut.isComplete()) {
																logger.log(Level.INFO, "[DataHandler] => Future already completed!");
															}
															else {
																logger.log(Level.INFO, "[DataHandler] => Future not completed! Completing...");
																fut.complete();
															}
														}
														else {
															//logger.log(Level.INFO, "[DataHandler] => Received data...");
															res.write(data);
														}
													});
													c_res.endHandler(v -> {
														httpClient.close();

														if(fut.isComplete()) {
															logger.log(Level.INFO, "[EndHandler] => Timeout fired! Future already completed!");
														}
														else if(res.ended()) {
															logger.log(Level.INFO, "[EndHandler] => Timeout fired! Response already ended!");
															fut.complete(v);
														}
														else {
															res.end();
															logger.log(Level.INFO, "[EndHandler] => OK!");
															fut.complete(v);
														}
													});
											}
											String transferEncoding = c_res.getHeader("Transfer-Encoding");
											res.setChunked(transferEncoding != null && transferEncoding.equalsIgnoreCase("chunked"));
											logger.log(Level.INFO, "Response set: Status code = " + res.getStatusCode() + ", chunked = " + res.isChunked() + ", transfer encoding = " + transferEncoding + ", headers: ");
											for(Entry<String, String> header : res.headers()) {
												logger.log(Level.INFO, header.getKey() + ": " + header.getValue());
											}
										}
										catch(Throwable ex) {
											logger.log(Level.INFO, "(5): " + ex.getMessage());
											fut.fail(ex);
										}
									});
									c_req.headers().setAll(req.headers());
									c_req.headers().set("X-Boundary-Ip", req.localAddress().host() + ':' + req.localAddress().port());
									c_req.headers().set("X-Remote-Ip", req.remoteAddress().host() + ':' + req.remoteAddress().port());


									if(jwt != null) {
										c_req.headers().set("X-User", jwt);
									}

									c_req.setChunked(this.chunkRequest(originalRequest, serviceParams));

									c_req.exceptionHandler(ex -> {
										// Aquí es donde se captura el timeout de 2 minutos cuando se sube un fichero
										// https://stackoverflow.com/questions/51973723/illegalstateexception-with-vertx-only-when-attaching-a-file
										logger.log(Level.INFO, "(4): " + ex.getMessage() + " // Check the composed URI (prefix + original URI) is correct.");
										fut.fail(ex);
									});
									
									logger.log(Level.INFO, "Request built!");

									// Para que esto funcione es necesario haberle añadido un body handler al router
									c_req.end(originalRequest.getBody()); // c_req.write(originalRequest.getBody()).end();

									// La otra alternativa falla cuando se ha hecho antes otra petición (a auth para obtener un token JWT)
									/*
									req.handler(data1 -> {
										c_req.write(data1);
										logger.log(Level.INFO, "Data received: " + data);
									});

									req.bodyHandler(data1 -> {
										c_req.end(data1);
										logger.log(Level.INFO, "Data received: " + data1);
									});

									req.endHandler(v -> {
										c_req.end();
										logger.log(Level.INFO, "Request sent!");
									});
									*/
									
									logger.log(Level.INFO, "Request sent! Headers: ");

									for(Entry<String, String> entry : c_req.headers().entries()) {
										logger.log(Level.INFO, " -> " + entry.getKey() + ":\t" + entry.getValue());
									}
								}
							}
							catch(Throwable ex) {
								logger.log(Level.SEVERE, "(3): " + ex.getMessage());

								ex.printStackTrace(System.out);

								fut.fail(ex);
							}
						});
					}
				}
				catch(Throwable ex) {
					logger.log(Level.SEVERE, "(2): " + ex.getMessage());
					fut.fail(ex);
				}
			});
		}
		catch(Throwable ex) {
			logger.log(Level.SEVERE, "(1): " + ex.getMessage());
			fut.fail(ex);
		}
		return fut;
	}
}
