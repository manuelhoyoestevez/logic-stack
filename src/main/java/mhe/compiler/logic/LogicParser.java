package mhe.compiler.logic;

import mhe.compiler.*;
import mhe.compiler.logger.LogType;
import mhe.compiler.logic.ast.*;
import mhe.compiler.mhe.*;

public class LogicParser extends Parser implements LogicASTConstants, LexicalCategoryMHE {	
	
	public LogicSymbolMapInterface lset;
	
	public LogicParser(LexerInterface lexer, LogicSymbolMapInterface lset) {
		super(lexer);
		this.lset = lset;
	}
	
	public LogicParser(LexerInterface lexer) {
		this(lexer, new LogicSymbolMap(lexer.getLogger()));
	}
	
	public LogicSymbolMapInterface getLogicSymbolMap() {
		return this.lset;
	}

	protected String getLexeme() {
		return this.getLexer().getStream().getLexeme();
	}

	public ASTInterface Compile() throws Exception{
		//this.currenttokencat = this.getLexer().getNextTokenCategory();
		
		this.getLexer().getNextTokenCategory();
		return this.CompileP();
	}

	protected ASTInterface CompileP() throws Exception{		
		ASTInterface r; 
		this
		.getLogger()
		.incTabLevel()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "+ CompileP(): ");
		
		switch(this.getLexer().getCurrentTokenCategory()){
			case IDENTIFIER:
			case EXIT:
			case SAVE:
			case SHOW:
			case RETURN:
				ASTInterface s = this.CompileS();
				this.getLexer().matchToken(SEMICOLON);
				r = new ASTp(s, this.CompileP());
				break;
			case END:
				r = AST.ASTlambda;
				break;
			default:
				String[] y = {"identificador", "show", "save", "exit", "$end"};
				this.getLogger().logError(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "CompileP: Se esperaba " + y + " en lugar de " + this.getLexer().getCurrentToken());
				r = new ASTerror();
		}
		this
		.getLogger()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "- CompileP: ")
		.decTabLevel(); 
		return r;
	}
	
	protected ASTInterface CompileS() throws Exception{
		String id;
		ASTInterface r;
		
		this
		.getLogger()
		.incTabLevel()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "+ CompileS(): ");
		
		switch(this.getLexer().getCurrentTokenCategory()){
			case RETURN:
				this.getLexer().matchToken(RETURN);
				r = new ASTreturn(this.CompileE());
				break;
			case SHOW:
				this.getLexer().matchToken(SHOW);				
				id = this.lset.processShow(this.getLexer().getCurrentToken());
				this.getLexer().matchToken(IDENTIFIER);
				r = new ASTshow(id);
				break;
			case EXIT:
				this.getLexer().matchToken(EXIT);
				r = new ASTexit();
				break;
			case SAVE:
				this.getLexer().matchToken(SAVE);
				// Comprobaciones de fichero
				id = this.getLexeme();
				this.getLexer().matchToken(STRING);
				r = new ASTsave(id);
				break;
			case IDENTIFIER:
				id = this.getLexeme();
				SymbolInterface s = this.lset.processAssignement(this.getLexer().getCurrentToken());
				this.getLexer().matchToken(IDENTIFIER);
				this.getLexer().matchToken(EQUAL);
				ASTInterface e = this.CompileE();
				s.setAST(e);
				r = new ASTasig(id,e);
				break;
			default:
				String[] y = {"identificador", "show", "save", "exit"};
				this.getLogger().logError(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "CompileS: Se esperaba " + y + " en lugar de " + this.getLexer().getCurrentToken());
				r = new ASTerror();
		}
		
		this
		.getLogger()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "- CompileS(): ")
		.decTabLevel(); 
		
		return r;
	}
	
	protected ASTInterface CompileE() throws Exception{
		ASTInterface r;
		
		this
		.getLogger()
		.incTabLevel()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "+ CompileE(): ");
		
		switch(this.getLexer().getCurrentTokenCategory()){
			case LKEY:
			case LCORCH:
			case INTEGER:
			case IDENTIFIER:
			case LPAREN:
			case NOT:
				r = new ASTe(CompileC(),CompileE0());
				break;
			default: 
				String[] y = {"identificador", "entero", "(", "[", "{", "!"};
				this.getLogger().logError(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "CompileE: Se esperaba " + y + " en lugar de " + this.getLexer().getCurrentToken());
				r = new ASTerror();
		}
		
		this
		.getLogger()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "- CompileE(): ")
		.decTabLevel(); 
		
		return r;
	}
	
	protected ASTInterface CompileE0() throws Exception{
		ASTInterface r; 
		
		this
		.getLogger()
		.incTabLevel()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "+ CompileE0(): ");
		
		switch(this.getLexer().getCurrentTokenCategory()){
			case IMPLDOUBLE:
				this.getLexer().matchToken(IMPLDOUBLE);
			//	r = this.CompileE();
				r = new ASTe(CompileC(),CompileE0());
				break;
			case COLON:
			case RKEY:
			case RCORCH:
			case SEMICOLON:
			case RPAREN:
				r = AST.ASTlambda;
				break;
			default:
				String[] y = {"<>", ")", ";", "]", "}", "," };
				this.getLogger().logError(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "CompileE0: Se esperaba " + y + " en lugar de " + this.getLexer().getCurrentToken());
				r = new ASTerror();
		}
		
		this
		.getLogger()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "- CompileE0(): ")
		.decTabLevel(); 
		
		return r;
	}
	
	protected ASTInterface CompileC() throws Exception{
		ASTInterface r;
		
		this
		.getLogger()
		.incTabLevel()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "+ CompileC(): ");
		
		switch(this.getLexer().getCurrentTokenCategory()){
			case LKEY:
			case LCORCH:
			case INTEGER:
			case IDENTIFIER:
			case LPAREN:
			case NOT:
				r = new ASTc(this.CompileA(),this.CompileC0());
				break;
			default:
				String[] y = {"identificador", "entero", "(", "[", "{", "!"};
				this.getLogger().logError(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "CompileC: Se esperaba " + y + " en lugar de " + this.getLexer().getCurrentToken());
				r = new ASTerror();
		}

		this
		.getLogger()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "- CompileC(): ")
		.decTabLevel(); 
		
		return r;
	}
	
	protected ASTInterface CompileC0() throws Exception{
		ASTInterface r; 

		this
		.getLogger()
		.incTabLevel()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "+ CompileC0(): ");
		
		switch(this.getLexer().getCurrentTokenCategory()){
			case IMPLRIGHT:
				this.getLexer().matchToken(IMPLRIGHT);
			//	r = this.CompileC();
				r = new ASTc(this.CompileA(),this.CompileC0());
				break;
			case COLON:
			case RKEY:
			case RCORCH:
			case SEMICOLON:
			case RPAREN:
			case IMPLDOUBLE:
				r = AST.ASTlambda;
				break;
			default:
				String[] y = {"->", "<>", ")", ";", "]", "}", "," };
				this.getLogger().logError(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "CompileC0: Se esperaba " + y + " en lugar de " + this.getLexer().getCurrentToken());
				r = new ASTerror();
		}
		this
		.getLogger()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "- CompileC0(): ")
		.decTabLevel(); 
		
		return r;
	}
	
	protected ASTInterface CompileA() throws Exception{
		ASTInterface r;

		this
		.getLogger()
		.incTabLevel()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "+ CompileA(): ");
		
		switch(this.getLexer().getCurrentTokenCategory()){
			case LKEY:
			case LCORCH:
			case INTEGER:
			case IDENTIFIER:
			case LPAREN:
			case NOT:
				r = new ASTa(this.CompileO(),this.CompileA0());
				break;
			default:
				String[] y = {"identificador", "entero", "(", "[", "{", "!"};
				this.getLogger().logError(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "CompileA: Se esperaba " + y + " en lugar de " + this.getLexer().getCurrentToken());
				r = new ASTerror();
		}
		
		this
		.getLogger()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "- CompileA(): ")
		.decTabLevel(); 
		
		return r;
	}
	
	protected ASTInterface CompileA0() throws Exception{
		ASTInterface r;
		
		this
		.getLogger()
		.incTabLevel()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "+ CompileA0(): ");
		
		switch(this.getLexer().getCurrentTokenCategory()){
			case AMPERSAND:
				this.getLexer().matchToken(AMPERSAND);
				r = CompileA();
				break;
			case COLON:
			case RKEY:
			case RCORCH:
			case SEMICOLON:
			case RPAREN:
			case IMPLDOUBLE:
			case IMPLRIGHT:
				r = AST.ASTlambda;
				break;
			default:
				String[] y = {"&", "->", "<>", ")", ";", "]", "}", "," };
				this.getLogger().logError(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "CompileA0: Se esperaba " + y + " en lugar de " + this.getLexer().getCurrentToken());
				r = new ASTerror();
		}
		this
		.getLogger()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "- CompileA0(): ")
		.decTabLevel(); 
		
		return r;
	}
	
	protected ASTInterface CompileO() throws Exception{
		ASTInterface r; 

		this
		.getLogger()
		.incTabLevel()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "+ CompileO(): ");
		
		switch(this.getLexer().getCurrentTokenCategory()){
			case LKEY:
			case LCORCH:
			case INTEGER:
			case IDENTIFIER:
			case LPAREN:
			case NOT:
				r = new ASTo(this.CompileN(),this.CompileO0());
				break;
			default: 
				String[] y = {"identificador", "entero", "(", "[", "{", "!"};
				this.getLogger().logError(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "CompileO: Se esperaba " + y + " en lugar de " + this.getLexer().getCurrentToken());
				r = new ASTerror();
		}
		this
		.getLogger()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "- CompileO(): ")
		.decTabLevel(); 
		
		return r;
	}
	
	
	protected ASTInterface CompileO0() throws Exception{
		ASTInterface r;
		
		this
		.getLogger()
		.incTabLevel()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "+ CompileO0(): ");
		
		switch(this.getLexer().getCurrentTokenCategory()){
			case BAR:
				this.getLexer().matchToken(BAR);
				r = CompileO();
				break;
			case COLON:
			case RKEY:
			case RCORCH:
			case SEMICOLON:
			case RPAREN:
			case IMPLDOUBLE:
			case IMPLRIGHT:
			case AMPERSAND:
				r = AST.ASTlambda;
				break;
			default:
				String[] y = {"|", "&", "->", "<>", ")", ";", "]", "}", "," };
				this.getLogger().logError(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "CompileO0: Se esperaba " + y + " en lugar de " + this.getLexer().getCurrentToken());
				r = new ASTerror();
		}
		this
		.getLogger()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "- CompileO0(): ")
		.decTabLevel(); 
		
		return r;
	}
	
	
	protected ASTInterface CompileN() throws Exception{
		ASTInterface r;

		this
		.getLogger()
		.incTabLevel()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "+ CompileN(): ");
		
		switch(this.getLexer().getCurrentTokenCategory()){
			case LKEY:
			case LCORCH:
			case INTEGER:
			case IDENTIFIER:
			case LPAREN:
				r = this.CompileL();
				break;
			case NOT:
				this.getLexer().matchToken(NOT);				
				r = new ASTn(this.CompileN());
				break;
			default:
				String[] y = {"identificador", "entero", "(", "[", "{", "!"};
				this.getLogger().logError(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "CompileN: Se esperaba " + y + " en lugar de " + this.getLexer().getCurrentToken());
				r = new ASTerror();
		}
		this
		.getLogger()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "- Compilen(): ")
		.decTabLevel(); 
		
		return r;
	}
	
	protected ASTInterface CompileL() throws Exception{
		ASTInterface r;
		
		this
		.getLogger()
		.incTabLevel()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "+ CompileL(): ");
		
		switch(this.getLexer().getCurrentTokenCategory()){
			case INTEGER:
				r = AST.constant(this.lset.processInteger(this.getLexer().getCurrentToken()));
				this.getLexer().matchToken(INTEGER);
				break;
			case IDENTIFIER:
				SymbolInterface s = this.lset.processIdentifier(this.getLexer().getCurrentToken());
				
				r = s.getAST();
				
				if(r == null){
					r = new ASTid(this.getLexeme());
					s.setAST(r);
				}
				/*
				else if(!s.isLiteral()){
					r = new ASTvar(s.getName());
				}
				*/
				this.getLexer().matchToken(IDENTIFIER);
				break;
			case LPAREN:
				this.getLexer().matchToken(LPAREN);
				r = this.CompileE();
				this.getLexer().matchToken(RPAREN);
				break;
			case LKEY:
				this.getLexer().matchToken(LKEY);
				r = this.CompileX(true);
				this.getLexer().matchToken(RKEY);
				break;
			case LCORCH:
				this.getLexer().matchToken(LCORCH);
				r = this.CompileX(false);
				this.getLexer().matchToken(RCORCH);
				break;
			default:
				String[] y = {"identificador", "entero", "(", "[", "{"};
				this.getLogger().logError(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "CompileL: Se esperaba " + y + " en lugar de " + this.getLexer().getCurrentToken());
				r = new ASTerror();
		}
		this
		.getLogger()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "- CompileL(): ")
		.decTabLevel(); 
		
		return r;
	}
	
	protected ASTInterface CompileX(boolean x) throws Exception{
		ASTInterface r;
		
		this
		.getLogger()
		.incTabLevel()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "+ CompileX(): ");
		
		switch(this.getLexer().getCurrentTokenCategory()){
			case LCORCH:
			case LKEY:
			case INTEGER:
			case IDENTIFIER:
			case LPAREN:
			case NOT:
				r = x
					? new ASTo(this.CompileE(),this.CompileX0(x))
					: new ASTa(this.CompileE(),this.CompileX0(x));
				break;
			default: 
				String[] y = {"identificador", "entero", "(", "[", "{", "!"};
				this.getLogger().logError(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "CompileX: Se esperaba " + y + " en lugar de " + this.getLexer().getCurrentToken());
				r = new ASTerror();
		}
		this
		.getLogger()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "- CompileX(): ")
		.decTabLevel(); 
		
		return r;
	}
	
	
	protected ASTInterface CompileX0(boolean x) throws Exception{
		ASTInterface r;
		
		this
		.getLogger()
		.incTabLevel()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "+ CompileX0(): ");
		
		switch(this.getLexer().getCurrentTokenCategory()){
			case COLON:
				this.getLexer().matchToken(COLON);
				r = CompileX(x);
				break;
			case RKEY:
			case RCORCH:
				r = AST.ASTlambda;
				break;
			default:
				String[] y = { "]", "}", "," };
				this.getLogger().logError(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "CompileX0: Se esperaba " + y + " en lugar de " + this.getLexer().getCurrentToken());
				r = new ASTerror();
		}
		this
		.getLogger()
		.logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken(), "- CompileX0(): ")
		.decTabLevel(); 
		
		return r;
	}
}
