package mhe.compiler.mhe;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import mhe.compiler.exception.CompilerIOException;
import mhe.compiler.model.Lexer;
import mhe.compiler.model.Stream;
import mhe.compiler.model.Token;
import mhe.compiler.model.impl.AbstractStream;

@RunWith(JUnit4.class)
public class MheLexerTest {
	
	class D {
		String lexeme;
		MheLexicalCategory category;
		public D(String lexeme, MheLexicalCategory category) {
			this.lexeme = lexeme;
			this.category = category;
		}
	}
	
	private D D(String lexeme, MheLexicalCategory category) {
		return new D(lexeme, category);
    }
	
	private void testTokens(Lexer<MheLexicalCategory> lexer, List<D> tokens) throws CompilerIOException {
    	for (D d : tokens) {
    		Token<MheLexicalCategory> t = lexer.getNextToken();
    		assertEquals(d.category, t.getCategory());
            assertEquals(d.lexeme, t.getLexeme());
    	}
    }
	
	private void testFinished(Lexer<MheLexicalCategory> lexer) throws CompilerIOException {
    	Token<MheLexicalCategory> t = lexer.getNextToken();
    	assertEquals(MheLexicalCategory.END, t.getCategory());
    }

    @Test
    public void shouldCompileTokens01() throws CompilerIOException {
        String code = " null true false 87.21 token id , : /*  wed  **/ 'i' \"hol\" 5  sdg -> < ! ?";

        Stream stream = new AbstractStream(new StringReader(code));
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

        Stream stream = new AbstractStream(new StringReader(code));
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
        String code = ".// a b \n,* ** *= / /= > >= >> < <= << <> = == ! != & && | || |= a87 ñ";

        Stream stream = new AbstractStream(new StringReader(code));
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);
        
        D[] d = {
        		D(".", MheLexicalCategory.POINT),
        		D(",", MheLexicalCategory.COLON),
        		D("*", MheLexicalCategory.STAR),
        		D("**", MheLexicalCategory.POW),
        		D("*=", MheLexicalCategory.STAREQ),
        		D("/", MheLexicalCategory.DIV),
        		D("/=", MheLexicalCategory.DIVEQ),
        		D(">", MheLexicalCategory.BIGGER),
        		D(">=", MheLexicalCategory.BIGGEREQ),
        		D(">>", MheLexicalCategory.MOVERIGHT),
        		D("<", MheLexicalCategory.SMALLER),
        		D("<=", MheLexicalCategory.SMALLEREQ),
        		D("<<", MheLexicalCategory.MOVELEFT),
        		D("<>", MheLexicalCategory.IMPLDOUBLE),
        		D("=", MheLexicalCategory.EQUAL),
        		D("==", MheLexicalCategory.EQUALEQ),
        		D("!", MheLexicalCategory.NOT),
        		D("!=", MheLexicalCategory.NOTEQUAL),
        		D("&", MheLexicalCategory.AMPERSAND),
        		D("&&", MheLexicalCategory.ANDLOG),
        		D("|", MheLexicalCategory.BAR),
        		D("||", MheLexicalCategory.ORLOG),
        		D("|=", MheLexicalCategory.BAREQ),
        		D("a87", MheLexicalCategory.IDENTIFIER),
        		D("ñ", MheLexicalCategory.ERROR)
        };

        testTokens(lexer, Arrays.asList(d));
        testFinished(lexer);
    }
}
