package com.mhe.dev.logic.stack.core.compiler.model.impl;

import java.io.IOException;
import java.io.Reader;
import com.mhe.dev.logic.stack.core.compiler.exception.CompilerIoException;
import com.mhe.dev.logic.stack.core.compiler.logger.MheLogger;
import com.mhe.dev.logic.stack.core.compiler.logger.MheLoggerFactory;
import com.mhe.dev.logic.stack.core.compiler.model.Stream;

/**
 * Implementación de Stream con objetos de tipo 'io.Reader'.
 *
 * @author Manuel Hoyo Estévez
 */
public class AbstractStream implements Stream {

    /**
     * Carácter de fin de stream.
     */
    public static final char STR_END = 65535;

    /**
     * Objeto logger.
     */
    private static final MheLogger logger = MheLoggerFactory.getLogger(AbstractStream.class);

    /**
     * Objeto reader.
     */
    private final Reader reader;

    /**
     * Contador de columnas.
     */
    private int col = 0;

    /**
     * Contador de filas.
     */
    private int row = 0;

    /**
     * Indicador de columna.
     */
    private int fixedCol = 0;

    /**
     * Indicador de fila.
     */
    private int fixedRow = 0;

    /**
     * Carácter actual.
     */
    private char chr = 0;

    /**
     * Carácter de marca.
     */
    private char mark = 0;

    /**
     * Indicador de salto.
     */
    private boolean jump = false;

    /**
     * Lexema del componente léxico.
     */
    private String lexeme = "";

    public AbstractStream(Reader reader) {
        this.reader = reader;
    }

    @Override
    public Reader getReader() {
        return reader;
    }

    @Override
    public int getRowNumber() {
        return fixedRow;
    }

    @Override
    public int getColNumber() {
        return fixedCol;
    }

    @Override
    public char getCurrentCharacter() {
        return chr;
    }

    @Override
    public boolean isFinished() {
        return chr == STR_END;
    }

    @Override
    public String getLexeme() {
        return lexeme;
    }

    @Override
    public void resetLexeme() {
        logger.stream(fixedRow, fixedCol, "Lexema a resetear: " + lexeme);
        lexeme = "";
        fixedCol = col;
        fixedRow = row;
    }

    @Override
    public char getBackCharacter() throws CompilerIoException {
        try {
            getReader().reset();
            chr = mark;
            lexeme = lexeme.substring(0, lexeme.length() - 1);
            if (jump) {
                col--;
            } else {
                row--;
            }

            logger.stream(fixedRow, fixedCol, "Retrocedido carácter: " + lexeme);

            return chr;
        } catch (IOException ex) {
            throw new CompilerIoException(row, col, ex);
        }
    }

    @Override
    public char getNextCharacter() throws CompilerIoException {
        if (chr != STR_END) {
            try {
                getReader().mark(1);
                mark = chr;
                chr = (char) getReader().read();
                lexeme += (chr);
                if (chr == '\n') {
                    col++;
                    row = 1;
                    jump = true;
                } else {
                    row++;
                    jump = false;
                }
            } catch (IOException ex) {
                throw new CompilerIoException(row, col, ex);
            }
        }

        logger.stream(fixedRow, fixedCol, "Avanzado carácter: " + lexeme);

        return chr;
    }
}
