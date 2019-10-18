package mhe.compiler.logger;

import java.util.List;
import java.util.LinkedList;
import java.util.logging.Level;

public class MemoryLogger extends AbstractLogger {
	private List<Log> logs;
	
	public MemoryLogger() {
		this.logs = new LinkedList<Log>();
	}

	@Override
	public Logger logError(LogType type, int row, int col, String message) {
		this.logs.add(new Log(Level.SEVERE, type, row, col, message, getTabLevel()));
		return this;
	}

	@Override
	public Logger logMessage(LogType type, int row, int col, String message) {
		this.logs.add(new Log(Level.INFO, type, row, col, message, getTabLevel()));
		return this;
	}
}
