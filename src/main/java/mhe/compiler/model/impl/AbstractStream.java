package mhe.compiler.model.impl;

import java.io.IOException;
import java.io.Reader;

import mhe.compiler.exception.CompilerException;
import mhe.compiler.exception.CompilerIOException;
import mhe.compiler.logger.LogType;
import mhe.compiler.logger.Logger;
import mhe.compiler.model.Loggable;
import mhe.compiler.model.Stream;

/** Implementación de Stream con objetos de tipo 'io.Reader'
 * @author  Manuel Hoyo Estévez
 */
public class AbstractStream implements Stream {
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
    private Logger logger = null;

    /** */
    private Reader reader = null;

    public AbstractStream(Reader reader, Logger logger) {
        this.setReader(reader);
        this.setLogger(logger);
    }

    @Override
    public char getErrorCategory() {
        return this.chrError;
    }

    public Stream setErrorCategory(char chrError) {
        this.chrError = chrError;
        return this;
    }

    @Override
    public char getEndCategory() {
        return this.strEnd;
    }

    public Stream setEndCategory(char strEnd) {
        this.strEnd = strEnd;
        return this;
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }

    public Loggable setLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    @Override
    public Reader getReader() {
        return this.reader;
    }

    public Stream setReader(Reader reader) {
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
    public char matchCharacter(char c) throws CompilerException {
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
    }

    @Override
    public char getBackCharacter() throws CompilerIOException {
        try {
            this.getReader().reset();
            this.chr = this.mark;
            this.lexem = this.lexem.substring(0, this.lexem.length() - 1);
            if(this.jump) {
                this.col--;
            }
            else {
                this.row--;
            }

            this.getLogger().logMessage(
                    LogType.STREAM,
                    this.fixedrow,
                    this.fixedcol,
                    "Retrocedido caracter: " + this.lexem
            );

            return this.chr;
        } catch (IOException ex) {
            throw new CompilerIOException(
                    LogType.STREAM,
                    this.row,
                    this.col,
                    ex
            );
        }
    }

    @Override
    public char getNextCharacter() throws CompilerIOException {
        if(this.chr != this.getEndCategory()){
            try {
                this.getReader().mark(1);
                this.mark = this.chr;
                this.chr = (char)this.getReader().read();
                this.lexem += (this.chr);
                if(this.chr == '\n'){
                    this.col++;
                    this.row = 1;
                    this.jump = true;
                }
                else{
                    this.row++;
                    this.jump = false;
                }
            } catch (IOException ex) {
                throw new CompilerIOException(
                        LogType.STREAM,
                        this.row,
                        this.col,
                        ex
                );
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
