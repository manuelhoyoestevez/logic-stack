package com.mhe.dev.compiler.lib.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

class StreamImplTest
{
    @Test
    void shouldReadCharacters() throws CompilerIoException {
        MheDummyLogger logger = new MheDummyLogger();
        String code = ",ab\n:";
        Stream stream = new StreamImpl(logger, new StringReader(code));
        char c;

        assertEquals(0, stream.getRowNumber());
        assertEquals(0, stream.getColNumber());
        assertEquals("", stream.getLexeme());

        c = stream.getNextCharacter();
        assertEquals(',', c);

        assertEquals(0, stream.getRowNumber());
        assertEquals(0, stream.getColNumber());
        assertEquals(",", stream.getLexeme());

        c = stream.getNextCharacter();
        assertEquals('a', c);

        assertEquals(0, stream.getRowNumber());
        assertEquals(0, stream.getColNumber());
        assertEquals(",a", stream.getLexeme());

        c = stream.getNextCharacter();
        assertEquals('b', c);

        assertEquals(0, stream.getRowNumber());
        assertEquals(0, stream.getColNumber());
        assertEquals(",ab", stream.getLexeme());

        stream.resetLexeme();

        assertEquals(1, stream.getRowNumber());
        assertEquals(4, stream.getColNumber());
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
        assertEquals(StreamImpl.STR_END, c);

        c = stream.getNextCharacter();
        assertEquals(StreamImpl.STR_END, c);

        assertTrue(stream.isFinished());
    }

    @Test
    void shouldThrowCompilerIOException01() throws IOException
    {
        MheDummyLogger logger = new MheDummyLogger();
        try {
            String code = ",\n:";
            Stream stream = new StreamImpl(logger, new StringReader(code));

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
        MheDummyLogger logger = new MheDummyLogger();
        try
        {
            String code = ",\n:";
            Stream stream = new StreamImpl(logger, new StringReader(code));

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
        MheDummyLogger logger = new MheDummyLogger();
        String code = ",\n:";
        Stream stream = new StreamImpl(logger, new StringReader(code));
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
        assertEquals(StreamImpl.STR_END, c);

        assertTrue(stream.isFinished());
    }
}