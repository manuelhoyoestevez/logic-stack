package lotto.logger;

import java.util.Map;
import java.util.logging.Level;

public interface LoggerInterface {

	public LoggerInterface createLocalLogger(Map<String, String> args);
	public LoggerInterface createLocalLogger(String serial);
	public LoggerInterface createLocalLogger(String moduleName, String functionName);
	public LoggerInterface createLocalLogger(String moduleName, String functionName, String channel);
	
	public void log(Level level, String message);
	public void debuglog(Level level);
}
