package mhe.compiler.logger;

import mhe.compiler.CompilerException;
import mhe.compiler.LoggerInterface;
import mhe.compiler.TokenInterface;

public abstract class AbstractLogger implements LoggerInterface {
	private int tabLevel = 0;
	
	public int getTabLevel() {
		return tabLevel;
	}
	
	@Override
	public LoggerInterface incTabLevel() {
		++this.tabLevel;
		return this;
	}

	@Override
	public LoggerInterface decTabLevel() {
		--this.tabLevel;
		return this;
	}
	
	@Override
	public LoggerInterface logError(LogType type, TokenInterface token, String message) throws CompilerException {
		return this.logError(type, token.getRow(), token.getCol(), message);
	}

	@Override
	public LoggerInterface logMessage(LogType type, TokenInterface token, String message) {
		return this.logMessage(type, token.getRow(), token.getCol(), message);
	}
}
