package mhe.compiler;

import java.io.StringReader;

import mhe.compiler.exception.CompilerException;
import mhe.compiler.logic.LogicSemanticCategory;
import mhe.compiler.logic.LogicParser;
import mhe.compiler.logic.LogicSymbolHashMap;
import mhe.compiler.mhe.MheLexer;
import mhe.compiler.mhe.MheLexicalCategory;
import mhe.compiler.model.AbstractSyntaxTree;
import mhe.compiler.model.Lexer;
import mhe.compiler.model.Stream;
import mhe.compiler.model.impl.AbstractStream;

public class MheCompiler implements CompilerInterface {

    @Override
    public String expressionToJson(String expression) throws CompilerException {
        Stream stream = new AbstractStream(new StringReader(expression));
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);
        LogicParser parser = new LogicParser(lexer, new LogicSymbolHashMap());
        AbstractSyntaxTree<LogicSemanticCategory> ast = parser.Compile();
        return ast.toJson(parser.getLogicSymbolMap().getLiterals());
    }
}
