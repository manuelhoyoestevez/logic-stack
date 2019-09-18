package mhe.compiler.logic;

import java.util.*;

public abstract class LogicFunction implements LogicFunctionInterface {
	private List<String> allLiterals;
	private Set<String> usedLiterals;
	private Map<String, Boolean> reductions;

	private LogicFunction setUsedLiterals(Set<String> usedLiterals) {
		this.usedLiterals = usedLiterals;
		return this;
	}
	
	private LogicFunction setAppliedReductions(Map<String, Boolean> reductions) {
		this.reductions = reductions;
		return this;
	}
	
	protected LogicFunction(Set<String> usedLiterals, Map<String, Boolean> reductions){
		this
		.setUsedLiterals(usedLiterals)
		.setAppliedReductions(reductions);
	}

	@Override
	public List<String> getAllLiterals() {
		return this.allLiterals;
	}
	
	@Override
	public LogicFunctionInterface setAllLiterals(List<String> literals) {
		this.allLiterals = literals;
		return this;
	}

	@Override
	public Set<String> getUsedLiterals() {
		return this.usedLiterals;
	}

	@Override
	public Map<String, Boolean> getAppliedReductions() {
		return this.reductions;
	}

	@Override
	public LogicFunctionInterface reduceBy(String literal, Boolean value) {
		Map<String, Boolean> values = new HashMap<String, Boolean>();
		values.put(literal, value);
		return this.reduceBy(values);
	}

}
