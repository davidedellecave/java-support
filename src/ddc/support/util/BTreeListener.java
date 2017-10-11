package ddc.support.util;

public interface BTreeListener<T> {
	public boolean visit(BTree<T> node);
}
