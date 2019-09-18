package mhe.compiler.logic.ast;

import java.util.*;

import mhe.compiler.*;
import mhe.compiler.logic.*;

public class ASTc extends AST {

	public ASTc() {
		super(CONDLOGI, true, null);
	}
	
	public ASTc(ASTInterface a, ASTInterface c) {
		this();
		this.getChildren().add(a);
		
		if(!c.isLambda()) {
			this.getChildren().add(c);
		}
	}

	@Override
	public String getShape() {
		return quotify("rectangle");
	}

	@Override
	public String getLabel() {
		return quotify("ASTc ->");
	}

	@Override
	public String getColor() {
		return quotify("purple");
	}

	@Override
	public LogicNodeInterface getLogicNode() {
		ASTInterface first = this.getFirstChild();
		ASTInterface second = this.getSecondChild();
			
		if(first == null) {
			return null;
		}
		else if(second == null) {
			return first.getLogicNode();
		}
		else {
			// A
			LogicNodeInterface A = first.getLogicNode();
			
			// B
			LogicNodeInterface B = second.getLogicNode();
			
			// !A
			LogicNodeSetInterface newChildrenNotA = new LogicNodeSet();
			newChildrenNotA.add(A);
			LogicNodeInterface notA = new LogicNode(A.getUsedLiterals(), new HashMap<String, Boolean>(), LogicNodeType.NOT, false, null, newChildrenNotA);
			
			// !A | B
			Set<String> newUsedLiteralsOr = new TreeSet<String>();
			LogicNodeSetInterface newChildrenOr = new LogicNodeSet();
			newChildrenOr.add(notA);
			newChildrenOr.add(B);
			newUsedLiteralsOr.addAll(notA.getUsedLiterals());
			newUsedLiteralsOr.addAll(B.getUsedLiterals());
			return new LogicNode(newUsedLiteralsOr, new HashMap<String, Boolean>(), LogicNodeType.OPERATOR, false, null, newChildrenOr);
		}
	}
}
