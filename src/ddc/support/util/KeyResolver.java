package ddc.support.util;

public interface KeyResolver<K, V> {
	public K resolve(V item);
}
