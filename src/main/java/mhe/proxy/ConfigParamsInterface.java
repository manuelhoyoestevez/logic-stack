package mhe.proxy;

import mhe.logger.LoggerInterface;
import mhe.common.ServiceCatalogInterface;

public interface ConfigParamsInterface {
	Integer getPort();
	Integer getTimeout();
	ServiceCatalogInterface getServiceCatalog();
	FirewallInterface getFirewall();
	DispatcherInterface getDispatcher();
	LoggerInterface getLoggerFactory();
}
