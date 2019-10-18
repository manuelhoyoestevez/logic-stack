package mhe.compiler.logger;

import mhe.compiler.exception.CompilerException;

public class DefaultLogger extends PrintLogger {
	
	public Logger logError(LogType type, int row, int col, String message) throws CompilerException {
		throw new CompilerException(type, col, col, message);
	}

}
