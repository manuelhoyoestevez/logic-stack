package mhe.logic.truthtable;

import java.util.Set;
import java.util.TreeSet;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap;
import java.util.Collection;

public class ArrayMap implements Map<Integer, Boolean>{
	
	private List<Boolean> arrayList;
	
	public ArrayMap(List<Boolean> arrayList) {
		this.arrayList = arrayList;
	}
	
	@Override
	public int size() {
		return this.arrayList.size();
	}
	
	@Override
	public Boolean get(Object key) {
		return this.arrayList.get((Integer) key);
	}

	@Override
	public Set<Entry<Integer, Boolean>> entrySet() {
		Set<Entry<Integer, Boolean>> ret = new TreeSet<Entry<Integer, Boolean>>();
		for(int i = 0; i < this.arrayList.size(); i++) {
			ret.add(new AbstractMap.SimpleEntry<Integer, Boolean>(i, this.arrayList.get(i)));
		}
		return ret;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub	
	}

	@Override
	public boolean containsKey(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsValue(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<Integer> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean put(Integer arg0, Boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putAll(Map<? extends Integer, ? extends Boolean> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean remove(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Boolean> values() {
		// TODO Auto-generated method stub
		return null;
	}
}
