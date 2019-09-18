package mhe.compiler.logic.ast;

import java.util.*;

import mhe.compiler.*;
import mhe.compiler.logic.*;

public class ASTa extends AST {
	
	public ASTa() {
		super(ANDLOGI, true, null);
	}

	public ASTa(ASTInterface a, ASTInterface o) {
		this();
		this.getChildren().add(a);
		this.getChildren().addAll(o.getChildren());
		
	}

	@Override
	public String getShape() {
		return quotify("rectangle");
	}

	@Override
	public String getLabel() {
		return quotify("ASTa &");
	}

	@Override
	public String getColor() {
		return quotify("blue");
	}


	public ASTInterface cloneEmpty() {
		return new ASTa();
	}
	
	@Override
	public LogicNodeInterface getLogicNode() {

		switch(this.getChildren().size()) {
		case 0: return null;
		case 1: return this.getFirstChild().getLogicNode();
		default:
			Set<String> newUsedLiterals = new TreeSet<String>();
			LogicNodeSetInterface newChildren = new LogicNodeSet();
			
			for(ASTInterface child : this.getChildren()) {
				LogicNodeInterface aux = child.getLogicNode();
				if(aux != null) {
					newChildren.add(aux);
					newUsedLiterals.addAll(aux.getUsedLiterals());
				}
			}
			
			return new LogicNode(newUsedLiterals, new HashMap<String, Boolean>(), LogicNodeType.OPERATOR, true, null, newChildren); // AND;
		}
	}
}
