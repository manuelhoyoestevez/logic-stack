package mhe.compiler.logic;

import java.util.*;

import mhe.graphviz.*;

public class LogicNode extends LogicFunction implements LogicNodeInterface {
	private static int serie = 0;
	private int serial;
	private boolean mode = false;
	private String name = null;
	private LogicNodeType type = null;
	private LogicNodeSetInterface children = null;
	
	public static String quotify(String str) {
		return "\"" + str + "\"";
	}
	
	public LogicNode(
			Set<String> usedLiterals, 
			Map<String, Boolean> reductions, 
			LogicNodeType type, 
			boolean mode, 
			String name, 
			LogicNodeSetInterface children
	) {
		super(usedLiterals, reductions);
		this.serial = ++serie;
		this.type = type;
		this.mode = mode;
		this.name = name;
		this.children = children;
	}
	
	public LogicNode(
			List<String> allLiterals, 
			Set<String>  usedLiterals, 
			Map<String, Boolean> reductions, 
			LogicNodeType type, 
			boolean mode, 
			String name, 
			LogicNodeSetInterface children
	) {
		this(usedLiterals, reductions, type, mode, name, children);
		this.setAllLiterals(allLiterals);
	}
	
	public LogicFunctionInterface setAllLiterals(List<String> literals) {
		super.setAllLiterals(literals);
		for(LogicNodeInterface child : this.getChildren()) {
			child.setAllLiterals(literals);
		}
		return this;
	}

	@Override
	public boolean getMode() {
		return this.mode;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public LogicNodeType getType() {
		return this.type;
	}
	
	@Override
	public SortedSet<LogicNodeInterface> getChildren() {
		return this.children;
	}
	
	@Override
	public boolean addChild(LogicNodeInterface child) {
		return this.children.add(child);
	}
	
	@Override
	public boolean isFinal() {
		return this.getChildren().isEmpty();
	}

	@Override
	public Boolean getValueFrom(Map<String, Boolean> values) {
		switch(this.getType()) {
			case LITERAL: 
				return values.get(this.getName());
			case NOT:
				for(LogicNodeInterface n : this.getChildren()) {
					return !n.getValueFrom(values);
				}
				return null;
				
			case OPERATOR:				
				for(LogicNodeInterface n : this.getChildren()) {
					if(n.getValueFrom(values) != mode) {
						return !mode;
					}
				}
				return mode;
		}
		return null;
	}

	@Override
	public int getSerial() {
		//return this.hashCode();
		return this.serial;
	}
	
	@Override
	public int compareTo(GraphVizNode gnode) {
		LogicNodeInterface node = (LogicNodeInterface) gnode;
		int ret = this.getExpression().compareTo(node.getExpression());

		if(ret == 0) {
			ret = this.hashCode() - gnode.hashCode();
		}

		return ret;
	}

	@Override
	public Collection<GraphVizLink> getLinks() {
		Collection<GraphVizLink> ret = new ArrayList<GraphVizLink>();
		
		for(LogicNodeInterface child : this.getChildren()) {
			ret.add(new GraphVizDefaultLink(this, child));
		}
		
		return ret;
	}
	
	@Override
	public boolean equivalent(LogicNodeInterface node) {
		return this.getExpression().compareTo(node.getExpression()) == 0;
	}

	@Override
	public String getShape() {
		switch(this.type) {
			case OPERATOR:
				return quotify(this.getChildren().isEmpty() ? "ellipse" : "ellipse");
			case LITERAL:
				return quotify("rectangle");
			case NOT:
				return quotify("ellipse");
		}
		return null;
	}

	@Override
	public String getLabel() {
		switch(this.type) {
			case OPERATOR:
				if(this.getChildren().isEmpty()) {
					if(this.getMode()) {
						return quotify("1"+ " " + this.getUsedLiterals().toString());
					}
					else {
						return quotify("0"+ " " + this.getUsedLiterals().toString());
					}
				}
				else {
					if(this.getMode()) {
						return quotify("&"+ " " + this.getUsedLiterals().toString());
					}
					else {
						return quotify("|"+ " " + this.getUsedLiterals().toString());
					}
				}
			case LITERAL:
				return quotify(this.getName());
			case NOT:
				return quotify("!"+ " " + this.getUsedLiterals().toString());
		}
		return null;
	}

	@Override
	public String getColor() {
		switch(this.type) {
			case OPERATOR:
				if(this.getChildren().isEmpty()) {
					if(this.getMode()) {
						return quotify("blue");
					}
					else {
						return quotify("red");
					}
				}
				else {
					if(this.getMode()) {
						return quotify("green");
					}
					else {
						return quotify("purple");
					}
				}
			case LITERAL:
				return quotify("black");
			case NOT:
				return quotify("orange");
		}
		return null;
	}
	
	@Override
	public LogicNode not() {
		switch(this.getType()) {
			case NOT:
				return (LogicNode) this.getChildren().first();
			
			case LITERAL:
				return new LogicNode(this.getAllLiterals(), this.getUsedLiterals(), new HashMap<String, Boolean>(), LogicNodeType.NOT, false, null, new LogicNodeSet(this));
				
			case OPERATOR:
				LogicNodeSetInterface newChildren = new LogicNodeSet();
				
				for(LogicNodeInterface child : this.getChildren()) {
					newChildren.add(
							new LogicNode(
									child.getAllLiterals(),
									child.getUsedLiterals(),
									new HashMap<String, Boolean>(),
									LogicNodeType.NOT,
									false,
									null,
									new LogicNodeSet(child)
							)
					); 
				}

				return new LogicNode(this.getAllLiterals(), this.getUsedLiterals(), new HashMap<String, Boolean>(), LogicNodeType.OPERATOR, !this.getMode(), null, newChildren);
		}
		return this;
	}

	@Override
	public LogicNode reduceBy(Map<String, Boolean> values) {
		LogicNode ret = null, child;
		List<String> newLiterals = new ArrayList<String>();
		Set<String> newUsedLiterals = new TreeSet<String>();
		Map<String, Boolean> newReductions = new HashMap<String, Boolean>();
		LogicNodeSet newChildren = new LogicNodeSet();
		
		for(String lit : this.getAllLiterals()) {
			if(!values.containsKey(lit)) {
				newLiterals.add(lit);
			}
		}
		
		newReductions.putAll(values);
		newReductions.putAll(this.getAppliedReductions());
		
		switch(this.getType()) {
			case LITERAL:
				Boolean value = values.get(this.getName());
				if(value == null) {
					newUsedLiterals.add(this.getName());
					return new LogicNode(newLiterals, newUsedLiterals, newReductions, LogicNodeType.LITERAL, false, this.getName(), newChildren);
				}
				else {
					return new LogicNode(newLiterals, newUsedLiterals, newReductions, LogicNodeType.OPERATOR, value, null, newChildren);
				}
			case NOT:
				child = (LogicNode) this.getChildren().first();
				
				switch(child.getType()) {
					case LITERAL:
						Boolean childValue = values.get(child.getName());
						if(childValue == null) {
							newUsedLiterals.add(child.getName());
							LogicNode lit = new LogicNode(newLiterals, newUsedLiterals, newReductions, LogicNodeType.LITERAL, false, child.getName(), newChildren);
							return new LogicNode(newLiterals, newUsedLiterals, newReductions, LogicNodeType.NOT, false, null, new LogicNodeSet(lit));
						}
						else {
							return new LogicNode(newLiterals, newUsedLiterals, newReductions, LogicNodeType.OPERATOR, !childValue, null, newChildren);
						}
					default:
						return child.not().reduceBy(values);
				}
			case OPERATOR:
				for(LogicNodeInterface c : this.getChildren()) {
					child = (LogicNode) c.reduceBy(values);
					
					newUsedLiterals.addAll(child.getUsedLiterals());
					
					switch(child.getType()) {
						case LITERAL:
						case NOT:
							newChildren.add(child);
							break;
						case OPERATOR:
							if(this.getMode() == child.getMode()) {
								for(LogicNodeInterface d : child.getChildren()) {
									newChildren.add(d);
								}
							}
							else if(child.isFinal()){
								return child;
							}
							else {
								newChildren.add(child);
							}
							break;
					}
				}

				return newChildren.size() == 1 ? (LogicNode) newChildren.first() : new LogicNode(newLiterals, newUsedLiterals, newReductions, LogicNodeType.OPERATOR, this.getMode(), null, newChildren);
		}
		return ret;
	}

	@Override
	public String getExpression() {
		String ret = "";// + this.hashCode();
		switch(this.getType()) {
			case LITERAL: 
				ret += this.getName();
				break;
				
			case NOT:
				ret += "!" + this.getChildren().first().getExpression();
				break;
				
			case OPERATOR:
				switch(this.getChildren().size()) {
					case 0:
						ret += this.getMode() ? "1" : "0";
						break;
					case 1:
						ret += this.getChildren().first().getExpression();
						break;
					default:
						boolean f = true;
						
						ret += this.getMode() ? "[" : "{";

						for(LogicNodeInterface child: this.getChildren()) {
							if(f) {
								f = false;
							}
							else {
								ret += ",";
							}
							ret += child.getExpression();
						}
						ret += this.getMode() ? "]" : "}";
				}
				break;
		}
		return ret;
	}
}
