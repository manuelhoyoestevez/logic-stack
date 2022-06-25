package com.mhe.dev.logic.stack.core.compiler;

import java.io.StringReader;
import com.mhe.dev.logic.stack.core.compiler.exception.CompilerException;
import com.mhe.dev.logic.stack.core.compiler.logic.LogicParser;
import com.mhe.dev.logic.stack.core.compiler.logic.LogicSemanticCategory;
import com.mhe.dev.logic.stack.core.compiler.logic.LogicSymbolHashMap;
import com.mhe.dev.logic.stack.core.compiler.mhe.MheLexer;
import com.mhe.dev.logic.stack.core.compiler.mhe.MheLexicalCategory;
import com.mhe.dev.logic.stack.core.compiler.model.AbstractSyntaxTree;
import com.mhe.dev.logic.stack.core.compiler.model.Lexer;
import com.mhe.dev.logic.stack.core.compiler.model.Stream;
import com.mhe.dev.logic.stack.core.compiler.model.impl.AbstractStream;

/**
 * MheCompiler.
 */
public class MheCompiler implements CompilerInterface {

    @Override
    public String expressionToJson(String expression) throws CompilerException {
        Stream stream = new AbstractStream(new StringReader(expression));
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);
        LogicParser parser = new LogicParser(lexer, new LogicSymbolHashMap());
        AbstractSyntaxTree<LogicSemanticCategory> ast = parser.compile();
        return ast.toJson(parser.getLogicSymbolMap().getLiterals());
    }
}
