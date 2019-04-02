package lotto.common;

import java.util.List;

import io.vertx.core.json.JsonObject;

public interface ServiceParamsInterface {
	public String getNode();

	public String getAddress();

	public String getServiceId();

	public String getServiceName();

	public String getServiceAddress();

	public Integer getServicePort();

	public List<String> getServiceTags();

	public JsonObject toJsonObject();
	
	public String getPrefix();
	
	public Boolean isAllowed();
	
	public Boolean isChunked();
}
