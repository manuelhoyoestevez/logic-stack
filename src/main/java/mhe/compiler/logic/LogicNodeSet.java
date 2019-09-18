package mhe.compiler.logic;

import java.util.*;

@SuppressWarnings("serial")
public class LogicNodeSet extends TreeSet<LogicNodeInterface> implements LogicNodeSetInterface {
	
	public LogicNodeSet(){
		super();
	}
	
	public LogicNodeSet(LogicNodeInterface node){
		this();
		this.add(node);
	}

	@Override
	public boolean add(LogicNodeInterface e) {
		boolean ret = true;
		
		for(LogicNodeInterface c : this) {
			if(e.equivalent(c)){
				ret = false;
				break;
			}
		}
		
		if(ret) {
			ret = super.add(e);
		}
		
		return ret;
	}

	@Override
	public boolean addAll(Collection<? extends LogicNodeInterface> c) {
		boolean ret = false;
		
		for(LogicNodeInterface n : c) {
			ret = ret || this.add(n);
		}
		
		return ret;
	}
}
