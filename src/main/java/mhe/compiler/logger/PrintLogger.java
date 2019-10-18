package mhe.compiler.logger;

import java.io.PrintStream;
import java.util.logging.Level;

import mhe.compiler.exception.CompilerException;

public class PrintLogger extends AbstractLogger {
	private PrintStream std;
	private PrintStream err;

	public PrintLogger() {
		this.std = System.out;
		this.err = System.err;
	}

	@Override
	public Logger logError(LogType type, int row, int col, String message) throws CompilerException {
		this.err.println(new Log(Level.SEVERE, type, row, col, message, getTabLevel()));
		return this;
	}

	@Override
	public Logger logMessage(LogType type, int row, int col, String message) {
		this.std.println(new Log(Level.SEVERE, type, row, col, message, getTabLevel()));
		return this;
	}
}
