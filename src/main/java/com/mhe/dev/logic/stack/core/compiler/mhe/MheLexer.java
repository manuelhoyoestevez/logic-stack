package com.mhe.dev.logic.stack.core.compiler.mhe;

import com.mhe.dev.logic.stack.core.compiler.exception.CompilerIoException;
import com.mhe.dev.logic.stack.core.compiler.model.Stream;
import com.mhe.dev.logic.stack.core.compiler.model.impl.AbstractLexer;

/**
 * Analizador léxico.
 *
 * @author Manuel Hoyo Estévez
 */
public class MheLexer extends AbstractLexer<MheLexicalCategory>
{

    public MheLexer(Stream stream)
    {
        super(stream);
    }

    @Override
    public MheLexicalCategory getSkipCategory()
    {
        return MheLexicalCategory.SKIP;
    }

    @Override
    protected MheLexicalCategory compileToken() throws CompilerIoException
    {
        char c = getStream().getNextCharacter();

        if (isLetter(c))
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
                return compileStar();
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
                return MheLexicalCategory.ERROR;
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

    protected MheLexicalCategory compileStar() throws CompilerIoException
    {
        switch (getStream().getNextCharacter())
        {
            case '=':
                return MheLexicalCategory.STAREQ;
            case '*':
                return MheLexicalCategory.POW;
            default:
                getStream().getBackCharacter();
                return MheLexicalCategory.STAR;
        }
    }

    protected MheLexicalCategory compileDiv() throws CompilerIoException
    {
        switch (getStream().getNextCharacter())
        {
            case '=':
                return MheLexicalCategory.DIVEQ;
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
                return MheLexicalCategory.IMPLDOUBLE;
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
        switch (getStream().getNextCharacter())
        {
            case '|':
                return MheLexicalCategory.ORLOG;
            case '=':
                return MheLexicalCategory.BAREQ;
            default:
                getStream().getBackCharacter();
                return MheLexicalCategory.BAR;
        }
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

    protected MheLexicalCategory compileCharacter() throws CompilerIoException
    {
        switch (getStream().getNextCharacter())
        {
            case '\\':
                return compileCharA();
            case '\n':
            case '\r':
            case '\'':
            case '\"':
                return MheLexicalCategory.ERROR;
            default:
                return compileCharD();
        }
    }

    protected MheLexicalCategory compileCharA() throws CompilerIoException
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
                return MheLexicalCategory.ERROR;

        }
    }

    protected MheLexicalCategory compileCharB() throws CompilerIoException
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
                return MheLexicalCategory.ERROR;
        }
    }

    protected MheLexicalCategory compileCharC() throws CompilerIoException
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
                return MheLexicalCategory.ERROR;
        }
    }

    protected MheLexicalCategory compileCharD() throws CompilerIoException
    {
        if (getStream().getNextCharacter() == '\'')
        {
            return MheLexicalCategory.CHARACTER;
        }
        return MheLexicalCategory.ERROR;
    }

    protected MheLexicalCategory compileWord() throws CompilerIoException
    {
        char c;
        do
        {
            c = getStream().getNextCharacter();
        } while (isLetter(c) || isNumber(c));
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
        if (s == null)
        {
            return MheLexicalCategory.ERROR;
        }
        if (s.compareTo("token") == 0)
        {
            return MheLexicalCategory.TOKEN;
        }
        if (s.compareTo("exit") == 0)
        {
            return MheLexicalCategory.EXIT;
        }
        if (s.compareTo("show") == 0)
        {
            return MheLexicalCategory.SHOW;
        }
        if (s.compareTo("load") == 0)
        {
            return MheLexicalCategory.LOAD;
        }
        if (s.compareTo("save") == 0)
        {
            return MheLexicalCategory.SAVE;
        }
        if (s.compareTo("list") == 0)
        {
            return MheLexicalCategory.LIST;
        }
        if (s.compareTo("test") == 0)
        {
            return MheLexicalCategory.TEST;
        }
        if (s.compareTo("return") == 0)
        {
            return MheLexicalCategory.RETURN;
        }
        if (s.compareTo("true") == 0)
        {
            return MheLexicalCategory.BOOLEAN;
        }
        if (s.compareTo("false") == 0)
        {
            return MheLexicalCategory.BOOLEAN;
        }
        return MheLexicalCategory.IDENTIFIER;
    }

    protected MheLexicalCategory compileString() throws CompilerIoException
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
        return (c > 0 && !getStream().isFinished()) ? MheLexicalCategory.STRING : MheLexicalCategory.ERROR;
    }

    protected MheLexicalCategory compileUniComm() throws CompilerIoException
    {
        char c;
        do
        {
            c = getStream().getNextCharacter();
        } while (c != '\n' && c > 0 && !getStream().isFinished());
        return (c > 0 && !getStream().isFinished()) ? MheLexicalCategory.SKIP : MheLexicalCategory.ERROR;
    }

    protected MheLexicalCategory compileMultiCommA() throws CompilerIoException
    {
        char c;
        do
        {
            c = getStream().getNextCharacter();
        } while (c != '*' && c > 0 && !getStream().isFinished());

        if (getStream().isFinished())
        {
            return MheLexicalCategory.ERROR;
        }

        if (c == '*')
        {
            return compileMultiCommB();
        }

        return MheLexicalCategory.ERROR;
    }

    protected MheLexicalCategory compileMultiCommB() throws CompilerIoException
    {
        char c;
        do
        {
            c = getStream().getNextCharacter();
        } while (c == '*' && !getStream().isFinished());

        if (getStream().isFinished())
        {
            return MheLexicalCategory.ERROR;
        }

        if (c == '/')
        {
            return MheLexicalCategory.SKIP;
        }

        return c > 0 ? compileMultiCommA() : MheLexicalCategory.ERROR;
    }
}
