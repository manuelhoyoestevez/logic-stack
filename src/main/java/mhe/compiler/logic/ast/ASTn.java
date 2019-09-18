package mhe.compiler.logic.ast;

import java.util.*;

import mhe.compiler.*;
import mhe.compiler.logic.*;

public class ASTn extends AST {

	public ASTn() {
		super(NOTLOGI, true, null);
	}
	
	public ASTn(ASTInterface l) {
		this();
		this.getChildren().add(l);
	}

	@Override
	public String getShape() {
		return "\"circle\"";
	}

	@Override
	public String getLabel() {
		return "\"ASTn !\"";
	}

	@Override
	public String getColor() {
		return "\"orange\"";
	}

	@Override
	public LogicNodeInterface getLogicNode() {
		ASTInterface first = this.getFirstChild();
		if(first == null) {
			return null;
		}
		else {
			Set<String> newUsedLiterals = new TreeSet<String>();
			LogicNodeSetInterface newChildren = new LogicNodeSet();
			
			for(ASTInterface child : this.getChildren()) {
				LogicNodeInterface aux = child.getLogicNode();
				if(aux != null) {
					newChildren.add(aux);
					newUsedLiterals.addAll(aux.getUsedLiterals());
				}
			}
			
			return new LogicNode(newUsedLiterals, new HashMap<String, Boolean>(), LogicNodeType.NOT, false, null, newChildren); // NOT;
		}
	}
}
