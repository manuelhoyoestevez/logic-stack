package mhe.compiler;

public interface LoggerInterface {
	public LoggerInterface addError(LogType type, int row, int col, String message, boolean thro) throws CompilerException;
	public LoggerInterface addError(LogType type, TokenInterface token, String string, boolean thro) throws CompilerException;
	
	public LoggerInterface logError(LogType type, int row, int col, String message);
	public LoggerInterface logError(LogType type, TokenInterface token, String string);
	
	public LoggerInterface throwError(LogType type, int row, int col, String message) throws CompilerException;
	public LoggerInterface throwError(LogType type, TokenInterface token, String string) throws CompilerException;
	
	public LoggerInterface logMessage(LogType type, int row, int col, String message);
	public LoggerInterface logMessage(LogType type, TokenInterface token, String message);

	public LoggerInterface incTabLevel();
	public LoggerInterface decTabLevel();
}
