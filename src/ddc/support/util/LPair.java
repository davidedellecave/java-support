package ddc.support.util;

public class LPair {
	private long lower = Long.MIN_VALUE;
	private long upper = Long.MAX_VALUE;

	public LPair(long lower, long upper) {
		super();
		this.lower = lower;
		this.upper = upper;
	}

	public LPair() {
	}

	public LRange toRange() {
		return new LRange(lower, upper);
	}
	
	public long getLower() {
		return lower;
	}

	public void setLower(long lower) {
		this.lower = lower;
	}

	public long getUpper() {
		return upper;
	}

	public void setUpper(long upper) {
		this.upper = upper;
	}

}
