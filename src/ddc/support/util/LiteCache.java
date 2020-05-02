package ddc.support.util;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class LiteCache {
	private static Map<String, CacheItem> cache = new HashMap<>();

	public synchronized static Object get(String name) {
		if (cache.containsKey(name)) {
			CacheItem item = cache.get(name);
			if (item.countdown.isOver()) {
				cache.remove(name);
				return null;
			} else {
				return item.obj;
			}
		}
		return null;
	}

	public synchronized static Object get(String name, Renewal renewal) throws Exception {
		Object obj = null;
		if (cache.containsKey(name)) {
			CacheItem item = cache.get(name);
			if (item.countdown.isOver()) {
				cache.remove(name);
			} else {
				obj = item.obj;
			}
		}
		if (obj == null) {
			obj = renewal.renew();
			put(name, obj, renewal.getEviction().toMillis());
		}
		return obj;
	}

	private synchronized static void put(String name, Object obj, long evictionMillis) {
		CacheItem item = (new LiteCache()).new CacheItem();
		item.countdown = new Countdown(evictionMillis);
		item.obj = obj;
		cache.put(name, item);
	}

	public synchronized static boolean isEvicted(String name) {
		if (cache.containsKey(name)) {
			CacheItem item = cache.get(name);
			return item.countdown.isOver();
		}
		return true;
	}

	public synchronized static void clear() {
		cache.clear();
	}

	class CacheItem {
		public Countdown countdown;
		public Object obj;
	}

	public interface Renewal {
		public Object renew() throws Exception;
		public Duration getEviction() throws Exception;
	}
}
