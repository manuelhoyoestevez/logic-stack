package mhe.logic.exception;

import org.json.simple.parser.ParseException;

/**
 * JsonParseException.
 */
public class JsonParseException extends LogicException {
    private static final long serialVersionUID = 5677145064130876604L;

    public JsonParseException(ParseException parseException) {
        super(parseException.getMessage(), parseException);
    }

}
