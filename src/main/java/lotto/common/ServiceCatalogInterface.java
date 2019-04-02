package lotto.common;

import java.util.List;
import io.vertx.core.Future;
import lotto.logger.LoggerInterface;

public interface ServiceCatalogInterface {
	public Future<ServiceParamsInterface> getNextServiceParams(String serviceId, LoggerInterface requestFactory);
	public Future<List<ServiceParamsInterface>> getServiceParams(String serviceId, LoggerInterface requestFactory);
	public void setListColor(Boolean c);
}
