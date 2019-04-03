package mhe.proxy.impl;

import java.util.logging.Level;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import mhe.logger.LoggerInterface;
import mhe.common.MheException;
import mhe.common.ServiceCatalogInterface;

public class CustomDispatcher extends Dispatcher {

	public CustomDispatcher(Vertx vertx, ServiceCatalogInterface serviceCatalog, String mode, Boolean chunk) {
		super(vertx, serviceCatalog, mode, chunk);
	}

	protected Future<String> getServiceId(RoutingContext originalRequest, String jwt, LoggerInterface parentLogger) {
		Future<String> fut = Future.future();
		LoggerInterface logger = parentLogger.createLocalLogger("CustomDispatcher", "getServiceId");

		String x_service_id = originalRequest.request().getHeader("X-Service-Id");
		logger.log(Level.INFO, "X-Service-Id header: " + x_service_id);

		if(x_service_id != null) {
			fut.complete(x_service_id);
		}
		else {
			String uri = originalRequest.request().uri();
			logger.log(Level.INFO, "URI: " + uri);
			switch(uri) {
				case "/public/transaction/apcopay-response": fut.complete("wallets-service"); break;
				default: fut.fail(new MheException(400, "SERVICE_ID_NOT_RESOLVED", "Service ID not resolved"));
			}
		}
		return fut;
	}
}
