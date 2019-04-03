package mhe.proxy;

import io.vertx.core.Future;
import mhe.logger.LoggerInterface;

public interface AuthorizatorInterface {
	public Future<Integer> getPermission(String method, String uri, LoggerInterface requestFactory);
}
