package mhe.compiler.logic.ast;

import mhe.compiler.ASTInterface;
import mhe.compiler.logic.*;
import mhe.graphviz.*;

import java.util.*;

/** Abstract Syntax Tree generic node for Regular Expressions Parser
 * @author Manuel Hoyo Estévez
 */
public abstract class AST implements ASTInterface, LogicASTConstants, GraphVizNode {
	/** Serial Counter */
	private static int s = 0;	
	/** Serial Number */
	private int k;
	/** Type */
	private int t;
	/** Value */
	private boolean v;
	/** Name */
	private String n;
	/** Children */
	private LinkedList<ASTInterface> children;
	
	public LinkedList<ASTInterface> getChildren(){
		return this.children;
	}
	
	public static String quotify(String str) {
		return "\"" + str + "\"";
	}
	
	public static final AST ASTzero    = new ASTconst(false);
	public static final AST ASTone     = new ASTconst(true);
	public static final AST ASTlambda  = new ASTlambda();
	
	public AST(int t, boolean v, String n){
		this.k = ++s;
		this.t = t;
		this.v = v;
		this.n = n;
		this.children = new LinkedList<ASTInterface>();
	}
	
	public static AST constant(boolean v) {
		return v ? ASTone : ASTzero;
	}
	
	@Override
	public LogicNodeInterface getLogicNode() {
		return null;
	}
	
	@Override
	public ASTInterface getFirstChild() {
		if(this.getChildren().isEmpty()) {
			return null;
		}
		else {
			return this.getChildren().getFirst();
		}
	}
	
	public ASTInterface getSecondChild() {
		if(this.getChildren().size() > 1) {
			return this.getChildren().get(1);
		}
		else {
			return null;
		}
	}
	
	@Override
	public int getSerial(){
		return this.k;
	}

	public int getType() {
		return this.t;
	}
	
	public boolean getValue(){
		return this.v;
	}
	
	public String getName(){
		return this.n;
	}
	
	public boolean isLambda(){
		return false;
	}
	
	@Override
	public Collection<GraphVizLink> getLinks() {
		Collection<GraphVizLink> ret = new ArrayList<GraphVizLink>();		
		
		for(ASTInterface e : this.getChildren()) {
			ret.add(new GraphVizDefaultLink(this,e));
		}
		this.hashCode();
		return ret;
	}
	
	@Override
	public int compareTo(GraphVizNode node){
		Integer x = this.getSerial();
		Integer y = node.getSerial();
		return x.compareTo(y);
	}
	
	@Override
	public String toString(){
		return this.toString(0);
	}
	
	private String toString(int p){
		String r = "";
		
		for(int i = 0;i < p;i++) {
			r += " ";
		}
		
		r += this.getLabel() + '\n';
		
		for(ASTInterface e : this.getChildren()) {
			r += ((AST)e).toString(p + 1);
		}
				
		return r;
	}
}
