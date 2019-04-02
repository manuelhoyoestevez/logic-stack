package lotto.common;

import io.vertx.core.Future;
import lotto.logger.LoggerInterface;

public interface ParametersSetInterface {
	public Future<String> getParameterValueByKey(String key, LoggerInterface requestFactory);
	public Future<String> getParameterValueByKey(String key, String def, LoggerInterface requestFactory);
}
