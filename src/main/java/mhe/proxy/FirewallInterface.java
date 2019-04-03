package mhe.proxy;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import mhe.logger.LoggerInterface;

public interface FirewallInterface {
	public Future<JsonObject> check(RoutingContext request, LoggerInterface requestFactory);
}
