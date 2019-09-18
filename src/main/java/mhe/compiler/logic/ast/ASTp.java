package mhe.compiler.logic.ast;

import mhe.compiler.ASTInterface;
import mhe.compiler.logic.LogicNodeInterface;

public class ASTp extends AST {

	public ASTp() {
		super(CODELOGI,true,null);
	}
	
	public ASTp(ASTInterface s, ASTInterface p) {
		this();
		this.getChildren().add(s);
		this.getChildren().addAll(p.getChildren());
	}
	
	@Override
	public LogicNodeInterface getLogicNode() {
		
//System.out.println("ASTp");
		
		for(ASTInterface child : this.getChildren()) {
			
//System.out.print(" - " + child.getType() + " ");
			
			if(child.getType() == RETURNLOGI) {
//System.out.println("SI");
				return child.getLogicNode();
			}
			else {
//System.out.println("NO");
			}
		}
		
		return null;
	}

	@Override
	public String getShape() {
		return "rectangle";
	}

	@Override
	public String getLabel() {
		return "ASTp code";
	}

	@Override
	public String getColor() {
		return "black";
	}

	public ASTInterface cloneEmpty() {
		return new ASTp();
	}
}
