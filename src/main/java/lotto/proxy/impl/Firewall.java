package lotto.proxy.impl;

import java.util.logging.Level;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lotto.common.LottoException;
import lotto.logger.LoggerInterface;
import lotto.model.AuthenticatedUserInterface;
import lotto.proxy.AuthenticatorInterface;
import lotto.proxy.AuthorizatorInterface;
import lotto.proxy.FirewallInterface;

public class Firewall implements FirewallInterface {
	private AuthenticatorInterface authenticator;
	private AuthorizatorInterface authorizator;

	public Firewall(AuthenticatorInterface authenticator, AuthorizatorInterface authorizator){
		this.authenticator = authenticator;
		this.authorizator = authorizator;
	}
	
	private Future<AuthenticatedUserInterface> getUser(RoutingContext originalRequest, LoggerInterface requestFactory) {
		String jwt = originalRequest.request().getHeader("X-User");
		String tokenId = originalRequest.request().getHeader("Authorization");
		LoggerInterface logger = requestFactory.createLocalLogger("Firewall", "getUser");

		if(jwt != null) {
			logger.log(Level.INFO, " Decoding JWT: " + jwt);
			return this.authenticator.decodeToken(jwt, requestFactory);
		}
		else if(tokenId != null) {
			logger.log(Level.INFO, " Getting JWT with ID: " + tokenId);
			return this.authenticator.getDecodedToken(tokenId, requestFactory);
		}
		else {
			logger.log(Level.INFO, " No user in request");
			return Future.succeededFuture(null);
		}
	}

	private Future<Integer> getPermission(RoutingContext originalRequest, String prefix, String uri, LoggerInterface requestFactory){
		return this.authorizator.getPermission(originalRequest.request().method().toString(), uri, requestFactory);
	}

	@Override
	public Future<JsonObject> check(RoutingContext originalRequest, LoggerInterface requestFactory) {
		Future<JsonObject> fut = Future.future();
		LoggerInterface logger = requestFactory.createLocalLogger("Firewall", "check");
		try {
			String uri = originalRequest.request().uri();
			String[] uri_parts = uri.split("/", 3);
			JsonObject response = new JsonObject();
			
			if(uri_parts.length < 3) {
				fut.fail(new LottoException(400, "BAD_URI", "Bad URI: " + uri));
			}
			else {
				logger.log(Level.INFO, "Request " + uri_parts[1] + " / " + uri_parts[2]);

				response.put("prefix", uri_parts[1]);
				response.put("uri", uri_parts[2]);

				switch(uri_parts[1]) {
					/*
					// Recuperación de valores por clave: Restringir por criterios de IP, criptográficamente...
					case "kv":
						fut.complete(response.put("action", "kv").put("key", uri_parts[2]));
						break;
					*/	
				
					// Allow always
					case "public":
						logger.log(Level.INFO, "Public!");
						fut.complete(response.put("action", "dispatch"));
						break;

					// User must be logged
					case "user":
					case "logged":
						logger.log(Level.INFO, "Getting user...");
						this.getUser(originalRequest, requestFactory).setHandler(userResult -> {
							try {
								AuthenticatedUserInterface user = userResult.result();
								if(userResult.failed()) {
									fut.fail(userResult.cause());
								}
								else if(user == null) {
									fut.fail(new LottoException(401, "MISSING_USER_TOKEN", "Missing user token in request"));
								}
								else {
									logger.log(Level.INFO, "User is logged: " + user.toString());
									fut.complete(response.put("action", "dispatch").put("jwt", user.getJWT()));
								}
							}
							catch(Throwable ex) {
								fut.fail(ex);
							}
						});

						break;

					// User must have must have ROLE_ADMIN or URI permission
					case "staff":
					case "customer":
						logger.log(Level.INFO, "Getting user...");
						this.getUser(originalRequest, requestFactory).setHandler(userResult -> {
							try {
								AuthenticatedUserInterface user = userResult.result();

								if(userResult.failed()) {
									fut.fail(userResult.cause());
								}
								else if(user == null) {
									fut.fail(new LottoException(401, "MISSING_USER_TOKEN", "Missing user token in request"));
								}
								else if(user.hasRole("ROLE_ADMIN")){
									logger.log(Level.INFO, "User is admin: " + user.toString());
									fut.complete(response.put("action", "dispatch").put("jwt", user.getJWT()));
								}
								else {
									logger.log(Level.INFO, "Checking permission...");
									this.getPermission(originalRequest, uri_parts[1], uri_parts[2], requestFactory).setHandler(permissionResult -> {
										try {
											Integer permission = permissionResult.result();

											if(permissionResult.failed()) {
												fut.fail(permissionResult.cause());
											}
											else if(permission == null){
												fut.fail(new LottoException(404, "MISSING_URL_PERMISSION", "URL doesnt match with active permissions"));
											}
											else if(!user.hasPermission(permission)){
												fut.fail(new LottoException(403, "USER_HAS_NOT_PERMISSION", "User has not permission"));
											}
											else {
												logger.log(Level.INFO, " User has permission " + permission + ": " + user.toString());
												fut.complete(response.put("action", "dispatch").put("jwt", user.getJWT()).put("permission", permission));
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
						break;
					
					// User must have ROLE_ADMIN
					case "tech":
					case "admin":
					default:
						logger.log(Level.INFO, " Getting user...");
						this.getUser(originalRequest, requestFactory).setHandler(userResult -> {
							AuthenticatedUserInterface user = userResult.result();
							try {
								if(userResult.failed()) {
									fut.fail(userResult.cause());
								}
								else if(user == null) {
									fut.fail(new LottoException(401, "MISSING_USER_TOKEN", "Missing user token in request"));
								}
								else if(!user.hasRole("ROLE_ADMIN")){
									fut.fail(new LottoException(403, "USER_IS_NOT_ADMIN", "User is not ROLE_ADMIN"));
								}
								else {
									logger.log(Level.INFO, " User is admin: " + user.toString());
									fut.complete(response.put("action", "dispatch").put("jwt", user.getJWT()));
								}
							}
							catch(Throwable ex) {
								fut.fail(ex);
							}
						});
				}
			}
		}
		catch(Throwable ex) {
			fut.fail(ex);
		}

		return fut;
	}
}
