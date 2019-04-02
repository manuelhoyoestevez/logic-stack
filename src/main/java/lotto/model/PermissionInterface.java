package lotto.model;

import java.util.regex.Pattern;
import io.vertx.core.json.JsonObject;

public interface PermissionInterface {
	public Integer getId();
	public String getMethod();
	public String getUri();
	public Pattern getUriRegex();
	public JsonObject toJsonObject();
}
