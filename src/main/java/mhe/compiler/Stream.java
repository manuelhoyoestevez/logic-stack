package mhe.compiler;

import java.io.*;

/** Implementación de Stream con objetos de tipo 'io.Reader'
 * @author  Manuel Hoyo Estévez
 */
public class Stream implements StreamInterface {
	/** */
	private final static char CHRERROR = 0;
	
	/** */
	private final static char STREND = 65535;
	
	/** */
	private char chrError = CHRERROR;
	
	/** */
	private char strEnd   = STREND;
	
	/** Contador de posiciones */
	//private int pos;
	
	/** Contador de columnas */
	private int col;
	
	/** Contador de filas */
	private int row;
    
    /** Inidicador de posición */
	//private int fixedpos;	
	
	/** Inidicador de columna */
	private int fixedcol;
	
	/** Inidicador de fila */
	private int fixedrow;
    
    /** Carácter actual */
	private char chr;
    
    /** Carácter de marca */
	private char mark;
    
    /** Indicador de salto */
	private boolean jump;
    
    /** Lexema del componente léxico */
	private String lexem;
	
	/** */
	private LoggerInterface logger = null;

	/** */
	private Reader reader = null;
    
    public Stream(Reader reader, LoggerInterface logger) {
		this.setReader(reader);
		this.setLogger(logger);
	}
    
    @Override
	public char getErrorCategory() {
		return this.chrError;
	}
    
    public StreamInterface setErrorCategory(char chrError) {
    	this.chrError = chrError;
    	return this;
    }

	@Override
	public char getEndCategory() {
		return this.strEnd;
	}
	
	public StreamInterface setEndCategory(char strEnd) {
		this.strEnd = strEnd;
		return this;
	}

	@Override
	public LoggerInterface getLogger() {
		return this.logger;
	}
	
	public Loggable setLogger(LoggerInterface logger) {
		this.logger = logger;
		return this;
	}
    
    @Override
	public Reader getReader() {
		return this.reader;
	}
	
	public StreamInterface setReader(Reader reader) {
		this.reader = reader;
		return this;
	}

	@Override
    public int getRowNumber() {
    	return this.fixedrow;
    }
    
	@Override
	public int getColNumber() {
		return this.fixedcol;
	}
	
	@Override
	public char getCurrentCharacter() {
		return this.chr;
	}
	
	@Override
	public boolean isFinished(){
		return this.chr == this.getEndCategory();
	}	
	
	@Override
	public String getLexeme() {
		return this.lexem.toString();
	}
	
	@Override
	public char matchCharacter(char c) throws Exception {
		if(this.chr == c) {
			this.getLogger().logMessage(
					LogType.STREAM, 
					this.fixedrow, 
					this.fixedcol, 
					"Caracter valido: Recibido caracter '" + c + "'"
			);
			return this.getNextCharacter();
		}
		else{
			this.getLogger().logError(
					LogType.STREAM, 
					this.fixedrow, 
					this.fixedcol, 
					"Caracter no valido: Se esperaba caracter '" + c + "' en lugar de '" + this.chr + "'"
			);
			return this.getErrorCategory();
		}
	}
 
    @Override
    public void resetLexeme() {
    	this.getLogger().logMessage(
				LogType.STREAM, 
				this.fixedrow, 
				this.fixedcol, 
				"Lexema a resetar: " + this.lexem
		);
    	
		this.lexem = new String();
		this.fixedcol = this.col;
		this.fixedrow = this.row;
		//this.fixedpos = this.pos;
	}
    
    @Override
	public char getBackCharacter() throws IOException{    	
    	this.getReader().reset();
		this.chr = this.mark;			
		this.lexem = this.lexem.substring(0, this.lexem.length() - 1);
		if(this.jump)
			this.col--;
		else
			this.row--;
		//this.pos--;
		
		this.getLogger().logMessage(
				LogType.STREAM, 
				this.fixedrow, 
				this.fixedcol, 
				"Retrocedido caracter: " + this.lexem
		);
		
		return this.chr;
	}
	
	@Override
	public char getNextCharacter() throws IOException {
		if(this.chr != this.getEndCategory()){			
			this.getReader().mark(1);
			this.mark = this.chr;			
			this.chr = (char)this.getReader().read();
			this.lexem += (this.chr);			
			//this.pos++;
			if(this.chr == '\n'){
				this.col++;
				this.row = 1;
				this.jump = true;
			}
			else{
				this.row++;
				this.jump = false;
			}			
		}
		
		this.getLogger().logMessage(
				LogType.STREAM, 
				this.fixedrow, 
				this.fixedcol, 
				"Avanzado caracter: " + this.lexem
		);
		
		return this.chr;
	}
}
