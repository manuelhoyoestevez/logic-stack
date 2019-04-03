package mhe.common;

import io.vertx.core.json.JsonObject;

public class MheResponse {
	private Integer status = null;
	private JsonObject body = null;
	
	public MheResponse(Integer status, JsonObject body) {
		this.status = status;
		this.body = body;
	}
	
	public Integer getStatus() {
		return this.status;
	}
	
	public JsonObject getBody() {
		return this.body;
	}
}
