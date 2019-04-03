package mhe.common;

import io.vertx.core.json.JsonObject;

public interface ServiceExtraInfoInterface {
	public String getServiceId();
	public String getPrefix();
	public Boolean isAllowed();
	public Boolean isChunked();
	public JsonObject toJsonObject();
}
