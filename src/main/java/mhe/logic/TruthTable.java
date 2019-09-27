package mhe.logic;

import java.util.Map;

public interface TruthTable extends LogicFunction {
	public Boolean getResult(Integer position);
	public Boolean getResult(Map<String, Boolean> values);
	
	public Map<Boolean, Integer> getDistribution();
	public Integer getRowsCount();
	public Double getEntropy();
	public Double getAverage();
	public Boolean isLeaf();
	public Boolean getLeafValue();
}
