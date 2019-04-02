package lotto.proxy.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.*;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import lotto.common.LottoException;
import lotto.common.ServiceCatalogInterface;
import lotto.common.ServiceParamsInterface;
import lotto.logger.LoggerInterface;
import lotto.model.Permission;
import lotto.model.PermissionInterface;
import lotto.proxy.AuthorizatorInterface;

public class Authorizator implements AuthorizatorInterface {

	private Vertx vertx;
	private ServiceCatalogInterface catalogService;
	private String authServiceId;
	private List<PermissionInterface> permissions = null;
	private Boolean reportPermissionUse;

	public Authorizator(Vertx vertx, ServiceCatalogInterface catalogService, String authServiceId, Boolean reportPermissionUse) {
		this.vertx = vertx;
		this.catalogService = catalogService;
		this.authServiceId = authServiceId;
		this.reportPermissionUse = reportPermissionUse;
	}

	private Future<List<PermissionInterface>> getAllPermissions(LoggerInterface requestFactory){
		Future<List<PermissionInterface>> fut = Future.future();
		LoggerInterface logger = requestFactory.createLocalLogger("Authorizator", "getAllPermissions");
		
		try {
			logger.log(Level.INFO, " Getting auth service info: " + this.authServiceId);
			this.catalogService.getNextServiceParams(this.authServiceId, requestFactory).setHandler(ServiceParamsResult -> {
				try {
					// Línea solo para desarrollar: retrirar después de las pruebas
					this.permissions = null;

					if(this.permissions != null) {
						logger.log(Level.INFO, " Permissions cached!");
						fut.complete(this.permissions);
					}
					else {
						logger.log(Level.INFO, " Permissions not cached! Requesting Auth...");
						ServiceParamsInterface serviceParams = ServiceParamsResult.result();
						HttpClient httpClient = this.vertx.createHttpClient(new HttpClientOptions());
						HttpClientRequest c_req = httpClient.request(
								HttpMethod.GET,
								serviceParams.getServicePort(),
								serviceParams.getServiceAddress(),
								"/auth/permission/list",
								c_res ->
						{
							try {
								logger.log(Level.INFO, " => " + c_res.statusCode());

								c_res.exceptionHandler(ex -> {
									fut.fail(ex);
								});

								c_res.bodyHandler(body -> {
									httpClient.close();
									if(c_res.statusCode() < 400) {
										JsonArray permissionsJson = body.toJsonObject().getJsonArray("permissions");
										logger.log(Level.INFO, " Permissions: " + permissionsJson.toString());
										
										this.permissions = new ArrayList<PermissionInterface>();
										
										for(int i = 0; i < permissionsJson.size();i++) {
											this.permissions.add(new Permission().fromJsonObject(permissionsJson.getJsonObject(i)));
										}
										
										fut.complete(this.permissions);
									}
									else {
										fut.fail(new LottoException(c_res.statusCode(), "AUTH_ERROR", "Auth error", body.toString()));
									}
								});

								logger.log(Level.INFO, " Handlers set!");
							}
							catch(Throwable ex) {
								fut.fail(ex);
							}
						});

						c_req.putHeader("content-type", "application/json");
						//c_req.setChunked(true);
						c_req.end();
						logger.log(Level.INFO, " Auth request sent!");
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

	private Future<Boolean> reportPermissionUse(Integer permissionId, LoggerInterface requestFactory){
		Future<Boolean> fut = Future.future();
		LoggerInterface logger = requestFactory.createLocalLogger("Authorizator", "reportPermissionUse");

		try {
			logger.log(Level.INFO, " Getting auth service info: " + this.authServiceId);
			this.catalogService.getNextServiceParams(this.authServiceId, requestFactory).setHandler(ServiceParamsResult -> {
				try {
						logger.log(Level.INFO, "Requesting Auth...");
						ServiceParamsInterface serviceParams = ServiceParamsResult.result();
						HttpClient httpClient = this.vertx.createHttpClient(new HttpClientOptions());
						HttpClientRequest c_req = httpClient.request(
								HttpMethod.POST,
								serviceParams.getServicePort(),
								serviceParams.getServiceAddress(),
								"/auth/permission/report/" + permissionId,
								c_res ->
						{
							try {
								logger.log(Level.INFO, " => " + c_res.statusCode());

								c_res.exceptionHandler(ex -> {
									fut.fail(ex);
								});

								c_res.bodyHandler(body -> {
									httpClient.close();
									if(c_res.statusCode() < 400) {
										fut.complete(true);
									}
									else {
										fut.fail(new LottoException(c_res.statusCode(), "AUTH_ERROR", "Auth error", body.toString()));
									}
								});

								logger.log(Level.INFO, " Handlers set!");
							}
							catch(Throwable ex) {
								fut.fail(ex);
							}
						});

						c_req.putHeader("content-type", "application/json");
						//c_req.setChunked(true);
						c_req.end();
						logger.log(Level.INFO, " Auth request sent!");
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
	public Future<Integer> getPermission(String method, String uri, LoggerInterface requestFactory) {
		Future<Integer> fut = Future.future();
		LoggerInterface logger = requestFactory.createLocalLogger("Authorizator", "getPermission");
		try {
			logger.log(Level.INFO, " Getting permissions...");
			this.getAllPermissions(requestFactory).setHandler(permResult -> {
				try {
					if(permResult.failed()) {
						fut.fail(permResult.cause());
					}
					else {
						logger.log(Level.INFO, "Permissions got! Checking URI: " + uri);
						
						for(PermissionInterface permission : permResult.result()) {
							if(permission.getMethod() != null && permission.getMethod().equalsIgnoreCase(method)) {
								logger.log(Level.INFO, "Permission: " + permission.toString());
								Pattern uriRegex = permission.getUriRegex();
								Matcher matches = uriRegex.matcher(uri);
								if(matches.matches()) {
									logger.log(Level.INFO, "Permission matched: " + permission.getId());
									fut.complete(permission.getId());
									if(this.reportPermissionUse) {
										this.reportPermissionUse(permission.getId(), logger).setHandler(res -> {
											if(res.failed()) {
												logger.log(Level.WARNING, "Permission not reported! " + res.cause().toString());
											}
											else {
												logger.log(Level.INFO, "Permission reported!");
											}
										});
									}
									return;
								}
							}
						}
						fut.complete(null);
						return;
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
