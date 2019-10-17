package mhe.compiler;

import java.io.StringReader;

import mhe.compiler.exception.CompilerException;
import mhe.compiler.logger.Logger;
import mhe.compiler.logic.LogicParser;
import mhe.compiler.mhe.LexicalAnalyzerMHE;
import mhe.compiler.mhe.MheLexicalCategory;

public class MheCompiler implements CompilerInterface {
    private Logger logger = new Logger();

    @Override
    public String expressionToJson(String expression) throws CompilerException {
        StreamInterface stream = new Stream(new StringReader(expression), logger);
        LexerInterface<MheLexicalCategory> lexer = new LexicalAnalyzerMHE(stream);
        LogicParser parser = new LogicParser(lexer);
        ASTInterface ast = parser.Compile();
        return ast.toJson(parser.getLogicSymbolMap().getLiterals());
    }
}
