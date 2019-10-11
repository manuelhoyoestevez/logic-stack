package mhe.compiler.logger;

import mhe.compiler.LoggerInterface;
import mhe.compiler.exception.CompilerException;

public class Logger extends PrintLogger {
	
	public LoggerInterface logError(LogType type, int row, int col, String message) throws CompilerException {
		throw new CompilerException(type, col, col, message);
	}

}
