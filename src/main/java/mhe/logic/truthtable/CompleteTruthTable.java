package mhe.logic.truthtable;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import mhe.logic.TruthTable;

public class CompleteTruthTable extends AbstractTruthTable {
	private List<Boolean> values;
	
	public CompleteTruthTable(List<String> literals, List<Boolean> values) {
		super(literals);
		this.values = values;

		for(int i = 0; i < values.size(); i++) {
			this.addValue(position2map(i, this.getReversedLiterals()), values.get(i));
		}
		
		this.setBranchLiteral();
	}
	
	public List<Boolean> getValues() {
		return this.values;
	}
	
	@Override
	public Integer getRowsCount() {
		return this.getValues().size();
	}
	
	@Override
	public Boolean getResult(Integer position) {
		if(this.values.size() >= position) {
			return null;
		}

		return this.getValues().get(position);
	}
	
	@Override
	public TruthTable reduceBy(Map<String, Boolean> values) {
		Set<String> removedLiterals = values.keySet();
		List<String> newLiterals = new ArrayList<String>();
		List<Boolean> newValues = new ArrayList<Boolean>();
		
		for(String lit : this.getLiterals()) {
			if(!removedLiterals.contains(lit)) {
				newLiterals.add(lit);
			}
		}
		
		for(int i = 0; i < this.getValues().size(); i++) {
			Map<String, Boolean> auxValues = position2map(i, this.getReversedLiterals());
			
			if(subset(auxValues, values)) {
				newValues.add(this.getValues().get(i));
			}
		}
		
		return new CompleteTruthTable(newLiterals, newValues);
	}
	
	@Override
	public String toString() {
		String ret = "L = " 
				+ this.getRowsCount() + ", D: [ 0: " 
				+ this.getDistribution().get(false) + " | 1: " 
				+ this.getDistribution().get(true) + " ], E = " 
				+ this.getEntropy() + ", A = " 
				+ this.getAverage() + ", F: ";
		
		if(this.isLeaf()) {
			ret+= "YES (" + this.getLeafValue() + ")";
		}
		else {
			ret+= "NO";
		}
		
		ret+= ". X = " + this.getBranchLiteral() + "\r\n";
		
		for(String literal : this.getLiterals()) {
			ret+= this.getLiteralPartition().get(literal).toString() + "\r\n";
		}
		
		ret+= "\r\n";
		
		for(String literal : this.getLiterals()) {
			ret+= literal + "|";
		}
		
		ret+= "\r\n";

		for(int i = 0; i < this.getRowsCount(); i++) {
			Map<String, Boolean> map = position2map(i, this.getReversedLiterals());
			for(String literal : this.getLiterals()) {
				ret += (map.get(literal) ? "1" : "0") + "|";
			}
			
			ret+= (this.getValues().get(i) ? "1" : "0") + "\r\n";
		}

		return ret;
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject toJson() {
		JSONObject ret = new JSONObject();
		ret.put("literals", this.getLiterals());
		ret.put("values", this.getValues());
		return ret;
	}
	
	public static void main(String[] args) {
		List<String> literals = new ArrayList<String>();
		literals.add("a");
		literals.add("b");
		literals.add("c");
		literals.add("d");

		List<Boolean> values = new ArrayList<Boolean>();
		values.add(false);
		values.add(true);
		values.add(true);
		values.add(false);
		values.add(false);
		values.add(true);
		values.add(false);
		values.add(true);
		values.add(false);
		values.add(false);
		values.add(true);
		values.add(false);
		values.add(true);
		values.add(true);
		values.add(false);
		values.add(false);

		CompleteTruthTable table = new CompleteTruthTable(literals, values);
		
		System.out.println(table.toJson().toString());
		System.out.println(table.toString());
		
		try {
			Map<String, Boolean> reduction0 = string2Values("{\"a\":false}");
			Map<String, Boolean> reduction1 = string2Values("{\"a\":true}");
			
			TruthTable subtable0 = table.reduceBy(reduction0);
			System.out.println(subtable0.toJson().toString());
			System.out.println(subtable0.toString());
			
			TruthTable subtable1 = table.reduceBy(reduction1);
			System.out.println(subtable1.toJson().toString());
			System.out.println(subtable1.toString());
			
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
