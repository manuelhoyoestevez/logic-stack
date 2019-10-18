package mhe.compiler;

import java.io.StringReader;

import mhe.compiler.exception.CompilerException;
import mhe.compiler.logger.DefaultLogger;
import mhe.compiler.logic.LogicSemanticCategory;
import mhe.compiler.logic.LogicParser;
import mhe.compiler.mhe.MheLexer;
import mhe.compiler.mhe.MheLexicalCategory;
import mhe.compiler.model.AbstractSintaxTree;
import mhe.compiler.model.Lexer;
import mhe.compiler.model.Stream;
import mhe.compiler.model.impl.AbstractStream;

public class MheCompiler implements CompilerInterface {
    private DefaultLogger logger = new DefaultLogger();

    @Override
    public String expressionToJson(String expression) throws CompilerException {
        Stream stream = new AbstractStream(new StringReader(expression), logger);
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);
        LogicParser parser = new LogicParser(lexer);
        AbstractSintaxTree<LogicSemanticCategory> ast = parser.Compile();
        return ast.toJson(parser.getLogicSymbolMap().getLiterals());
    }
}
