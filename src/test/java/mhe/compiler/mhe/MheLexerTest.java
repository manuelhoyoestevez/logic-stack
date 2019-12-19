package mhe.compiler.mhe;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import mhe.compiler.exception.CompilerIOException;
import mhe.compiler.logger.DefaultLogger;
import mhe.compiler.model.Lexer;
import mhe.compiler.model.Stream;
import mhe.compiler.model.Token;
import mhe.compiler.model.impl.AbstractStream;

@RunWith(JUnit4.class)
public class MheLexerTest {

    @Test
    public void shouldCompileTokens01() throws CompilerIOException {
        String code = " null true false 87.21 token id , : /*  wed  **/ 'i' \"hol\" 5  sdg -> < ! ?";

        Stream stream = new AbstractStream(new StringReader(code), new DefaultLogger());
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);
        Token<MheLexicalCategory> t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.IDENTIFIER, t.getCategory());
        assertEquals("null", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.BOOLEAN, t.getCategory());
        assertEquals("true", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.BOOLEAN, t.getCategory());
        assertEquals("false", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.DECIMAL, t.getCategory());
        assertEquals("87.21", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.TOKEN, t.getCategory());
        assertEquals("token", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.IDENTIFIER, t.getCategory());
        assertEquals("id", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.COLON, t.getCategory());
        assertEquals(",", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.TWOPOINT, t.getCategory());
        assertEquals(":", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.CHARACTER, t.getCategory());
        assertEquals("'i'", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.STRING, t.getCategory());
        assertEquals("\"hol\"", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.INTEGER, t.getCategory());
        assertEquals("5", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.IDENTIFIER, t.getCategory());
        assertEquals("sdg", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.IMPLRIGHT, t.getCategory());
        assertEquals("->", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.SMALLER, t.getCategory());
        assertEquals("<", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.NOT, t.getCategory());
        assertEquals("!", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.HOOK, t.getCategory());
        assertEquals("?", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.END, t.getCategory());
    }

    @Test
    public void shouldCompileTokens02() throws CompilerIOException {
        String code = ".,;()[]{}% + += ++ - -= --";

        Stream stream = new AbstractStream(new StringReader(code), new DefaultLogger());
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);
        Token<MheLexicalCategory> t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.POINT, t.getCategory());
        assertEquals(".", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.COLON, t.getCategory());
        assertEquals(",", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.SEMICOLON, t.getCategory());
        assertEquals(";", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.LPAREN, t.getCategory());
        assertEquals("(", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.RPAREN, t.getCategory());
        assertEquals(")", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.LCORCH, t.getCategory());
        assertEquals("[", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.RCORCH, t.getCategory());
        assertEquals("]", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.LKEY, t.getCategory());
        assertEquals("{", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.RKEY, t.getCategory());
        assertEquals("}", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.PERCENT, t.getCategory());
        assertEquals("%", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.PLUS, t.getCategory());
        assertEquals("+", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.PLUSEQ, t.getCategory());
        assertEquals("+=", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.INC, t.getCategory());
        assertEquals("++", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.MINUS, t.getCategory());
        assertEquals("-", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.MINUSEQ, t.getCategory());
        assertEquals("-=", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.DEC, t.getCategory());
        assertEquals("--", t.getLexeme());
    }

    @Test
    public void shouldCompileTokens03() throws CompilerIOException {
        String code = ".// a b \n,";

        Stream stream = new AbstractStream(new StringReader(code), new DefaultLogger());
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);
        Token<MheLexicalCategory> t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.POINT, t.getCategory());
        assertEquals(".", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.COLON, t.getCategory());
        assertEquals(",", t.getLexeme());

        t = lexer.getNextToken();

        assertEquals(MheLexicalCategory.END, t.getCategory());
    }
}
