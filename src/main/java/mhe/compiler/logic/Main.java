package mhe.compiler.logic;

import java.io.StringReader;

import io.vertx.core.json.JsonObject;
import mhe.compiler.ASTInterface;
import mhe.compiler.LexerInterface;
import mhe.compiler.Stream;
import mhe.compiler.StreamInterface;
import mhe.compiler.logger.Logger;
import mhe.compiler.mhe.LexicalAnalyzerMHE;

public class Main {

	public static void main(String[] args) {
			String l  = "" +
					"X = a & b & c | 1;" +
					"Y = d -> 0 & X <> (!a |d & !{!a,c,![b,c],d} <> c);" +
					"return !(Y & X);";
/*
			l  = ""
					+ "X = a -> !b;"
					+ "Y = b -> !c;"
					+ "Z = c -> !d;"

					+ "return X & Y & Z;";
*/
			l = "" +
					"A =  cu ->  fc; // Un custom system SIEMPRE es de fecha cerrada\n" +
					"B =  cu -> !md; // Un custom system NUNCA es multidraw\n" +
					"C =  cu -> !dc; // Un custom system NUNCA es un producto destacado\n" +
					"D =  cu -> !dv; // Un custom system NUNCA tiene descuento por volumen\n" +
					"E =  cu -> !ps; // Un custom system SIEMPRE tiene una única participación\n" +
					"F =  cu -> !tk; // Un custom system NUNCA puede ser pagado con tickets\n" +
					"G = !fc -> !md; // Un producto de fecha abierta NUNCA es multidraw\n" +
					"H =  dv -> !pr; // Un producto con descuentos por volumen NUNCA puede tener promociones directas\n" +
					"\n" +
					"return [A, B, C, D, E, F, G, H];";
			/*
			l = "" +
					"A =  [!x, !y, !z];" +
					"B =  [!x, !y,  z];" +
					"C =  [!x,  y, !z];" +
					"D =  [!x,  y,  z];" +
					"E =  [ x, !y, !z];" +
					"F =  [ x, !y,  z];" +
					"G =  [ x,  y, !z];" +
					"H =  [ x,  y,  z];" +
					"\n" +
					"return {D, G, H};";
*/
			//String pete = "return !(b | !c);";
			//pete = "return a <> !(b | !c);";
			//l = "return !(a <> !(b | !c));";
			//l = "return !(!a <> !(!b | !c));";

			try {

				StreamInterface stream = new Stream(new StringReader(l), new Logger());

				LexerInterface lexer = new LexicalAnalyzerMHE(stream);

				LogicParser parser  = new LogicParser(lexer);

				ASTInterface ast = parser.Compile();

				//LogicSymbolMapInterface symbols = parser.getLogicSymbolMap();

				//List<String> literals = symbols.getLiterals();

				JsonObject logicNode = ast.toJson();


				System.out.println(logicNode.toString());

				//logicNode.setAllLiterals(literals);

				//LogicFunctionCacheInterface cache = new LogicFunctionCache(logicNode);

				//System.out.println(GraphViz.drawTree(logicNode, "logicNode"));

				//System.out.println(GraphViz.drawTree((GraphVizNode) logicNode.not(), "logicNode_not"));

				//cache.calculate().expand();

				//System.out.println(cache);
				//System.out.println(GraphViz.drawTree(cache, "cache"));

				/*
				HashMap<String, Boolean> reduction = new HashMap<String, Boolean>();
				reduction.put("a", false);
				LogicNodeInterface reducedLogicNode = (LogicNodeInterface) logicNode.reduceBy(reduction);
				System.out.println(GraphViz.drawTree(reducedLogicNode, "reducedLogicNode"));
				*/

				//System.out.println(cache.toString());

				//System.out.println(GraphViz.drawTree(ast, "AST"));

				//System.out.println(GraphViz.drawTree(monganLogicNode, "monganLogicNode"));

				//System.out.println(GraphViz.drawTree(reducedLogicNode, "reducedLogicNode"));

				//System.out.println(GraphViz.drawTree(cache, "cache"));

			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}
}
