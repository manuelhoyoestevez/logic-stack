package mhe.compiler.exception;

import java.io.IOException;

import mhe.compiler.logger.LogType;

public class CompilerIOException extends CompilerException {
    private static final long serialVersionUID = 3991281390266761012L;

    public CompilerIOException(LogType type, int row, int col, IOException exception) {
        super(type, row, col, exception.getMessage(), exception);
    }
}
