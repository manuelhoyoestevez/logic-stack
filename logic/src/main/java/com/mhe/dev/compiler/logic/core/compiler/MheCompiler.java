package com.mhe.dev.compiler.logic.core.compiler;

import com.mhe.dev.compiler.lib.core.CompilerException;
import com.mhe.dev.compiler.lib.core.Lexer;
import com.mhe.dev.compiler.lib.core.LexerImpl;
import com.mhe.dev.compiler.lib.core.MheLexicalCategory;
import com.mhe.dev.compiler.lib.core.Stream;
import com.mhe.dev.compiler.lib.core.StreamImpl;
import com.mhe.dev.compiler.logic.core.compiler.logger.LogicParser;
import com.mhe.dev.compiler.logic.core.compiler.logger.LogicSemanticCategory;
import com.mhe.dev.compiler.logic.core.compiler.logger.LogicSymbolHashMap;
import com.mhe.dev.compiler.logic.core.compiler.logger.AbstractSyntaxTree;
import java.io.StringReader;

/**
 * MheCompiler.
 */
public class MheCompiler implements CompilerInterface
{

    @Override
    public String expressionToJson(String expression) throws CompilerException
    {
        Stream stream = new StreamImpl(null, new StringReader(expression));
        Lexer<MheLexicalCategory> lexer = new LexerImpl(null, stream);
        LogicParser parser = new LogicParser(lexer, new LogicSymbolHashMap());
        AbstractSyntaxTree<LogicSemanticCategory> ast = parser.compile();
        return ast.toJson(parser.getLogicSymbolMap().getLiterals());
    }
}
