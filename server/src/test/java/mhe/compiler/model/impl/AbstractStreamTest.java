package mhe.compiler.model.impl;

import mhe.compiler.exception.CompilerIoException;
import mhe.compiler.model.Stream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class AbstractStreamTest {

    @Test
    public void shouldReadCharacters() throws CompilerIoException {
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

    @Test(expected = CompilerIoException.class)
    public void shouldThrowCompilerIOExcepcion01() throws IOException, CompilerIoException {
        String code = ",\n:";
        Stream stream = new AbstractStream(new StringReader(code));

        char c = stream.getNextCharacter();
        assertEquals(',', c);

        stream.getReader().close();

        stream.getNextCharacter();
    }

    @Test(expected = CompilerIoException.class)
    public void shouldThrowCompilerIOExcepcion02() throws IOException, CompilerIoException {
        String code = ",\n:";
        Stream stream = new AbstractStream(new StringReader(code));

        char c = stream.getNextCharacter();
        assertEquals(',', c);

        stream.getReader().close();

        stream.getBackCharacter();
    }

    @Test
    public void shouldReadCharactersWithBreakLine() throws CompilerIoException {
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
