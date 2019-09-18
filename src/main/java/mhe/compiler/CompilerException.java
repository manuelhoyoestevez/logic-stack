package mhe.compiler;

@SuppressWarnings("serial")
public class CompilerException extends Exception {
	private LogType type;
	private int row;
	private int col;
	
	public CompilerException(LogType type, int row, int col, String message){
		super(message);
		this.type = type;
		this.row = row;
		this.col = col;
	}
	
	protected String buildMessage() {
		return "[" + row + ", " + col + ", " + type.toString() + "] ";
	}
}
