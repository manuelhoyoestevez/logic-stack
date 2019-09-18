package mhe.compiler.logic.ast;

import java.util.*;

import mhe.compiler.*;
import mhe.compiler.logic.*;
import mhe.compiler.logic.ast.AST;

public class ASTo extends AST {

	public ASTo() {
		super(ORLOGI, true, null);
	}
	
	public ASTo(ASTInterface n, ASTInterface o) {
		this();
		this.getChildren().add(n);
		this.getChildren().addAll(o.getChildren());
	}

	@Override
	public String getShape() {
		return quotify("rectangle");
	}

	@Override
	public String getLabel() {
		return quotify("ASTo |");
	}

	@Override
	public String getColor() {
		return quotify("red");
	}
	
	public ASTInterface cloneEmpty() {
		return new ASTo();
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
			
			return new LogicNode(newUsedLiterals, new HashMap<String, Boolean>(), LogicNodeType.OPERATOR, false, null, newChildren);
		}
	}
}
