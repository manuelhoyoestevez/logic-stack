package mhe.compiler.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;

import mhe.compiler.logger.DefaultLogger;
import mhe.compiler.mhe.MheLexer;
import mhe.compiler.mhe.MheLexicalCategory;
import mhe.compiler.model.AbstractSintaxTree;
import mhe.compiler.model.Lexer;
import mhe.compiler.model.Stream;
import mhe.compiler.model.Symbol;
import mhe.compiler.model.impl.AbstractStream;

public class LogicConsole {

	public static void main(String[] args) {
		boolean exit;
		BufferedReader input;
		Writer output;

		DefaultLogger logger = new DefaultLogger();
		LogicSymbolMap symbols = new LogicSymbolHashMap(logger);

		Stream stream;
		Lexer<MheLexicalCategory> lexer;
		LogicParser parser;
		AbstractSintaxTree<LogicSemanticCategory> ast;

		try {
			input  = new BufferedReader (new InputStreamReader(System.in));
			output = new PrintWriter(System.out);
			exit = false;

			while(!exit){
				try {
					output.write(symbols.toString() + "\r\n");
					output.write("> ");
					output.flush();

					stream = new AbstractStream(new StringReader(input.readLine()), logger);
					lexer = new MheLexer(stream);
					parser  = new LogicParser(lexer, symbols);
					ast = parser.Compile();

					for(AbstractSintaxTree<LogicSemanticCategory> s : ast.getChildren()) {
						switch(s.getType()) {
							case EXITLOGI:
								exit = true;
								break;
							case ASIGLOGI:
								output.write("Asignada la variable " + s.getName() + "\r\n");
								break;
							case SHOWLOGI:
								Symbol<MheLexicalCategory, LogicSemanticCategory> r = symbols.getSymbolByName(s.getName());

								if(r != null) {
									output.write(r.getAST().toJson(symbols.getLiterals()).toString() + "\r\n");
								}
								else {
									output.write("Variable " + s.getName() + " no encontrada\r\n");
								}

								break;
							default:
								output.write("Orden no habilitada\r\n.");
						}
					}
				}
				catch (Exception e) {
					try {
						output.write(e.toString() + "\n");
						output.flush();
					}
					catch (IOException ioe) {
						ioe.printStackTrace();
					}
				}
			}
		}
		catch (Exception ioe) {
			ioe.printStackTrace();
		}
	}
}
