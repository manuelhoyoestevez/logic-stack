package lotto.proxy.impl;

import lotto.common.ServiceCatalogInterface;
import lotto.logger.LoggerInterface;
import lotto.proxy.ConfigParamsInterface;
import lotto.proxy.DispatcherInterface;
import lotto.proxy.FirewallInterface;

public class ConfigParams implements ConfigParamsInterface {

	private Integer port;
	private Integer timeout;
	private FirewallInterface firewall;
	private DispatcherInterface dispatcher;
	private ServiceCatalogInterface serviceCatalog;
	private LoggerInterface loggerFactory;
	
	public ConfigParams(
			Integer port,
			Integer timeout,
			FirewallInterface firewall,
			DispatcherInterface dispatcher,
			ServiceCatalogInterface serviceCatalog,
			LoggerInterface loggerFactory
	) {
		super();
		this
		.setPort(port)
		.setTimeout(timeout)
		.setFirewall(firewall)
		.setDispatcher(dispatcher)
		.setServiceCatalog(serviceCatalog)
		.setLoggerFactory(loggerFactory);
	}

	@Override
	public FirewallInterface getFirewall() {
		return this.firewall;
	}

	@Override
	public DispatcherInterface getDispatcher() {
		return this.dispatcher;
	}
	
	@Override
	public ServiceCatalogInterface getServiceCatalog() {
		return this.serviceCatalog;
	}
	
	@Override
	public Integer getPort() {
		return this.port;
	}

	@Override
	public Integer getTimeout() {
		return this.timeout;
	}
	
	@Override
	public LoggerInterface getLoggerFactory() {
		return this.loggerFactory;
	}

	protected ConfigParams setPort(Integer port) {
		this.port = port;
		return this;
	}

	protected ConfigParams setTimeout(Integer timeout) {
		this.timeout = timeout;
		return this;
	}
	
	protected ConfigParams setFirewall(FirewallInterface firewall) {
		this.firewall = firewall;
		return this;
	}
	
	protected ConfigParams setDispatcher(DispatcherInterface dispatcher) {
		this.dispatcher = dispatcher;
		return this;
	}
	
	protected ConfigParams setServiceCatalog(ServiceCatalogInterface serviceCatalog) {
		this.serviceCatalog = serviceCatalog;
		return this;
	}
	
	protected ConfigParams setLoggerFactory(LoggerInterface loggerFactory) {
		this.loggerFactory = loggerFactory;
		return this;
	}
	
	@Override
	public String toString() {
		return "{ Port: " + port + ", Time out: " + timeout + " }";
	}
}
