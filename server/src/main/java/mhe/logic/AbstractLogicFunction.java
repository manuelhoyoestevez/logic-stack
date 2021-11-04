package mhe.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;

/**
 * AbstractLogicFunction.
 */
public abstract class AbstractLogicFunction implements LogicFunction {
    /**
     * Lista de literales.
     */
    private List<String> literals;

    public static JSONObject values2Json(Map<String, Boolean> values) {
        return new JSONObject(values);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Boolean> json2Values(JSONObject json) {
        return new HashMap<String, Boolean>(json);
    }

    @Override
    public List<String> getLiterals() {
        return this.literals;
    }

    protected void setLiterals(List<String> literals) {
        this.literals = literals;
    }
}
