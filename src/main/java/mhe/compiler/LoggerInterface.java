package mhe.compiler;

import mhe.compiler.exception.CompilerException;
import mhe.compiler.logger.LogType;

public interface LoggerInterface {
	public LoggerInterface logError(LogType type, int row, int col, String message) throws CompilerException ;
	public LoggerInterface logError(LogType type, TokenInterface token, String message) throws CompilerException ;
	
	public LoggerInterface logMessage(LogType type, int row, int col, String message);
	public LoggerInterface logMessage(LogType type, TokenInterface token, String message);

	public LoggerInterface incTabLevel();
	public LoggerInterface decTabLevel();
}
