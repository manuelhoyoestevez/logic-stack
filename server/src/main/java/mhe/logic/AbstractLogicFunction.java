package mhe.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public abstract class AbstractLogicFunction implements LogicFunction {
    /**
     * Lista de literales
     */
    private List<String> literals;

    @Override
    public List<String> getLiterals() {
        return this.literals;
    }

    protected void setLiterals(List<String> literals) {
        this.literals = literals;
    }

    public static JSONObject values2Json(Map<String, Boolean> values) {
        return new JSONObject(values);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Boolean> json2Values(JSONObject json) {
        return new HashMap<String, Boolean>(json);
    }

    public static Map<String, Boolean> string2Values(String json) throws ParseException {
        return json2Values((JSONObject) new JSONParser().parse(json));
    }
}
