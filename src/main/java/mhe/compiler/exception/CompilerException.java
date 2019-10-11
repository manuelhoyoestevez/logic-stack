package mhe.compiler.exception;

import mhe.compiler.logger.LogType;

@SuppressWarnings("serial")
public class CompilerException extends Exception {
    private LogType type;
    private int row;
    private int col;

    public CompilerException(LogType type, int row, int col, String message, Throwable cause){
        super(message, cause);
        this.type = type;
        this.row = row;
        this.col = col;
    }

    public CompilerException(LogType type, int row, int col, String message) {
        this(type, row, col, message, null);
    }

    protected String buildMessage() {
        return "[" + row + ", " + col + ", " + type.toString() + "] ";
    }
}
