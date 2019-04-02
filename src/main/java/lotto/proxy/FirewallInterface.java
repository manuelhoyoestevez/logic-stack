package lotto.proxy;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lotto.logger.LoggerInterface;

public interface FirewallInterface {
	public Future<JsonObject> check(RoutingContext request, LoggerInterface requestFactory);
}
