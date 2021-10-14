package mhe.compiler.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import mhe.compiler.exception.CompilerIOException;
import mhe.compiler.model.Stream;

@RunWith(JUnit4.class)
public class AbstractStreamTest {

    @Test
    public void shouldReadCharacters() throws CompilerIOException {
        String code = ",ab\n:";
        Stream stream = new AbstractStream(new StringReader(code));
        char c;

        c = stream.getNextCharacter();
        assertEquals(',', c);

        assertEquals(0, stream.getRowNumber());
        assertEquals(0, stream.getColNumber());

        c = stream.getNextCharacter();
        assertEquals('a', c);

        c = stream.getNextCharacter();
        assertEquals('b', c);

        assertEquals(0, stream.getRowNumber());
        assertEquals(0, stream.getColNumber());

        assertEquals(",ab", stream.getLexeme());

        stream.resetLexeme();

        assertEquals(3, stream.getRowNumber());
        assertEquals(0, stream.getColNumber());

        assertEquals("", stream.getLexeme());

        c = stream.getNextCharacter();
        assertEquals('\n', c);

        c = stream.getNextCharacter();
        assertEquals(':', c);

        c = stream.getBackCharacter();
        assertEquals('\n', c);

        c = stream.getNextCharacter();
        assertEquals(':', c);

        assertFalse(stream.isFinished());

        c = stream.getCurrentCharacter();
        assertEquals(':', c);

        c = stream.getNextCharacter();
        assertEquals(AbstractStream.STR_END, c);

        c = stream.getNextCharacter();
        assertEquals(AbstractStream.STR_END, c);

        assertTrue(stream.isFinished());
    }

    @Test(expected = CompilerIOException.class)
    public void shouldThrowCompilerIOExcepcion01() throws IOException, CompilerIOException {
        String code = ",\n:";
        Stream stream = new AbstractStream(new StringReader(code));

        char c = stream.getNextCharacter();
        assertEquals(',', c);

        stream.getReader().close();

        stream.getNextCharacter();
    }

    @Test(expected = CompilerIOException.class)
    public void shouldThrowCompilerIOExcepcion02() throws IOException, CompilerIOException {
        String code = ",\n:";
        Stream stream = new AbstractStream(new StringReader(code));

        char c = stream.getNextCharacter();
        assertEquals(',', c);

        stream.getReader().close();

        stream.getBackCharacter();
    }

    @Test
    public void shouldReadCharactersWithBreakLine() throws CompilerIOException {
        String code = ",\n:";
        Stream stream = new AbstractStream(new StringReader(code));
        char c;

        c = stream.getNextCharacter();
        assertEquals(',', c);

        c = stream.getNextCharacter();
        assertEquals('\n', c);

        c = stream.getBackCharacter();
        assertEquals(',', c);

        c = stream.getNextCharacter();
        assertEquals('\n', c);

        c = stream.getNextCharacter();
        assertEquals(':', c);

        c = stream.getNextCharacter();
        assertEquals(AbstractStream.STR_END, c);

        assertTrue(stream.isFinished());
    }
}
