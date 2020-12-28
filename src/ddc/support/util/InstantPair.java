package ddc.support.util;

import java.time.Instant;

public class InstantPair {

	private Instant lower = Instant.MIN;
	private Instant upper = Instant.MAX;

	public InstantPair(Instant lower, Instant upper) {
		super();
		this.lower = lower;
		this.upper = upper;
	}

	public InstantPair() {
	}

	public InstantRange toRange() {
		return new InstantRange(lower, upper);
	}

	public String getLower() {
		return lower.toString();
	}

	public void setLower(String isoLocalDateTimer) {
		lower = Instant.parse(isoLocalDateTimer);
	}

	public String getUpper() {
		return upper.toString();
	}

	public void setUpper(String isoLocalDateTimer) {
		upper = Instant.parse(isoLocalDateTimer);
	}

	@Override
	public String toString() {
		return " (" + lower + "," + upper + ")";
	}
}
