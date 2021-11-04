package mhe.compiler.exception;

import java.io.IOException;

/**
 * CompilerIOException.
 */
public class CompilerIoException extends CompilerException {
    private static final long serialVersionUID = 3991281390266761012L;

    public CompilerIoException(int row, int col, IOException exception) {
        super(row, col, exception.getMessage(), exception);
    }
}
