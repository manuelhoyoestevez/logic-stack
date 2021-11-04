package mhe.logic.exception;

/**
 * TooManyLiteralsException.
 */
public class TooManyLiteralsException extends LogicException {
    private static final long serialVersionUID = -2276395717812205729L;

    public TooManyLiteralsException(Integer tried, Integer limit) {
        super("Maximum number of literals is " + limit + ". Tried: " + tried);
    }

}
