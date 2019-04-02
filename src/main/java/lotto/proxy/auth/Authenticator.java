package lotto.proxy.auth;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import lotto.common.LottoException;
import lotto.common.ServiceCatalogInterface;
import lotto.common.ServiceParamsInterface;
import lotto.logger.LoggerInterface;
import lotto.model.AuthenticatedUser;
import lotto.model.AuthenticatedUserInterface;
import lotto.proxy.AuthenticatorInterface;

public class Authenticator implements AuthenticatorInterface {

	private Vertx vertx;
	private JWTDecoderInterface jwtDecoder;
	private ServiceCatalogInterface catalogService;
	private String authServiceId;
	
	
	public Authenticator(
			Vertx vertx, 
			ServiceCatalogInterface catalogService,
			JWTDecoderInterface jwtDecoder,
			String authServiceId
	){
		this.vertx = vertx;
		this.catalogService = catalogService;
		this.authServiceId = authServiceId;
		this.jwtDecoder = jwtDecoder;
	}

	protected Future<String> requestToken(String tokenId, LoggerInterface requestFactory) {
		Future<String> fut = Future.future();
		LoggerInterface logger = requestFactory.createLocalLogger("Authenticator", "requestToken");
		
		try {
			logger.log(Level.INFO, "Getting auth service info: " + this.authServiceId);
			this.catalogService.getNextServiceParams(this.authServiceId, requestFactory).setHandler(ServiceParamsResult -> {
				try {
					if(ServiceParamsResult.failed()) {
						fut.fail(ServiceParamsResult.cause());
					}
					else {
						ServiceParamsInterface serviceParams = ServiceParamsResult.result();
						
						logger.log(Level.INFO, "Getting JWT with ID = " + tokenId + "...");
						
						HttpClient httpClient = this.vertx.createHttpClient(new HttpClientOptions());
						
						HttpClientRequest c_req = httpClient.request(
								HttpMethod.GET,
								serviceParams.getServicePort(),
								serviceParams.getServiceAddress(),
								"/token/" + tokenId,
								c_res -> 
						{
							try {
								logger.log(Level.INFO, "=> " + c_res.statusCode());
								
								c_res.exceptionHandler(ex -> {
									fut.fail(ex);
								});

								c_res.bodyHandler(body -> {
									httpClient.close();
									if(c_res.statusCode() < 400) {
										String jwt = body.toJsonObject().getString("token");
										logger.log(Level.INFO, "JWT: " + jwt);
										fut.complete(jwt);
									}
									else {
										fut.fail(new LottoException(c_res.statusCode(), "AUTH_ERROR", "Auth error", body.toString()));
									}
								});

								logger.log(Level.INFO, "Handlers set!");
							}
							catch(Throwable ex) {
								fut.fail(ex);
							}
						});

						c_req.putHeader("content-type", "application/json");
						//c_req.setChunked(true);
						c_req.end();
						logger.log(Level.INFO, "Auth request sent!");
					}
				}
				catch(Throwable ex) {
					fut.fail(ex);
				}
			});
		}
		catch(Throwable ex) {
			fut.fail(ex);
		}
		return fut;
	}
	
	@Override
	public Future<String> getToken(String tokenId, LoggerInterface requestFactory) {
		return this.requestToken(tokenId, requestFactory);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Future<AuthenticatedUserInterface> decodeToken(String jwt, LoggerInterface requestFactory) {
		Future<AuthenticatedUserInterface> fut = Future.future();
		LoggerInterface logger = requestFactory.createLocalLogger("Authenticator", "decodeToken");
		try {
			logger.log(Level.INFO, "Decoding JWT: " + jwt);
			Map<String, Object> decodedJWT = this.jwtDecoder.decode(jwt);
			
			AuthenticatedUser user = new AuthenticatedUser(
					decodedJWT.get("id").toString(), 
					decodedJWT.get("email").toString(), 
					(List<String>)  decodedJWT.get("roles"),
					(List<Integer>) decodedJWT.get("permissions"),
					jwt
			);
			
			logger.log(Level.INFO, "Decoded JWT: " + user.toString());
			
			fut.complete(user);
		}
		catch(Throwable ex) {
			fut.fail(ex);
		}
		return fut;
	}

	@Override
	public Future<AuthenticatedUserInterface> getDecodedToken(String tokenId, LoggerInterface requestFactory) {
		Future<AuthenticatedUserInterface> fut = Future.future();
		LoggerInterface logger = requestFactory.createLocalLogger("Authenticator", "getDecodedToken");
		try {
			logger.log(Level.INFO, "Getting JWT with ID: " + tokenId);
			this.getToken(tokenId, requestFactory).setHandler(jwtResult -> {
				try {
					if(jwtResult.failed()) {
						fut.fail(jwtResult.cause());
					}
					else {
						this.decodeToken(jwtResult.result(), requestFactory).setHandler(userResult -> {
							try {
								if(userResult.failed()) {
									fut.fail(userResult.cause());
								}
								else {
									fut.complete(userResult.result());
								}
							}
							catch(Throwable ex) {
								fut.fail(ex);
							}
						});
					}
				}
				catch(Throwable ex) {
					fut.fail(ex);
				}
			});
		}
		catch(Throwable ex) {
			fut.fail(ex);
		}
		return fut;
	}
}
