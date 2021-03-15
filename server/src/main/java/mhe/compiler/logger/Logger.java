package mhe.compiler.logger;

import mhe.compiler.exception.CompilerException;

public interface Logger {
    public Logger logError(LogType type, int row, int col, String message) throws CompilerException;
    public Logger logMessage(LogType type, int row, int col, String message);
    public Logger incTabLevel();
    public Logger decTabLevel();
}
