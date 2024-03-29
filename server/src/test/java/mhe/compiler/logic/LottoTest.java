package mhe.compiler.logic;

import mhe.compiler.exception.CompilerException;
import mhe.compiler.mhe.MheLexer;
import mhe.compiler.mhe.MheLexicalCategory;
import mhe.compiler.model.AbstractSyntaxTree;
import mhe.compiler.model.Lexer;
import mhe.compiler.model.Stream;
import mhe.compiler.model.impl.AbstractStream;
import mhe.graphviz.GraphViz;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class LottoTest {
    private static final String logic = "" +
            "A =  cu ->  fc; // Un custom system SIEMPRE es de fecha cerrada\n" +
            "B =  cu -> !md; // Un custom system NUNCA es multidraw\n" +
            "C =  cu -> !dc; // Un custom system NUNCA es un producto destacado\n" +
            "D =  cu -> !dv; // Un custom system NUNCA tiene descuento por volumen\n" +
            "E =  cu -> !ps; // Un custom system SIEMPRE tiene una única participación\n" +
            "F =  cu -> !tk; // Un custom system NUNCA puede ser pagado con tickets\n" +
            "G = !fc -> !md; // Un producto de fecha abierta NUNCA es multidraw\n" +
            "H =  dv -> !pr; // Un producto con descuentos por volumen NUNCA puede tener promociones directas\n" +
            "\n" + "return [A, B, C, D, E, F, G, H];";

    @Test
    public void lottoTest() throws CompilerException {
        Stream stream = new AbstractStream(new StringReader(logic));
        Lexer<MheLexicalCategory> lexer = new MheLexer(stream);
        LogicParser parser = new LogicParser(lexer, new LogicSymbolHashMap());
        AbstractSyntaxTree<LogicSemanticCategory> ast = parser.compile();
        LogicSymbolMap symbols = parser.getLogicSymbolMap();
        List<String> literals = symbols.getLiterals();
        assertEquals(Arrays.asList("pr", "ps", "cu", "dv", "md", "tk", "fc", "dc"), literals);
        String jsonString = ast.toJson(literals);
        assertEquals("{\"operator\":\"and\",\"order\":[\"pr\",\"ps\",\"cu\",\"dv\",\"md\",\"tk\",\"fc\",\"dc\"],\"children\":[{\"operator\":\"or\",\"children\":[{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"cu\"}]},{\"operator\":\"literal\",\"literal\":\"fc\"}]},{\"operator\":\"or\",\"children\":[{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"cu\"}]},{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"md\"}]}]},{\"operator\":\"or\",\"children\":[{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"cu\"}]},{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"dc\"}]}]},{\"operator\":\"or\",\"children\":[{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"cu\"}]},{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"dv\"}]}]},{\"operator\":\"or\",\"children\":[{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"cu\"}]},{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"ps\"}]}]},{\"operator\":\"or\",\"children\":[{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"cu\"}]},{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"tk\"}]}]},{\"operator\":\"or\",\"children\":[{\"operator\":\"not\",\"children\":[{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"fc\"}]}]},{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"md\"}]}]},{\"operator\":\"or\",\"children\":[{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"dv\"}]},{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"pr\"}]}]}]}", jsonString);
        String dotString = GraphViz.drawTree(ast, "AST");
        System.out.println(dotString);
    }
}
