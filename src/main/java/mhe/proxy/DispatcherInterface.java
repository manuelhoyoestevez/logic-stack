package mhe.proxy;

import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;
import mhe.logger.LoggerInterface;

public interface DispatcherInterface {
	public Future<Void> dispatch(RoutingContext originalRequest, String jwt, LoggerInterface requestFactory);
}
