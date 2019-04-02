package lotto.proxy;

import lotto.common.ServiceCatalogInterface;
import lotto.logger.LoggerInterface;

public interface ConfigParamsInterface {
	Integer getPort();
	Integer getTimeout();
	ServiceCatalogInterface getServiceCatalog();
	FirewallInterface getFirewall();
	DispatcherInterface getDispatcher();
	LoggerInterface getLoggerFactory();
}
