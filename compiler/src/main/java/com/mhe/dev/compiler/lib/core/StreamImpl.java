package com.mhe.dev.compiler.lib.core;

import java.io.IOException;
import java.io.Reader;

/**
 * Implementación de Stream con objetos de tipo 'io.Reader'.
 *
 * @author Manuel Hoyo Estévez
 */
public class StreamImpl implements Stream
{
    /**
     * Carácter de fin de stream.
     */
    public static final char STR_END = 65535;

    /**
     * Objeto logger.
     */
    private final MheLogger logger;

    /**
     * Objeto reader.
     */
    private final Reader reader;

    /**
     * Contador de columnas.
     */
    private int col = 1;

    /**
     * Contador de filas.
     */
    private int row = 1;

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

    public StreamImpl(MheLogger logger, Reader reader)
    {
        this.logger = logger;
        this.reader = reader;
    }

    @Override
    public Reader getReader()
    {
        return reader;
    }

    @Override
    public int getRowNumber()
    {
        return fixedRow;
    }

    @Override
    public int getColNumber()
    {
        return fixedCol;
    }

    @Override
    public char getCurrentCharacter()
    {
        return chr;
    }

    @Override
    public boolean isFinished()
    {
        return chr == STR_END;
    }

    @Override
    public String getLexeme()
    {
        return lexeme;
    }

    @Override
    public void resetLexeme()
    {
        logger.stream(fixedRow, fixedCol, "Lexema a resetear: " + lexeme);
        lexeme = "";
        fixedCol = col;
        fixedRow = row;
    }

    @Override
    public char getBackCharacter() throws CompilerIoException
    {
        try
        {
            getReader().reset();
            chr = mark;
            lexeme = lexeme.substring(0, lexeme.length() - 1);
            if (jump)
            {
                row--;
            } else
            {
                col--;
            }

            logger.stream(fixedRow, fixedCol, "Retrocedido carácter: " + lexeme);

            return chr;
        } catch (IOException ex)
        {
            throw new CompilerIoException(row, col, ex);
        }
    }

    @Override
    public char getNextCharacter() throws CompilerIoException
    {
        if (chr != STR_END)
        {
            try
            {
                getReader().mark(1);
                mark = chr;
                chr = (char) getReader().read();
                lexeme += (chr);
                if (chr == '\n')
                {
                    row++;
                    col = 1;
                    jump = true;
                } else
                {
                    col++;
                    jump = false;
                }
            } catch (IOException ex)
            {
                throw new CompilerIoException(row, col, ex);
            }
        }

        logger.stream(fixedRow, fixedCol, "Avanzado carácter: " + lexeme);

        return chr;
    }
}
