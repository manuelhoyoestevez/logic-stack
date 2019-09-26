package mhe.logic;

import java.util.List;

public interface TruthTable extends LogicFunction {
	public List<Boolean> getValues();
	public Boolean getResult(Integer position);
	public Boolean getLiteralValues(Integer position);
}
