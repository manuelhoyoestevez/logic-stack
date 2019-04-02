package lotto.model;

import java.util.regex.Pattern;

import io.vertx.core.json.JsonObject;

public class Permission implements PermissionInterface {
	private Integer _id;
	private String method;
	private Pattern uriRegex;
	
	public Integer getId() {
		return this._id;
	}
	
	public String getMethod() {
		return this.method;
	}
	
	public String getUri() {
		Pattern aux = this.getUriRegex();
		return aux == null ? null : aux.pattern();
	}

	@Override
	public Pattern getUriRegex() {
		return this.uriRegex;
	}

	public JsonObject toJsonObject() {
		return new JsonObject()
				.put("_id", this.getId())
				.put("method", this.getMethod())
				.put("uri", this.getUri());
	}
	
	public String toString() {
		return this.toJsonObject().toString();
	}
	
	public Permission setId(Integer _id) {
		this._id = _id;
		return this;
	}
	
	public Permission setMethod(String method) {
		this.method = method;
		return this;
	}
	
	public Permission setUri(String uri) {
		this.uriRegex = Pattern.compile(uri);
		return this;
	}

	public Permission fromJsonObject(JsonObject json) {
		return this
				.setId(json.getInteger("_id"))
				.setMethod(json.getString("method"))
				.setUri(json.getString("uri"));
	}
}
