package lotto.proxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import io.vertx.core.Future;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.TimeoutHandler;
import lotto.common.LottoException;
import lotto.common.ServiceParamsInterface;
import lotto.logger.Logger;
import lotto.logger.LoggerInterface;
import lotto.proxy.impl.ConfigManager;

public class ProxyVerticle extends AbstractVerticle {
	private static long serie = (long) (Math.random() * 16777216);

	private Router router;
	private BodyHandler bodyHandler;
	private CorsHandler corsHandler;
	private TimeoutHandler timeoutHandler;

	private ConfigManagerInterface configManager;
	private ConfigParamsInterface configParams;
	private FirewallInterface firewall;
	private DispatcherInterface dispatcher;

	private LoggerInterface loggerFactory;

	public static String padLeftZeros(String str, int n) {
		return String.format("%1$" + n + "s", str).replace(' ', '0');
	}

	public static String getSerie(RoutingContext routingContext) {
		return Long.toHexString(routingContext.hashCode());
	}

	private void startup() {
		this.router = Router.router(vertx);
		this.firewall = this.configParams.getFirewall();
		this.dispatcher = this.configParams.getDispatcher();
		this.loggerFactory = this.configParams.getLoggerFactory();

		// El body handler es necesario para utilizar el método getBody del routing context
		this.bodyHandler = BodyHandler.create();
		this.router.route().handler(this.bodyHandler);

		// Devuelve un 503: Service unavailable
		this.timeoutHandler = TimeoutHandler.create(this.configParams.getTimeout());
		this.router.route().handler(this.timeoutHandler);

		this.corsHandler = CorsHandler.create("*");
		this.corsHandler.allowedMethod(HttpMethod.GET);
		this.corsHandler.allowedMethod(HttpMethod.POST);
		this.corsHandler.allowedMethod(HttpMethod.PUT);
		this.corsHandler.allowedMethod(HttpMethod.DELETE);
		this.corsHandler.allowedHeader("Content-Type");
		this.corsHandler.allowedHeader("Authorization");
		this.corsHandler.allowedHeader("X-User");
		this.corsHandler.allowedHeader("X-Service-Id");
		this.router.route().handler(this.corsHandler);
	}
	
	public void start(Future<Void> startFuture) throws Exception {
		this.loggerFactory = new Logger(new HashMap<String, String>());
		LoggerInterface globalLogger = this.loggerFactory.createLocalLogger("ProxyVerticle", "start");
		
		try {
			this.configManager = new ConfigManager(this.vertx, this.loggerFactory);

			// Argumentos pasados por línea de comandos
			List<String> args = this.processArgs();
			
			globalLogger.log(Level.INFO, "Starting with arguments: " + args.toString() + " ... ");
			
			// Instancia del objeto que contiene todos los parámetros de configuración
			this.configManager.getConfigParams(args).setHandler(arcp -> {
				try {
					if(arcp.failed()) {
						globalLogger.log(Level.INFO, "Error getting config params: " + arcp.cause().toString());
						startFuture.fail(arcp.cause());
					}
					else {
						// Parámetros de configuración
						this.configParams = arcp.result();
						
						globalLogger.log(Level.INFO, "Config params loaded!");

						// Aplicar configuración inicial
						this.startup();
						
						globalLogger.log(Level.INFO, "Router setted!");

						// Manejador de peticiones
						router.route().handler(originalRequest -> {
							String serial = padLeftZeros(Long.toHexString(++serie % 16777216), 6);
							LoggerInterface requestFactory = this.loggerFactory.createLocalLogger(serial);
							LoggerInterface requestLogger = requestFactory.createLocalLogger("ProxyVerticle", "start:handler");
							
							try {
								originalRequest.request().exceptionHandler(ex -> {
									requestLogger.log(Level.INFO, ex.toString());
									this.returnFail(ex, originalRequest, requestFactory);
								});

								requestLogger.log(Level.INFO, "Handling request: [" + originalRequest.request().method() + "] " + originalRequest.request().uri());

								for(Entry<String, String> entry : originalRequest.request().headers()) {
									requestLogger.log(Level.INFO, " -> " + entry.getKey() + ":\t" + entry.getValue());
								}
								
								// Aceptación de la petición
								this.firewall.check(originalRequest, requestFactory).setHandler(acceptedRequestResult -> {
									try {
										if(acceptedRequestResult.failed()) {
											this.returnFail(acceptedRequestResult.cause(), originalRequest, requestFactory);
										}
										else {
											JsonObject acceptedRequestResultJson = acceptedRequestResult.result();
											
											requestLogger.log(Level.INFO, "Request  granted by firewall! " + acceptedRequestResultJson.toString());
											
											String jwt = acceptedRequestResultJson.getString("jwt");
											String action = acceptedRequestResultJson.getString("action");
											
											switch(action) {
												case "return":
													requestLogger.log(Level.INFO, action);
													this.returnResponse(originalRequest, 200, acceptedRequestResultJson.toString());
													break;
												case "info":
													requestLogger.log(Level.INFO, action);
													this.configParams.getServiceCatalog().getServiceParams(acceptedRequestResultJson.getString("service-id"), requestFactory).setHandler(serviceParamsResult -> {
														try {
															JsonArray jsonArray = new JsonArray();
															
															for(ServiceParamsInterface serviceParams : serviceParamsResult.result()) {
																jsonArray.add(serviceParams.toJsonObject());
															}
															
															this.returnResponse(originalRequest, 200, jsonArray.toString());
														}
														catch(Throwable ex) {
															this.returnFail(ex, originalRequest, requestFactory);
														}
													});
													break;
												case "dispatch":
													// El firewall devuelve un objeto con información referente a la petición aceptada
													// Ejecución de la llamada
													this.dispatcher.dispatch(originalRequest, jwt, requestFactory).setHandler(dispatchResult -> {
														try {
															if(dispatchResult.failed()) {
																this.returnFail(dispatchResult.cause(), originalRequest, requestFactory);
															}
															else {
																// En este punto la llamada se ha respondido correctamente y no hacen falta más acciones
																requestLogger.log(Level.INFO, "Request dispatched! [" + originalRequest.request().method() + "] " + originalRequest.request().uri());
															}
														}
														catch(Throwable ex) {
															this.returnFail(ex, originalRequest, requestFactory);
														}
													});
													break;
												default:
													requestLogger.log(Level.WARNING, "Action unknown: " + action);
													this.returnFail(new LottoException(500, "UNKNOWN_FIREWALL_ACTION", "Action unknown: " + action), originalRequest, requestFactory);
											}
										}
									}
									catch(Throwable ex) {
										this.returnFail(ex, originalRequest, requestFactory);
									}
								});
							}
							catch(Throwable ex) {
								this.returnFail(ex, originalRequest, requestFactory);
							}
						});
						globalLogger.log(Level.INFO, "Request handler setted!");
						this.vertx.createHttpServer().requestHandler(router::accept).listen(this.configParams.getPort());
						globalLogger.log(Level.INFO, "Proxy listening in port " + this.configParams.getPort() + "!");
						super.start(startFuture);
					}
				}
				catch(Throwable ex) {
					globalLogger.log(Level.SEVERE, "Error starting (2): " + ex.toString());
					startFuture.fail(ex);
				}
			});
		}
		catch(Throwable ex) {
			globalLogger.log(Level.SEVERE, "Error starting (1): " + ex.toString());
			startFuture.fail(ex);
		}
	}

	private void returnFail(Throwable cause, RoutingContext routingContext, LoggerInterface requestFactory){
		LoggerInterface logger = requestFactory.createLocalLogger("ProxyVerticle", "returnFail");
		
		if(cause instanceof LottoException) {
			LottoException exception = (LottoException) cause;
			JsonObject ret = new JsonObject()
					.put("status", "ko")
					.put("error", new JsonObject()
						.put("code", exception.getCode())
						.put("message", exception.getMessage())
			);

			if(exception.getBody() != null) {
				ret.put("body", new JsonObject(exception.getBody()));
			}

			logger.log(Level.WARNING, "[" + exception.getStatus() + "] " + cause.toString());
			this.returnResponse(routingContext, exception.getStatus(), ret.toString());
		}
		else {
			JsonObject ret = new JsonObject()
					.put("status", "ko")
					.put("error", new JsonObject()
						.put("message", cause.getMessage())
			);
			
			logger.log(Level.SEVERE, "[500] " + cause.toString());
			this.returnResponse(routingContext, 500, ret.toString());
		}
		
		//cause.printStackTrace(System.out);
	}

	private void returnResponse(RoutingContext routingContext, Integer code, String message){
		routingContext.response()
		.setStatusCode(code)
		.putHeader("content-type", "application/json; charset=utf-8")
		.end(message);
	}
}
