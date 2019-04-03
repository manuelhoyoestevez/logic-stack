package mhe.logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Logger implements LoggerInterface {
	public static final String datetimeFormatString = "yyyy-MM-dd kk:mm:ss.SSS";
	private DateFormat dateFormat = new SimpleDateFormat(datetimeFormatString);
	private Map<String, String> args = null;
	
	public Logger(Map<String, String> args) {
		this.args = args;
	}
	
	@Override
	public LoggerInterface createLocalLogger(Map<String, String> args) {
		return new Logger(this.addArgs(args));
	}
	
	@Override
	public LoggerInterface createLocalLogger(String serial) {
		Map<String, String> addedArgs = new HashMap<String, String>();
		addedArgs.put("serial", serial);
		return this.createLocalLogger(addedArgs);
	}

	@Override
	public LoggerInterface createLocalLogger(String moduleName, String functionName) {
		Map<String, String> addedArgs = new HashMap<String, String>();
		addedArgs.put("module-name", moduleName);
		addedArgs.put("function-name", functionName);
		return this.createLocalLogger(addedArgs);
	}

	@Override
	public LoggerInterface createLocalLogger(String moduleName, String functionName, String printChannel) {
		Map<String, String> addedArgs = new HashMap<String, String>();
		addedArgs.put("module-name", moduleName);
		addedArgs.put("function-name", functionName);
		addedArgs.put("print-channel", printChannel);
		return this.createLocalLogger(addedArgs);
	}
	
	private PrintStream getPrintStream(String printChannel) {
		PrintStream channel = null;
		
		switch(printChannel.toLowerCase()) {
			case "none":
				break;
			case "default":
				channel = System.out;
				break;
			case "error":
				channel = System.err;
				break;
		}
		
		return channel;
	}
	
	@Override
	public void log(Level level, String message) {
		String serial       = this.getArg("serial", "XXXXXX");
		String serviceName  = this.getArg("service-name", "-");
		String moduleName   = this.getArg("module-name", "-");
		String functionName = this.getArg("function-name", "-");
		String printChannel = this.getArg("print-channel", "default");
		PrintStream channel = this.getPrintStream(printChannel);
	
		if(channel != null) {
			channel.println("[" + now() + "][" + serial + "][" + serviceName + "][" + moduleName + "][" + functionName + "] " + message);
		}
	}
	
	@Override
	public void debuglog(Level level) {
		String printChannel = this.getArg("print-channel", "default");
		PrintStream channel = this.getPrintStream(printChannel);
		
		if(channel != null) {
			channel.print("[" + now() + "]");
			for(String key : args.keySet()) {
				channel.print("[" + key + ": " + args.get(key) + "]");
			}
			channel.println();
		}
	}
	
	private String now() {
        return dateFormat.format(new Date());
	}
	
	private String getArg(String key, String def) {
		String value = args.get(key);
		
		if(value == null) {
			value = def;
		}
		
		return value;
	}
	
	private Map<String, String> addArgs(Map<String, String> args){
		Map<String, String> newArgs = copyArgs(this.args);
		
		for(String key : args.keySet()) {
			newArgs.put(key, args.get(key));
		}
		
		return newArgs;
	}
	
	private static Map<String, String> copyArgs(Map<String, String> args){
		Map<String, String> ret = new HashMap<String, String>();
		
		for(String key : args.keySet()) {
			ret.put(key, args.get(key));
		}
		
		return ret;
	}
}
