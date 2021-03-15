package mhe.logic.truthtable;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

class LiteralDistribution {
	private int total;
	private String literal;
	private Map<Boolean, Integer> zeros;
	private Map<Boolean, Integer> ones;
	private Map<Boolean, Integer> totals;
	private Map<Boolean, Map<Boolean, Integer>> subtotals; 
	
	public static final double log2 = Math.log(2);
	
	public static double log2(double number) {
		return Math.log(number) / log2;
	}
	
	public LiteralDistribution(String literal) {
		this.total     = 0;
		this.literal   = literal;
		this.zeros     = new HashMap<Boolean, Integer>();
		this.ones      = new HashMap<Boolean, Integer>();
		this.totals    = new HashMap<Boolean, Integer>();
		this.subtotals = new HashMap<Boolean, Map<Boolean, Integer>>(); 
		
		this.zeros.put(false, 0);
		this.zeros.put(true,  0);
		
		this.ones.put(false, 0);
		this.ones.put(true,  0);
		
		this.totals.put(false, 0);
		this.totals.put(true,  0);
		
		this.subtotals.put(false, zeros);
		this.subtotals.put(true,  ones);
	}
	
	public int getTotal() {
		return this.total;
	}

	public String getLiteral() {
		return this.literal;
	}

	public void addValue(Boolean litValue, Boolean varValue) {
		this.total++;
		
		Integer aux = this.totals.get(litValue);
		this.totals.put(litValue, ++aux);
		
		aux = this.subtotals.get(litValue).get(varValue);
		this.subtotals.get(litValue).put(varValue, ++aux);
	}
	
	public Double getEntropy() {
		Double r = 0.0;
		if(this.total > 0) {
			for(Entry<Boolean, Integer> entry : this.totals.entrySet()) {
				Double s = 0.0;
				Double i = (double) entry.getValue();
				
				if(i > 0.0) {
					for(Integer j : this.subtotals.get(entry.getKey()).values()) {
						if(j > 0) {
							Double P = ((double) j) / i;
							s += P * log2(P);
						}
					}
					r += s * i / ((double) this.total);
				}
			}
		}
		
		return r;
	}
	
	public String toString() {
		
		boolean e, f = true;
		
		String ret = "" + this.literal +  ": E = " + this.getEntropy() + ",\tT = " + this.total + ", D: { ";
		
		for(Entry<Boolean, Integer> entry1 : this.totals.entrySet()) {
			if(f) {
				f = false;
			}
			else {
				ret += ", ";
			}
			
			ret += "" + (entry1.getKey() ? "1" : "0") + ": " + entry1.getValue() + " [ ";
			
			e = true;
			
			for(Entry<Boolean, Integer> entry2 : this.subtotals.get(entry1.getKey()).entrySet()) {
				if(e) {
					e = false;
				}
				else {
					ret += " | ";
				}
				ret += "" + (entry2.getKey() ? "1" : "0") + ": " + entry2.getValue();
			}
			
			ret += " ]";
		}

		ret += " }";
		
		return ret;
	}
}
