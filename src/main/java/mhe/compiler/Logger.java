package mhe.compiler;

import java.io.PrintStream;

public class Logger implements LoggerInterface {
	private PrintStream std;
	private PrintStream err;
	private int tabLevel = 0;
	
	private LogType[] toLog = { LogType.SEMANTIC, LogType.SEMANTIC };
	
	//LogType.STREAM, LogType.LEXICAL,
	
	protected boolean containsLogType(LogType type) {
		boolean found = false;
		
		for(int i = 0; !found && i < this.toLog.length; i++) {
			if(this.toLog[i] == type) {
				found = true;
			}
		}
		
		return found;
	}
	
	public Logger(){
		this.std = System.out;
		this.err = System.err;
	}
	
	protected String buildMessage(LogType type, int row, int col, String message) {
		return "[" + row + ", " + col + ", " + type.toString() + "] " + message;
	}
	
	protected CompilerException newException(LogType type, int row, int col, String message) {
		return new CompilerException(type, row, col, message);
	}
	
	@Override
	public LoggerInterface incTabLevel() {
		this.tabLevel++;
		return this;
	}

	@Override
	public LoggerInterface decTabLevel() {
		this.tabLevel--;
		return this;
	}

	@Override
	public LoggerInterface logMessage(LogType type, int row, int col, String message) {
		if(this.containsLogType(type)) {
			String prefix = "";
			for(int i = 0; i < this.tabLevel; i++) {
				prefix += "\t";
			}
			
			this.std.println(prefix + "[" + row + ", " + col + ", " + type.toString() + "] " + message);
		}
		return this;
	}
	
	@Override
	public LoggerInterface logMessage(LogType type, TokenInterface token, String message) {
		return this.logMessage(type, token.getRow(), token.getCol(), message);
	}

	@Override
	public LoggerInterface addError(LogType type, int row, int col, String message, boolean thro) throws CompilerException {
		if(this.containsLogType(type)) {
			String prefix = "";
			String mess = this.buildMessage(type, row, col, message);
			
			for(int i = 0; i < this.tabLevel; i++) {
				prefix += "\t";
			}
			
			this.err.println(prefix + mess);
		}
		
		if(thro) {
			throw this.newException(type, row, col, message);
		}
		else {
			return this;
		}
	}
	
	@Override
	public LoggerInterface addError(LogType type, TokenInterface token, String message, boolean thro) throws CompilerException {
		return this.addError(type, token.getRow(), token.getCol(), message, thro);
	}

	@Override
	public LoggerInterface logError(LogType type, int row, int col, String message) {
		try {
			return this.addError(type, row, col, message, false);
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public LoggerInterface logError(LogType type, TokenInterface token, String message) {
		return this.logError(type, token.getRow(), token.getCol(), message);
	}

	@Override
	public LoggerInterface throwError(LogType type, int row, int col, String message) throws CompilerException {
		return this.addError(type, row, col, message, true);
	}

	@Override
	public LoggerInterface throwError(LogType type, TokenInterface token, String message) throws CompilerException {
		return this.throwError(type, token.getRow(), token.getCol(), message);
	}
}
