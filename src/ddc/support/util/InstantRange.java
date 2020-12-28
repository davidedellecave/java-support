package ddc.support.util;

import java.time.Instant;

public class InstantRange {
	public static final InstantRange EMPTY = new InstantRange(Instant.MIN, Instant.MIN);

	private Instant lower = Instant.MIN;
	private Instant upper = Instant.MAX;

	public InstantRange() {
	}

	public InstantRange(Instant lower, Instant upper) {
		this.lower = lower;
		this.upper = upper;
	}

	public Instant getLower() {
		return lower;
	}

	public void setLower(Instant lower) {
		this.lower = lower;
	}

	public Instant getUpper() {
		return upper;
	}

	public void setUpper(Instant upper) {
		this.upper = upper;
	}

	public boolean isEmpty() {
		return lower.equals(upper);
	}

	public boolean isNotEmpty() {
		return !isEmpty();
	}

	public boolean contains(Instant instant) {
		return (lower.isBefore(instant) || lower.equals(instant)) && (upper.isAfter(instant) || upper.equals(instant));
	}

	public boolean contains(InstantRange range) {
		return (lower.isBefore(range.lower) || lower.equals(range.lower))
				&& (upper.isAfter(range.upper) || upper.equals(range.upper));
	}

	public InstantRange intersection(InstantRange range) {
		Instant maxLower = max(range.lower, lower);
		Instant minUpper = min(range.upper, upper);
		if (maxLower.isBefore(minUpper)) {
			return new InstantRange(maxLower, minUpper);
		}
		return InstantRange.EMPTY;
	}

	public InstantRange extend(InstantRange range) {
		Instant minLower = min(range.lower, lower);
		Instant maxUpper = max(range.upper, upper);
		if (minLower.isBefore(maxUpper)) {
			return new InstantRange(minLower, maxUpper);
		}
		return InstantRange.EMPTY;
	}

	private Instant max(Instant p1, Instant p2) {
		return p1.isAfter(p2) ? p1 : p2;
	}

	private Instant min(Instant p1, Instant p2) {
		return p1.isBefore(p2) ? p1 : p2;
	}

	@Override
	public String toString() {
		return " (" + lower + "," + upper + ")";
	}

}
