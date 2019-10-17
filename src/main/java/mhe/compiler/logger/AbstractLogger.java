package mhe.compiler.logger;

import mhe.compiler.LoggerInterface;

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
}
