package lotto.proxy;

import io.vertx.core.Future;
import lotto.logger.LoggerInterface;

public interface AuthorizatorInterface {
	public Future<Integer> getPermission(String method, String uri, LoggerInterface requestFactory);
}
