package com.mhe.dev.logic.stack.core.compiler.xson;

import com.mhe.dev.logic.stack.core.compiler.exception.CompilerException;
import com.mhe.dev.logic.stack.core.compiler.mhe.MheLexer;
import com.mhe.dev.logic.stack.core.compiler.mhe.MheLexicalCategory;
import com.mhe.dev.logic.stack.core.compiler.model.Lexer;
import com.mhe.dev.logic.stack.core.compiler.model.Stream;
import com.mhe.dev.logic.stack.core.compiler.model.impl.AbstractStream;
import com.mhe.dev.logic.stack.core.xson.XsonValue;
import com.mhe.dev.logic.stack.core.xson.XsonValueType;
import com.mhe.dev.logic.stack.core.xson.exception.WrongXsonTypeException;
import java.io.StringReader;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class XsonParserTest {

    @Test
    void shouldParseBoolean() throws CompilerException, WrongXsonTypeException {
        String code = " true ";

        Stream stream = new AbstractStream(new StringReader(code));
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

        XsonParser xsonParser = new XsonParser(lexer);

        XsonValue result = xsonParser.compile();

        assertEquals(Boolean.TRUE, result.toBoolean());
    }

    @Test
    void shouldParseInteger() throws CompilerException, WrongXsonTypeException {
        String code = " 8 ";

        Stream stream = new AbstractStream(new StringReader(code));
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

        XsonParser xsonParser = new XsonParser(lexer);

        XsonValue result = xsonParser.compile();

        assertEquals(Integer.valueOf(8), result.toInteger());
    }

    @Test
    void shouldParseDouble() throws CompilerException, WrongXsonTypeException {
        String code = " 8.8 ";

        Stream stream = new AbstractStream(new StringReader(code));
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

        XsonParser xsonParser = new XsonParser(lexer);

        XsonValue result = xsonParser.compile();

        assertEquals(Double.valueOf(8.8), result.toDouble());
    }

    @Test
    void shouldParseString() throws CompilerException, WrongXsonTypeException {
        String code = " \" una cadena con espacios \\n y saltos de línea \" ";

        Stream stream = new AbstractStream(new StringReader(code));
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

        XsonParser xsonParser = new XsonParser(lexer);

        XsonValue result = xsonParser.compile();

        assertEquals(" una cadena con espacios \n y saltos de línea ", result.toString(""));
    }

    @Test
    void shouldParseEmptyArray() throws CompilerException {
        String code = "[]";

        Stream stream = new AbstractStream(new StringReader(code));
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

        XsonParser xsonParser = new XsonParser(lexer);

        XsonValue result = xsonParser.compile();

        assertEquals(XsonValueType.ARRAY, result.getType());
    }

    @Test
    void shouldParseArray() throws CompilerException {
        String code = "[ true, 8, 8.8, \"cadena\", identificador, [], {} ]";

        Stream stream = new AbstractStream(new StringReader(code));
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

        XsonParser xsonParser = new XsonParser(lexer);

        XsonValue result = xsonParser.compile();

        assertEquals(XsonValueType.ARRAY, result.getType());
        assertEquals("[true,8,8.8,\"cadena\",\"identificador\",[],{}]", result.toJsonString());
    }

    @Test
    void shouldParseEmptyObject() throws CompilerException {
        String code = "{}";

        Stream stream = new AbstractStream(new StringReader(code));
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

        XsonParser xsonParser = new XsonParser(lexer);

        XsonValue result = xsonParser.compile();

        assertEquals(XsonValueType.OBJECT, result.getType());
    }

    @Test
    void shouldParseObject() throws CompilerException {
        String code = "{ hola:mundo, entero: 8 }";

        Stream stream = new AbstractStream(new StringReader(code));
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

        XsonParser xsonParser = new XsonParser(lexer);

        XsonValue result = xsonParser.compile();

        assertEquals(XsonValueType.OBJECT, result.getType());
        assertEquals("{\"hola\":\"mundo\",\"entero\":8}", result.toJsonString());
    }

    @Test
    void shouldThrowCompilerException01() {
        try
        {
            String code = "}";

            Stream stream = new AbstractStream(new StringReader(code));
            Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

            XsonParser xsonParser = new XsonParser(lexer);

            xsonParser.compile();
            fail("CompilerException must be thrown");
        } catch (CompilerException compilerException)
        {
            assertEquals("Expected boolean, integer, decimal, string, array or object. Found: { RKEY: '}' }[0, 0]", compilerException.getMessage());
        }
    }

    @Test
    void shouldThrowCompilerException02() {
        try {
            String code = "[}]";

            Stream stream = new AbstractStream(new StringReader(code));
            Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

            XsonParser xsonParser = new XsonParser(lexer);

            xsonParser.compile();
            fail("CompilerException must be thrown");
        } catch (CompilerException compilerException)
        {
            assertEquals("Expected boolean, integer, decimal, string, array, object or ']'. Found: { RKEY: '}' }[1, 0]", compilerException.getMessage());
        }
    }

    @Test
    void shouldThrowCompilerException03() {
        try {
            String code = "[a}]";

            Stream stream = new AbstractStream(new StringReader(code));
            Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

            XsonParser xsonParser = new XsonParser(lexer);

            xsonParser.compile();
            fail("CompilerException must be thrown");
        } catch (CompilerException compilerException)
        {
            assertEquals("Expected ',' or ']'. Found: { RKEY: '}' }[2, 0]", compilerException.getMessage());
        }

    }

    @Test
    void shouldThrowCompilerException04() {
        try {
            String code = "{]}";

            Stream stream = new AbstractStream(new StringReader(code));
            Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

            XsonParser xsonParser = new XsonParser(lexer);

            xsonParser.compile();
            fail("CompilerException must be thrown");
        } catch (CompilerException compilerException)
        {
            assertEquals("Expected key to parse or '}'. Found: { RCORCH: ']' }[1, 0]", compilerException.getMessage());
        }
    }

    @Test
    void shouldThrowCompilerException05() {
        try {
            String code = "{ key: 1, key: 2 }";

            Stream stream = new AbstractStream(new StringReader(code));
            Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

            XsonParser xsonParser = new XsonParser(lexer);

            xsonParser.compile();
            fail("CompilerException must be thrown");
        } catch (CompilerException compilerException)
        {
            assertEquals("Duplicated key: key", compilerException.getMessage());
        }
    }
}
