package mhe.compiler.logic;

import java.util.*;

public interface LogicFunctionInterface {
	public String getExpression();
	
	public Boolean getValueFrom(Map<String, Boolean> values);
	
	public List<String> getAllLiterals();

	public Set<String> getUsedLiterals();
	
	public Map<String, Boolean> getAppliedReductions();
	
	public LogicFunctionInterface not();
	
	public LogicFunctionInterface reduceBy(String literal, Boolean value);
	
	public LogicFunctionInterface reduceBy(Map<String, Boolean> values);
	
	public LogicFunctionInterface setAllLiterals(List<String> literals);
}
