package mhe.proxy;

import java.util.List;
import io.vertx.core.Future;

public interface ConfigManagerInterface {
	Future<ConfigParamsInterface> getConfigParams(List<String> args);
}
