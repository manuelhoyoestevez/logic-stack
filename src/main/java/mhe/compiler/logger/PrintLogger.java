package mhe.compiler.logger;

import java.io.PrintStream;
import java.util.logging.Level;

import mhe.compiler.LoggerInterface;

public class PrintLogger extends AbstractLogger {
	private PrintStream std;
	private PrintStream err;
	
	//private LogType[] toLog = { LogType.SEMANTIC, LogType.SEMANTIC };
	//LogType.STREAM, LogType.LEXICAL,
	
	public PrintLogger() {
		this.std = System.out;
		this.err = System.err;
	}

	@Override
	public LoggerInterface logError(LogType type, int row, int col, String message) {
		this.std.println(new Log(Level.SEVERE, type, row, col, message, getTabLevel()));
		return this;
	}

	@Override
	public LoggerInterface logMessage(LogType type, int row, int col, String message) {
		this.err.println(new Log(Level.SEVERE, type, row, col, message, getTabLevel()));
		return this;
	}

}
