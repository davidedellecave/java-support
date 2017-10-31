package ddc.support.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUtil<K, V> {

	public Map<K, V> listToMap(List<V> list, KeyResolver<K, V> resolver) {
		Map<K, V> map = new HashMap<>();
		for (V v : list) {
			K k = resolver.resolve(v);
			if (!map.containsKey(k))
				map.put(k, v);
		}
		return map;
	}

	public Map<K, V> maxCluster(List<V> list, KeyResolver<K, V> resolver, Comparator<V> c) {
		Map<K, V> map = new HashMap<>();
		for (V v : list) {
			K k = resolver.resolve(v);
			if (map.containsKey(k)) {
				V value = map.get(k);
				if (c.compare(v, value) > 0) {
					map.remove(k);
					map.put(k, v);
				}
			} else {
				map.put(k, v);
			}
			map.put(resolver.resolve(v), v);
		}
		return map;
	}
	
	public Map<K, V> cluster(List<V> list, KeyResolver<K, V> resolver, ValueSelector<V> selector) {
		Map<K, V> map = new HashMap<>();
		for (V newValue : list) {
			K k = resolver.resolve(newValue);
			if (map.containsKey(k)) {
				V currentItem = map.get(k);
				V selected = selector.select(newValue, currentItem);
				if (selected!=currentItem) {
					map.remove(k);
					map.put(k, newValue);
				}
			} else {
				map.put(k, newValue);
			}
			map.put(resolver.resolve(newValue), newValue);
		}
		return map;
	}
}
