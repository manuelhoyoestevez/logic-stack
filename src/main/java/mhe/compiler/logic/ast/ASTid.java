package mhe.compiler.logic.ast;

import java.util.*;

import mhe.compiler.logic.*;

public class ASTid extends AST {

	public ASTid(String n) {
		super(LITLOGI, true, n);
	}

	@Override
	public String getShape() {
		return quotify("square");
	}

	@Override
	public String getLabel() {
		return quotify(this.getName());
	}

	@Override
	public String getColor() {
		return quotify("blue");
	}

	@Override
	public LogicNodeInterface getLogicNode() {
		Set<String> newUsedLiterals = new TreeSet<String>();
		newUsedLiterals.add(this.getName());
		return new LogicNode(newUsedLiterals, new HashMap<String, Boolean>(), LogicNodeType.LITERAL, false, this.getName(), new LogicNodeSet());
	}
}
