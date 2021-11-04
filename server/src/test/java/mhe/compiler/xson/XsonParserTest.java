package mhe.compiler.xson;

import mhe.compiler.exception.CompilerException;
import mhe.compiler.mhe.MheLexer;
import mhe.compiler.mhe.MheLexicalCategory;
import mhe.compiler.model.Lexer;
import mhe.compiler.model.Stream;
import mhe.compiler.model.impl.AbstractStream;
import mhe.xson.XsonValue;
import mhe.xson.XsonValueType;
import mhe.xson.exception.WrongXsonTypeException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class XsonParserTest {

    @Test
    public void shouldParseBoolean() throws CompilerException, WrongXsonTypeException {
        String code = " true ";

        Stream stream = new AbstractStream(new StringReader(code));
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

        XsonParser xsonParser = new XsonParser(lexer);

        XsonValue result = xsonParser.compile();

        assertEquals(Boolean.TRUE, result.toBoolean());
    }

    @Test
    public void shouldParseInteger() throws CompilerException, WrongXsonTypeException {
        String code = " 8 ";

        Stream stream = new AbstractStream(new StringReader(code));
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

        XsonParser xsonParser = new XsonParser(lexer);

        XsonValue result = xsonParser.compile();

        assertEquals(Integer.valueOf(8), result.toInteger());
    }

    @Test
    public void shouldParseDouble() throws CompilerException, WrongXsonTypeException {
        String code = " 8.8 ";

        Stream stream = new AbstractStream(new StringReader(code));
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

        XsonParser xsonParser = new XsonParser(lexer);

        XsonValue result = xsonParser.compile();

        assertEquals(Double.valueOf(8.8), result.toDouble());
    }

    @Test
    public void shouldParseString() throws CompilerException, WrongXsonTypeException {
        String code = " \" una cadena con espacios \\n y saltos de línea \" ";

        Stream stream = new AbstractStream(new StringReader(code));
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

        XsonParser xsonParser = new XsonParser(lexer);

        XsonValue result = xsonParser.compile();

        assertEquals(" una cadena con espacios \n y saltos de línea ", result.toString(""));
    }

    @Test
    public void shouldParseEmptyArray() throws CompilerException, WrongXsonTypeException {
        String code = "[]";

        Stream stream = new AbstractStream(new StringReader(code));
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

        XsonParser xsonParser = new XsonParser(lexer);

        XsonValue result = xsonParser.compile();

        assertEquals(XsonValueType.ARRAY, result.getType());
    }

    @Test
    public void shouldParseArray() throws CompilerException, WrongXsonTypeException {
        String code = "[ true, 8, 8.8, \"cadena\", identificador, [], {} ]";

        Stream stream = new AbstractStream(new StringReader(code));
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

        XsonParser xsonParser = new XsonParser(lexer);

        XsonValue result = xsonParser.compile();

        assertEquals(XsonValueType.ARRAY, result.getType());
        assertEquals("[true,8,8.8,\"cadena\",\"identificador\",[],{}]", result.toJsonString());
    }

    @Test
    public void shouldParseEmptyObject() throws CompilerException, WrongXsonTypeException {
        String code = "{}";

        Stream stream = new AbstractStream(new StringReader(code));
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

        XsonParser xsonParser = new XsonParser(lexer);

        XsonValue result = xsonParser.compile();

        assertEquals(XsonValueType.OBJECT, result.getType());
    }

    @Test
    public void shouldParseObject() throws CompilerException, WrongXsonTypeException {
        String code = "{ hola:mundo, entero: 8 }";

        Stream stream = new AbstractStream(new StringReader(code));
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

        XsonParser xsonParser = new XsonParser(lexer);

        XsonValue result = xsonParser.compile();

        assertEquals(XsonValueType.OBJECT, result.getType());
        assertEquals("{\"hola\":\"mundo\",\"entero\":8}", result.toJsonString());
    }

    @Test(expected = CompilerException.class)
    public void shouldThrowCompilerExcepcion01() throws CompilerException, WrongXsonTypeException {
        String code = "}";

        Stream stream = new AbstractStream(new StringReader(code));
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

        XsonParser xsonParser = new XsonParser(lexer);

        xsonParser.compile();
    }

    @Test(expected = CompilerException.class)
    public void shouldThrowCompilerExcepcion02() throws CompilerException, WrongXsonTypeException {
        String code = "[}]";

        Stream stream = new AbstractStream(new StringReader(code));
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

        XsonParser xsonParser = new XsonParser(lexer);

        xsonParser.compile();
    }

    @Test(expected = CompilerException.class)
    public void shouldThrowCompilerExcepcion03() throws CompilerException, WrongXsonTypeException {
        String code = "[a}]";

        Stream stream = new AbstractStream(new StringReader(code));
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

        XsonParser xsonParser = new XsonParser(lexer);

        xsonParser.compile();
    }

    @Test(expected = CompilerException.class)
    public void shouldThrowCompilerExcepcion04() throws CompilerException, WrongXsonTypeException {
        String code = "{]}";

        Stream stream = new AbstractStream(new StringReader(code));
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

        XsonParser xsonParser = new XsonParser(lexer);

        xsonParser.compile();
    }

    @Test(expected = CompilerException.class)
    public void shouldThrowCompilerExcepcion05() throws CompilerException, WrongXsonTypeException {
        String code = "{ key: 1, key: 2 }";

        Stream stream = new AbstractStream(new StringReader(code));
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

        XsonParser xsonParser = new XsonParser(lexer);

        xsonParser.compile();
    }
}
