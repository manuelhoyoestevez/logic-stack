package mhe.compiler.model.impl;

import java.io.IOException;
import java.io.Reader;

import mhe.compiler.exception.CompilerIOException;
import mhe.compiler.logger.LogType;
import mhe.compiler.logger.Logger;
import mhe.compiler.model.Loggable;
import mhe.compiler.model.Stream;

/**
 * Implementación de Stream con objetos de tipo 'io.Reader'
 * 
 * @author Manuel Hoyo Estévez
 */
public class AbstractStream implements Stream {
    /** */
    public final static char STREND = 65535;

    /** Contador de columnas */
    private int col = 0;

    /** Contador de filas */
    private int row = 0;

    /** Inidicador de columna */
    private int fixedcol = 0;

    /** Inidicador de fila */
    private int fixedrow = 0;

    /** Carácter actual */
    private char chr = 0;

    /** Carácter de marca */
    private char mark = 0;

    /** Indicador de salto */
    private boolean jump = false;

    /** Lexema del componente léxico */
    private String lexem = "";

    /** */
    private Logger logger = null;

    /** */
    private Reader reader = null;

    public AbstractStream(Reader reader, Logger logger) {
        this.setReader(reader);
        this.setLogger(logger);
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
    public boolean isFinished() {
        return this.chr == STREND;
    }

    @Override
    public String getLexeme() {
        return this.lexem.toString();
    }

    @Override
    public void resetLexeme() {
        this.getLogger().logMessage(LogType.STREAM, this.fixedrow, this.fixedcol, "Lexema a resetar: " + this.lexem);
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
            if (this.jump) {
                this.col--;
            } else {
                this.row--;
            }

            this.getLogger().logMessage(LogType.STREAM, this.fixedrow, this.fixedcol,
                    "Retrocedido caracter: " + this.lexem);

            return this.chr;
        } catch (IOException ex) {
            throw new CompilerIOException(LogType.STREAM, this.row, this.col, ex);
        }
    }

    @Override
    public char getNextCharacter() throws CompilerIOException {
        if (this.chr != STREND) {
            try {
                this.getReader().mark(1);
                this.mark = this.chr;
                this.chr = (char) this.getReader().read();
                this.lexem += (this.chr);
                if (this.chr == '\n') {
                    this.col++;
                    this.row = 1;
                    this.jump = true;
                } else {
                    this.row++;
                    this.jump = false;
                }
            } catch (IOException ex) {
                throw new CompilerIOException(LogType.STREAM, this.row, this.col, ex);
            }
        }

        this.getLogger().logMessage(LogType.STREAM, this.fixedrow, this.fixedcol, "Avanzado caracter: " + this.lexem);

        return this.chr;
    }
}
