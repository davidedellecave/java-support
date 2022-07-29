package ddc.support.util;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EzCache<T> {
	private Map<String, CacheItem<T>> cache = new HashMap<>();

	public synchronized T get(String name) {
		if (cache.containsKey(name)) {
			CacheItem<T> item = cache.get(name);
			if (item.countdown.isOver()) {
				cache.remove(name);
				return null;
			} else {
				return item.obj;
			}
		}
		return null;
	}

	public synchronized int size() {
		return cache.size();
	}
	
	public synchronized T get(String name, Renewal<T> renewal) throws Exception {
		T obj = get(name);
		if (obj == null) {
			obj = renewal.renew();
			put(name, obj, renewal.getEviction());
		}
		return obj;
	}

	public synchronized void put(String name, T obj, Duration eviction) {
		if (cache.containsKey(name)) {
			cache.remove(name);
		}
		CacheItem<T> item = new CacheItem<>();
		item.countdown = new Countdown(eviction);
		item.obj = obj;
		cache.put(name, item);
	}

	//check if is evicted and eventually remove it
	public synchronized boolean isEvicted(String name) {		
//		if (cache.containsKey(name)) {
//			CacheItem item = cache.get(name);
//			return item.countdown.isOver();
//		}
		return get(name)==null;
	}
	
	//check if is it evicted and eventually remove it	
	public synchronized boolean isValid(String name) {		
		return get(name)!=null;
	}

	/**
	 * Remove all evicted item 
	 */	
	public synchronized void purge() {
		Set<String> names = new  HashSet<>(cache.keySet());
		names.forEach(x -> get(x));
	}
	
	public synchronized void clear() {
		cache.clear();
	}

	class CacheItem<U> {
		public Countdown countdown;
		public U obj;
	}

	public interface Renewal<V> {
		public V renew() throws Exception;
		public Duration getEviction() throws Exception;
	}

}

