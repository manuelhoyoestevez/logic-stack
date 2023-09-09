package com.mhe.dev.compiler.lib.core;

/**
 * Analizador léxico.
 *
 * @author Manuel Hoyo Estévez
 */
public class LexerImpl extends LexerAbstract<MheLexicalCategory>
{
    public LexerImpl(MheLogger logger, Stream stream)
    {
        super(logger, stream);
    }

    @Override
    public MheLexicalCategory getSkipCategory()
    {
        return MheLexicalCategory.SKIP;
    }

    @Override
    protected MheLexicalCategory compileToken() throws CompilerException
    {
        char c = getStream().getNextCharacter();

        if (isLetter(c) || c == '_')
        {
            return compileWord();
        }
        if (isNumber(c))
        {
            return compileNumber();
        }
        if (getStream().isFinished())
        {
            return MheLexicalCategory.END;
        }

        switch (c)
        {
            case '\n':
            case '\t':
            case '\r':
            case ' ':
                return MheLexicalCategory.SKIP;
            case '.':
                return MheLexicalCategory.POINT;
            case ',':
                return MheLexicalCategory.COLON;
            case ':':
                return MheLexicalCategory.TWOPOINT;
            case ';':
                return MheLexicalCategory.SEMICOLON;
            case '(':
                return MheLexicalCategory.LPAREN;
            case ')':
                return MheLexicalCategory.RPAREN;
            case '[':
                return MheLexicalCategory.LCORCH;
            case ']':
                return MheLexicalCategory.RCORCH;
            case '{':
                return MheLexicalCategory.LKEY;
            case '}':
                return MheLexicalCategory.RKEY;
            case '%':
                return MheLexicalCategory.PERCENT;
            case '?':
                return MheLexicalCategory.HOOK;
            case '+':
                return compilePlus();
            case '-':
                return compileMinus();
            case '*':
                return MheLexicalCategory.STAR;
            case '@':
                return MheLexicalCategory.AT;
            case '$':
                return MheLexicalCategory.DOLLAR;
            case '/':
                return compileDiv();
            case '&':
                return compileAmpersand();
            case '|':
                return compileBar();
            case '!':
                return compileExclamation();
            case '=':
                return compileEqual();
            case '<':
                return compileSmaller();
            case '>':
                return compileBigger();
            case '\'':
                return compileCharacter();
            case '\"':
                return compileString();
            default:
                throw new CompilerException(
                        getStream().getRowNumber(),
                        getStream().getColNumber(),
                        "Caracter no soportando: '" + c + "'",
                        null
                );
        }
    }

    protected MheLexicalCategory compilePlus() throws CompilerIoException
    {
        switch (getStream().getNextCharacter())
        {
            case '=':
                return MheLexicalCategory.PLUSEQ;
            case '+':
                return MheLexicalCategory.INC;
            default:
                getStream().getBackCharacter();
                return MheLexicalCategory.PLUS;
        }
    }

    protected MheLexicalCategory compileMinus() throws CompilerIoException
    {
        switch (getStream().getNextCharacter())
        {
            case '=':
                return MheLexicalCategory.MINUSEQ;
            case '-':
                return MheLexicalCategory.DEC;
            case '>':
                return MheLexicalCategory.IMPLRIGHT;
            default:
                getStream().getBackCharacter();
                return MheLexicalCategory.MINUS;
        }
    }

    protected MheLexicalCategory compileDiv() throws CompilerException
    {
        switch (getStream().getNextCharacter())
        {
            case '*':
                return compileMultiCommA();
            case '/':
                return compileUniComm();
            default:
                getStream().getBackCharacter();
                return MheLexicalCategory.DIV;
        }
    }

    protected MheLexicalCategory compileBigger() throws CompilerIoException
    {
        switch (getStream().getNextCharacter())
        {
            case '=':
                return MheLexicalCategory.BIGGEREQ;
            case '>':
                return MheLexicalCategory.MOVERIGHT;
            default:
                getStream().getBackCharacter();
                return MheLexicalCategory.BIGGER;
        }
    }

    protected MheLexicalCategory compileSmaller() throws CompilerIoException
    {
        switch (getStream().getNextCharacter())
        {
            case '=':
                return MheLexicalCategory.SMALLEREQ;
            case '<':
                return MheLexicalCategory.MOVELEFT;
            case '>':
                return MheLexicalCategory.DISTINCT;
            default:
                getStream().getBackCharacter();
                return MheLexicalCategory.SMALLER;
        }
    }

    protected MheLexicalCategory compileEqual() throws CompilerIoException
    {
        if (getStream().getNextCharacter() == '=')
        {
            return MheLexicalCategory.EQUALEQ;
        }
        getStream().getBackCharacter();
        return MheLexicalCategory.EQUAL;
    }

    protected MheLexicalCategory compileExclamation() throws CompilerIoException
    {
        if (getStream().getNextCharacter() == '=')
        {
            return MheLexicalCategory.NOTEQUAL;
        }
        getStream().getBackCharacter();
        return MheLexicalCategory.NOT;
    }

    protected MheLexicalCategory compileBar() throws CompilerIoException
    {
        if (getStream().getNextCharacter() == '|')
        {
            return MheLexicalCategory.ORLOG;
        }
        getStream().getBackCharacter();
        return MheLexicalCategory.BAR;
    }

    protected MheLexicalCategory compileAmpersand() throws CompilerIoException
    {
        if (getStream().getNextCharacter() == '&')
        {
            return MheLexicalCategory.ANDLOG;
        }
        getStream().getBackCharacter();
        return MheLexicalCategory.AMPERSAND;
    }

    protected MheLexicalCategory compileCharacter() throws CompilerException
    {
        switch (getStream().getNextCharacter())
        {
            case '\\':
                return compileCharA();
            case '\n':
            case '\r':
            case '\'':
            case '\"':
                throw new CompilerException(
                        getStream().getRowNumber(),
                        getStream().getColNumber(),
                        "Definición de carácter no cerrada correctamente: " + getStream().getLexeme(),
                        null
                );
            default:
                return compileCharD();
        }
    }

    protected MheLexicalCategory compileCharA() throws CompilerException
    {
        switch (getStream().getNextCharacter())
        {
            case '0':
            case '1':
            case '2':
            case '3':
                return compileCharB();
            case '4':
            case '5':
            case '6':
            case '7':
                return compileCharC();
            case 'n':
            case 'r':
            case '\\':
            case '\'':
            case '\"':
                return compileCharD();
            default:
                throw new CompilerException(
                        getStream().getRowNumber(),
                        getStream().getColNumber(),
                        "Definición de carácter no cerrada correctamente: " + getStream().getLexeme(),
                        null
                );

        }
    }

    protected MheLexicalCategory compileCharB() throws CompilerException
    {
        switch (getStream().getNextCharacter())
        {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
                return compileCharC();
            case '\'':
                return MheLexicalCategory.CHARACTER;
            default:
                throw new CompilerException(
                        getStream().getRowNumber(),
                        getStream().getColNumber(),
                        "Definición de carácter no cerrada correctamente: " + getStream().getLexeme(),
                        null
                );
        }
    }

    protected MheLexicalCategory compileCharC() throws CompilerException
    {
        switch (getStream().getNextCharacter())
        {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
                return compileCharD();
            case '\'':
                return MheLexicalCategory.CHARACTER;
            default:
                throw new CompilerException(
                        getStream().getRowNumber(),
                        getStream().getColNumber(),
                        "Definición de carácter no cerrada correctamente: " + getStream().getLexeme(),
                        null
                );
        }
    }

    protected MheLexicalCategory compileCharD() throws CompilerException
    {
        if (getStream().getNextCharacter() == '\'')
        {
            return MheLexicalCategory.CHARACTER;
        }

        throw new CompilerException(
                getStream().getRowNumber(),
                getStream().getColNumber(),
                "Definición de carácter no cerrada correctamente: " + getStream().getLexeme(),
                null
        );
    }

    protected MheLexicalCategory compileWord() throws CompilerIoException
    {
        char c;
        do
        {
            c = getStream().getNextCharacter();
        } while (isLetter(c) || isNumber(c) || c == '_');
        getStream().getBackCharacter();
        return findReserved(getStream().getLexeme());
    }

    protected MheLexicalCategory compileNumber() throws CompilerIoException
    {
        char c;
        do
        {
            c = getStream().getNextCharacter();
        } while (isNumber(c));

        MheLexicalCategory r;

        if (c == '.')
        {
            do
            {
                c = getStream().getNextCharacter();
            } while (isNumber(c));
            r = MheLexicalCategory.DECIMAL;
        } else
        {
            r = MheLexicalCategory.INTEGER;
        }
        getStream().getBackCharacter();
        return r;
    }

    protected MheLexicalCategory findReserved(String s)
    {
        if (s.compareTo("true") == 0)
        {
            return MheLexicalCategory.BOOLEAN;
        }
        if (s.compareTo("false") == 0)
        {
            return MheLexicalCategory.BOOLEAN;
        }
        if (s.compareTo("return") == 0)
        {
            return MheLexicalCategory.RETURN;
        }
        return MheLexicalCategory.IDENTIFIER;
    }

    protected MheLexicalCategory compileString() throws CompilerException
    {
        char c;
        do
        {
            c = getStream().getNextCharacter();
            if (c == '\\')
            {
                getStream().getNextCharacter();
            }
        } while (c != '\"' && c > 0 && !getStream().isFinished());

        if (c > 0 && !getStream().isFinished())
        {
            return MheLexicalCategory.STRING;
        }

        throw new CompilerException(
                getStream().getRowNumber(),
                getStream().getColNumber(),
                "String no cerrado correctamente",
                null
        );
    }

    protected MheLexicalCategory compileUniComm() throws CompilerException
    {
        char c;
        do
        {
            c = getStream().getNextCharacter();
        } while (c != '\n' && c > 0 && !getStream().isFinished());

        if (c > 0 && !getStream().isFinished())
        {
            return MheLexicalCategory.SKIP;
        }

        throw new CompilerException(
                getStream().getRowNumber(),
                getStream().getColNumber(),
                "Comentario de un línea no cerrado correctamente",
                null
        );
    }

    protected MheLexicalCategory compileMultiCommA() throws CompilerException
    {
        char c;
        do
        {
            c = getStream().getNextCharacter();
        } while (c != '*' && c > 0 && !getStream().isFinished());

        if (getStream().isFinished())
        {
            throw new CompilerException(
                    getStream().getRowNumber(),
                    getStream().getColNumber(),
                    "Comentario multilinea no cerrado correctamente",
                    null
            );
        }

        if (c == '*')
        {
            return compileMultiCommB();
        }

        throw new CompilerException(
                getStream().getRowNumber(),
                getStream().getColNumber(),
                "Comentario multilinea no cerrado correctamente",
                null
        );
    }

    protected MheLexicalCategory compileMultiCommB() throws CompilerException
    {
        char c;
        do
        {
            c = getStream().getNextCharacter();
        } while (c == '*' && !getStream().isFinished());

        if (getStream().isFinished())
        {
            throw new CompilerException(
                    getStream().getRowNumber(),
                    getStream().getColNumber(),
                    "Comentario multilinea no cerrado correctamente",
                    null
            );
        }

        if (c == '/')
        {
            return MheLexicalCategory.SKIP;
        }

        if (c > 0)
        {
            return compileMultiCommA();
        }

        throw new CompilerException(
                getStream().getRowNumber(),
                getStream().getColNumber(),
                "Comentario multilinea no cerrado correctamente",
                null
        );
    }
}
