package mhe.compiler.logger;

import mhe.compiler.exception.CompilerException;

public class DummyLogger extends AbstractLogger {

    @Override
    public Logger logError(LogType type, int row, int col, String message) throws CompilerException {
        throw new CompilerException(type, col, col, message);
    }

    @Override
    public Logger logMessage(LogType type, int row, int col, String message) {
        return this;
    }

}
