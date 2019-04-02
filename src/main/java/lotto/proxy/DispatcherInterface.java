package lotto.proxy;

import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;
import lotto.logger.LoggerInterface;

public interface DispatcherInterface {
	public Future<Void> dispatch(RoutingContext originalRequest, String jwt, LoggerInterface requestFactory);
}
