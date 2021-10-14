package mhe.compiler.model.impl;

import java.io.IOException;
import java.io.Reader;

import mhe.compiler.exception.CompilerIOException;
import mhe.compiler.logger.MheLogger;
import mhe.compiler.logger.MheLoggerFactory;
import mhe.compiler.model.Stream;

/**
 * Implementación de Stream con objetos de tipo 'io.Reader'
 * 
 * @author Manuel Hoyo Estévez
 */
public class AbstractStream implements Stream {
    private static final MheLogger logger = MheLoggerFactory.getLogger(AbstractStream.class);

    /** */
    public final static char STR_END = 65535;

    /** Contador de columnas */
    private int col = 0;

    /** Contador de filas */
    private int row = 0;

    /** Indicador de columna */
    private int fixedCol = 0;

    /** Indicador de fila */
    private int fixedRow = 0;

    /** Carácter actual */
    private char chr = 0;

    /** Carácter de marca */
    private char mark = 0;

    /** Indicador de salto */
    private boolean jump = false;

    /** Lexema del componente léxico */
    private String lexeme = "";

    /** */
    private final Reader reader;

    public AbstractStream(Reader reader) {
        this.reader = reader;
    }

    @Override
    public Reader getReader() {
        return this.reader;
    }

    @Override
    public int getRowNumber() {
        return this.fixedRow;
    }

    @Override
    public int getColNumber() {
        return this.fixedCol;
    }

    @Override
    public char getCurrentCharacter() {
        return this.chr;
    }

    @Override
    public boolean isFinished() {
        return this.chr == STR_END;
    }

    @Override
    public String getLexeme() {
        return this.lexeme;
    }

    @Override
    public void resetLexeme() {
        logger.stream(this.fixedRow, this.fixedCol, "Lexema a resetear: " + this.lexeme);
        this.lexeme = "";
        this.fixedCol = this.col;
        this.fixedRow = this.row;
    }

    @Override
    public char getBackCharacter() throws CompilerIOException {
        try {
            this.getReader().reset();
            this.chr = this.mark;
            this.lexeme = this.lexeme.substring(0, this.lexeme.length() - 1);
            if (this.jump) {
                this.col--;
            } else {
                this.row--;
            }

            logger.stream(this.fixedRow, this.fixedCol,"Retrocedido carácter: " + this.lexeme);

            return this.chr;
        } catch (IOException ex) {
            throw new CompilerIOException(this.row, this.col, ex);
        }
    }

    @Override
    public char getNextCharacter() throws CompilerIOException {
        if (this.chr != STR_END) {
            try {
                this.getReader().mark(1);
                this.mark = this.chr;
                this.chr = (char) this.getReader().read();
                this.lexeme += (this.chr);
                if (this.chr == '\n') {
                    this.col++;
                    this.row = 1;
                    this.jump = true;
                } else {
                    this.row++;
                    this.jump = false;
                }
            } catch (IOException ex) {
                throw new CompilerIOException(this.row, this.col, ex);
            }
        }

        logger.stream(this.fixedRow, this.fixedCol, "Avanzado carácter: " + this.lexeme);

        return this.chr;
    }
}
