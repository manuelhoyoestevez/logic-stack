package mhe.compiler.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;

import mhe.compiler.ASTInterface;
import mhe.compiler.LexerInterface;
import mhe.compiler.Stream;
import mhe.compiler.StreamInterface;
import mhe.compiler.SymbolInterface;
import mhe.compiler.logger.Logger;
import mhe.compiler.mhe.LexicalAnalyzerMHE;
import mhe.compiler.mhe.MheLexicalCategory;

public class LogicConsole implements LogicASTConstants{

	public static void main(String[] args) {
		boolean exit;
		BufferedReader input;
		Writer output;

		Logger logger = new Logger();
		LogicSymbolMapInterface symbols = new LogicSymbolMap(logger);

		StreamInterface stream;
		LexerInterface<MheLexicalCategory> lexer;
		LogicParser parser;
		ASTInterface ast;

		try {
			input  = new BufferedReader (new InputStreamReader(System.in));
			output = new PrintWriter(System.out);
			exit = false;

			while(!exit){
				try {
					output.write(symbols.toString() + "\r\n");
					output.write("> ");
					output.flush();

					stream = new Stream(new StringReader(input.readLine()), logger);
					lexer = new LexicalAnalyzerMHE(stream);
					parser  = new LogicParser(lexer, symbols);
					ast = parser.Compile();

					for(ASTInterface s : ast.getChildren()) {
						switch(s.getType()) {
							case EXITLOGI:
								exit = true;
								break;
							case ASIGLOGI:
								output.write("Asignada la variable " + s.getName() + "\r\n");
								break;
							case SHOWLOGI:
								SymbolInterface<MheLexicalCategory> r = symbols.get(s.getName());

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
