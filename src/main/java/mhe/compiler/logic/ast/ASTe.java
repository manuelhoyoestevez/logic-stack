package mhe.compiler.logic.ast;

import java.util.*;

import mhe.compiler.*;
import mhe.compiler.logic.*;

public class ASTe extends AST {

	public ASTe() {
		super(EQLOGI, true, null);
	}
	
	public ASTe(ASTInterface c, ASTInterface e) {
		this();
		this.getChildren().add(c);
		
		if(!e.isLambda()) {
			this.getChildren().add(e);
		}
	}

	@Override
	public String getShape() {
		return "\"rectangle\"";
	}

	@Override
	public String getLabel() {
		return "\"ASTe <>\"";
	}

	@Override
	public String getColor() {
		return "\"green\"";
	}
	
	@Override
	public LogicNodeInterface getLogicNode() {
		ASTInterface a = this.getFirstChild();
		ASTInterface b = this.getSecondChild();
			
		if(a == null) {
			return null;
		}
		else if(b == null) {
			return a.getLogicNode();
		}
		else {
			// A
			LogicNodeInterface A = a.getLogicNode();
			
			// B
			LogicNodeInterface B = b.getLogicNode();

			// !A
			LogicNodeSetInterface newChildrenNotA = new LogicNodeSet();
			newChildrenNotA.add(A);
			LogicNodeInterface notA = new LogicNode(A.getUsedLiterals(), new HashMap<String, Boolean>(), LogicNodeType.NOT, false, null, newChildrenNotA);
			
			// !B
			LogicNodeSetInterface newChildrenNotB = new LogicNodeSet();
			newChildrenNotB.add(B);
			LogicNodeInterface notB = new LogicNode(B.getUsedLiterals(), new HashMap<String, Boolean>(), LogicNodeType.NOT, false, null, newChildrenNotB);
			
			// A | !B
			Set<String> newUsedLiteralsOr1 = new TreeSet<String>();
			LogicNodeSetInterface newChildrenOr1 = new LogicNodeSet();
			newChildrenOr1.add(A);
			newChildrenOr1.add(notB);
			newUsedLiteralsOr1.addAll(A.getUsedLiterals());
			newUsedLiteralsOr1.addAll(notB.getUsedLiterals());
			LogicNodeInterface or1 = new LogicNode(newUsedLiteralsOr1, new HashMap<String, Boolean>(), LogicNodeType.OPERATOR, false, null, newChildrenOr1);
			
			// !A | B
			Set<String> newUsedLiteralsOr2 = new TreeSet<String>();
			LogicNodeSetInterface newChildrenOr2 = new LogicNodeSet();
			newChildrenOr2.add(notA);
			newChildrenOr2.add(B);
			newUsedLiteralsOr2.addAll(notA.getUsedLiterals());
			newUsedLiteralsOr2.addAll(B.getUsedLiterals());
			LogicNodeInterface or2 = new LogicNode(newUsedLiteralsOr2, new HashMap<String, Boolean>(), LogicNodeType.OPERATOR, false, null, newChildrenOr2);
			
			// (A | !B) & (!A | B)
			Set<String> newUsedLiteralsRet = new TreeSet<String>();
			LogicNodeSetInterface newChildrenRet = new LogicNodeSet();
			newChildrenRet.add(or1);
			newChildrenRet.add(or2);
			newUsedLiteralsRet.addAll(or1.getUsedLiterals());
			newUsedLiteralsRet.addAll(or2.getUsedLiterals());
			return new LogicNode(newUsedLiteralsRet, new HashMap<String, Boolean>(), LogicNodeType.OPERATOR, true, null, newChildrenRet);
		}
	}
}
