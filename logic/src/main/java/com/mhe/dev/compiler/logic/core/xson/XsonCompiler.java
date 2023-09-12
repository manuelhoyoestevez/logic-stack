package com.mhe.dev.compiler.logic.core.xson;

import com.mhe.dev.compiler.lib.core.CompilerException;
import com.mhe.dev.compiler.lib.core.Lexer;
import com.mhe.dev.compiler.lib.core.LexerImpl;
import com.mhe.dev.compiler.lib.core.MheLexicalCategory;
import com.mhe.dev.compiler.lib.core.MheLogger;
import com.mhe.dev.compiler.lib.core.Stream;
import com.mhe.dev.compiler.lib.core.StreamImpl;
import java.io.StringReader;

public class XsonCompiler
{
    public XsonValue compile(String expression, MheLogger logger) throws CompilerException
    {
        Stream stream = new StreamImpl(logger, new StringReader(expression));
        Lexer<MheLexicalCategory> lexer = new LexerImpl(logger, stream);
        XsonParser xsonParser = new XsonParser(logger, lexer);
        return xsonParser.compile();
    }
}
