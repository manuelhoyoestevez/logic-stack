package mhe.compiler.exception;

import java.io.IOException;

public class CompilerIOException extends CompilerException {
    private static final long serialVersionUID = 3991281390266761012L;

    public CompilerIOException(int row, int col, IOException exception) {
        super(row, col, exception.getMessage(), exception);
    }
}
