package mhe.compiler.logger;

import java.util.logging.Level;

public class Log implements LogInterface {
	private Level level;
	private LogType type;
	private int row;
	private int col;
	private String message;
	private int tabLevel;
	
	public Log(Level level, LogType type, int row, int col, String message, int tabLevel) {
		this
		.setLevel(level)
		.setType(type)
		.setRow(row)
		.setCol(col)
		.setMessage(message)
		.setTabLevel(tabLevel);
	}

	@Override
	public Level getLevel() {
		return level;
	}
	
	public Log setLevel(Level level) {
		this.level = level;
		return this;
	}
	
	@Override
	public LogType getType() {
		return type;
	}
	
	public Log setType(LogType type) {
		this.type = type;
		return this;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
	public Log setMessage(String message) {
		this.message = message;
		return this;
	}
	
	@Override
	public int getRow() {
		return row;
	}
	
	public Log setRow(int row) {
		this.row = row;
		return this;
	}
	
	@Override
	public int getCol() {
		return col;
	}
	
	public Log setCol(int col) {
		this.col = col;
		return this;
	}
	
	@Override
	public int getTabLevel() {
		return tabLevel;
	}
	
	public Log setTabLevel(int tabLevel) {
		this.tabLevel = tabLevel;
		return this;
	}
	
	public String toString() {
		String prefix = "[" + this.getRow() + ", " + this.getCol() + "][" + this.getType() + "]\t";
		
		for(int i = 0; i < this.tabLevel; i++) {
			prefix += "\t";
		}
		
		return prefix + this.getMessage();
	}
}
