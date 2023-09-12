package com.mhe.dev.compiler.logic.core.compiler;

import com.mhe.dev.compiler.lib.core.CompilerException;
import com.mhe.dev.compiler.lib.core.Lexer;
import com.mhe.dev.compiler.lib.core.LexerImpl;
import com.mhe.dev.compiler.lib.core.MheLexicalCategory;
import com.mhe.dev.compiler.lib.core.MheLogger;
import com.mhe.dev.compiler.lib.core.Stream;
import com.mhe.dev.compiler.lib.core.StreamImpl;
import java.io.StringReader;

/**
 * LogicCompiler.
 */
public class LogicCompiler
{
    public String expressionToJson(String expression, MheLogger logger) throws CompilerException
    {
        Stream stream = new StreamImpl(logger, new StringReader(expression));
        Lexer<MheLexicalCategory> lexer = new LexerImpl(logger, stream);
        LogicParser parser = new LogicParser(logger, lexer, new LogicSymbolHashMap(logger));
        AbstractSyntaxTree<LogicSemanticCategory> ast = parser.compile();
        return ast.toJson(parser.getLogicSymbolMap().getLiterals());
    }
}
