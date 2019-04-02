package lotto.consul;

import java.util.List;
import io.vertx.core.json.JsonObject;
import lotto.common.ServiceExtraInfoInterface;
import lotto.common.ServiceParamsInterface;
import com.ecwid.consul.v1.catalog.model.CatalogService;

public class ConsulServiceParams implements ServiceParamsInterface {
	private CatalogService consulCatalogservice;
	private ServiceExtraInfoInterface extraInfo;
	
	public ConsulServiceParams(CatalogService consulCatalogservice, ServiceExtraInfoInterface extraInfo) {
		this.consulCatalogservice = consulCatalogservice;
		this.extraInfo = extraInfo;
	}

	public CatalogService getConsulCatalogservice() {
		return this.consulCatalogservice;
	}

	@Override
	public String getNode() {
		return this.getConsulCatalogservice().getNode();
	}

	@Override
	public String getAddress() {
		return this.getConsulCatalogservice().getAddress();
	}

	@Override
	public String getServiceId() {
		return this.getConsulCatalogservice().getServiceId();
	}

	@Override
	public String getServiceName() {
		return this.getConsulCatalogservice().getServiceName();
	}

	@Override
	public String getServiceAddress() {
		return this.getConsulCatalogservice().getServiceAddress();
	}

	@Override
	public Integer getServicePort() {
		return this.getConsulCatalogservice().getServicePort();
	}

	@Override
	public List<String> getServiceTags() {
		return this.getConsulCatalogservice().getServiceTags();
	}
	
	@Override
	public String getPrefix() {
		return this.extraInfo.getPrefix();
	}

	@Override
	public Boolean isAllowed() {
		return this.extraInfo.isAllowed();
	}
	
	@Override
	public Boolean isChunked() {
		return this.extraInfo.isChunked();
	}

	@Override
	public JsonObject toJsonObject() {
		return new JsonObject()
		.put("Node", this.getNode())
		.put("Address", this.getAddress())
		.put("ServiceID", this.getServiceId())
		.put("ServiceName", this.getServiceName())
		.put("ServiceAddress", this.getServiceAddress())
		.put("ServicePort", this.getServicePort())
		.put("ServiceTags", this.getServiceTags())
		.put("Prefix", this.getPrefix())
		.put("Allowed", this.isAllowed());
	}
	
	@Override
	public String toString() {
		return this.toJsonObject().toString();
	}
}
