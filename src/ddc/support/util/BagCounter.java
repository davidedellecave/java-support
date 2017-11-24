package ddc.support.util;

import java.util.HashMap;
import java.util.Map;

public class BagCounter extends HashMap<String, long[]> {
	private static final long serialVersionUID = -2429799681638234210L;

	public void put(String key) {
		long[] valueWrapper = this.get(key);
		if (valueWrapper == null) {
			this.put(key, new long[] { 1 });
		} else {
			valueWrapper[0]++;
		}
	}

	public Map<String, Long> getMap() {
		Map<String, Long> m = new HashMap<>();
		for (Map.Entry<String, long[]> i : this.entrySet()) {
			m.put(i.getKey(), i.getValue()[0]);
		}
		return m;
	}
}
