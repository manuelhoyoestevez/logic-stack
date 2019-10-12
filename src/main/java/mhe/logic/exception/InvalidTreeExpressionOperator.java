package mhe.logic.exception;

public class InvalidTreeExpressionOperator extends LogicException {
    private static final long serialVersionUID = -9062050965889754570L;

    public InvalidTreeExpressionOperator() {
        super("Missing logic operator");
    }

    public InvalidTreeExpressionOperator(String operator) {
        super("Invalid logic operator: " + operator);
    }
}
