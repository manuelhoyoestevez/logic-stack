package mhe.compiler.logic;

import java.io.*;

import mhe.compiler.*;
import mhe.compiler.logger.Logger;
import mhe.compiler.mhe.*;

public class LogicConsole implements LogicASTConstants{	
	
	public static void main(String[] args) {	
		boolean exit;
		BufferedReader input;
		Writer output;
		
		Logger logger = new Logger();
		LogicSymbolMapInterface symbols = new LogicSymbolMap(logger);
		
		StreamInterface stream;
		LexerInterface lexer;
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
								SymbolInterface r = symbols.get(s.getName());
								
								if(r != null) {
									output.write(r.getAST().getLogicNode().getExpression() + "\r\n");
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
