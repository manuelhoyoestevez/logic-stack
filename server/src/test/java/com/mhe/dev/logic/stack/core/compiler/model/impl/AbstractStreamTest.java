package com.mhe.dev.logic.stack.core.compiler.model.impl;

import com.mhe.dev.logic.stack.core.compiler.exception.CompilerIoException;
import com.mhe.dev.logic.stack.core.compiler.model.Stream;

import java.io.IOException;
import java.io.StringReader;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AbstractStreamTest {

    @Test
    void shouldReadCharacters() throws CompilerIoException {
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

    @Test
    void shouldThrowCompilerIOException01() throws IOException
    {
        try {
            String code = ",\n:";
            Stream stream = new AbstractStream(new StringReader(code));

            char c = stream.getNextCharacter();
            assertEquals(',', c);

            stream.getReader().close();

            stream.getNextCharacter();
            fail("CompilerIoException must be thrown");
        } catch (CompilerIoException e)
        {
            assertEquals("Stream closed", e.getMessage());
        }

    }

    @Test
    void shouldThrowCompilerIOException02() throws IOException {
        try
        {
            String code = ",\n:";
            Stream stream = new AbstractStream(new StringReader(code));

            char c = stream.getNextCharacter();
            assertEquals(',', c);

            stream.getReader().close();

            stream.getBackCharacter();
            fail("CompilerIoException must be thrown");
        } catch (CompilerIoException e)
        {
            assertEquals("Stream closed", e.getMessage());
        }
    }

    @Test
    void shouldReadCharactersWithBreakLine() throws CompilerIoException {
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
