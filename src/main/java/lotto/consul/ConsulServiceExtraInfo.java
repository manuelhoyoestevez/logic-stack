package lotto.consul;

import io.vertx.core.json.JsonObject;
import lotto.common.ServiceExtraInfoInterface;

public class ConsulServiceExtraInfo implements ServiceExtraInfoInterface {
	private String serviceId;
	private String prefix;
	private Boolean allowed;
	private Boolean chunked;
	
	@Override
	public String getServiceId() {
		return this.serviceId;
	}

	@Override
	public String getPrefix() {
		return this.prefix;
	}
	
	@Override
	public Boolean isAllowed() {
		return this.allowed;
	}
	
	@Override
	public Boolean isChunked() {
		return this.chunked;
	}

	@Override
	public JsonObject toJsonObject() {
		return new JsonObject()
		.put("ServiceID", this.getServiceId())
		.put("Prefix", this.getPrefix())
		.put("Allowed", this.isAllowed())
		.put("Chunked", this.isChunked());
	}
	
	
	private ConsulServiceExtraInfo setServiceId(String serviceId) {
		this.serviceId = serviceId;
		return this;
	}
	
	private ConsulServiceExtraInfo setPrefix(String prefix) {
		this.prefix = prefix == null ? "" : prefix;
		return this;
	}
	
	private ConsulServiceExtraInfo setAllowed(Boolean allowed) {
		this.allowed = allowed == null ? false : allowed;
		return this;
	}
	
	private ConsulServiceExtraInfo setChunked(Boolean chunked) {
		this.chunked = chunked;
		return this;
	}

	public ConsulServiceExtraInfo setValues(String serviceId, String prefix, Boolean allowed, Boolean chunked) {
		return this
		.setServiceId(serviceId)
		.setPrefix(prefix)
		.setAllowed(allowed)
		.setChunked(chunked);
	}
	
	public ConsulServiceExtraInfo fromJsonObject(String serviceId, JsonObject extraInfo) {
		return this.setValues(
				serviceId,
				extraInfo.getString("prefix", null),
				extraInfo.getBoolean("allowed", false),
				extraInfo.getBoolean("chunked", null)
		);
	}
}
