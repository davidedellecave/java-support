package ddc.support.util;

import java.time.LocalDate;
import java.time.Month;

public class DateRange2 {
	public static final DateRange2 EMPTY = new DateRange2(LocalDate.of(1900, 1, 1), LocalDate.of(1900, 1, 1));

	private LocalDate begin;
	private LocalDate end;

	public DateRange2(LocalDate begin, LocalDate end) {
		this.begin = begin == null ? LocalDate.MIN : begin;
		this.end = end == null ? LocalDate.MAX : end;
	}

	public DateRange2(int startYear, Month startMonth, int startDay, int endYear, Month endMonth, int endDay) {
		begin = LocalDate.of(startYear, startMonth, startDay);
		end = LocalDate.of(endYear, endMonth, endDay);

	}

	public boolean isEmpty() {
		return begin.isEqual(end);
	}

	public boolean isNotEmpty() {
		return !isEmpty();
	}

	public boolean contains(LocalDate date) {
		return (begin.isBefore(date) || begin.isEqual(date)) && (end.isAfter(date) || end.isEqual(date));
	}

	public DateRange2 intersection(DateRange2 range) {
		LocalDate maxLower = max(range.begin, begin);
		LocalDate minUpper = min(range.end, end);
		if (maxLower.isBefore(minUpper)) {
			return new DateRange2(maxLower, minUpper);
		}
		return DateRange2.EMPTY;
	}

	public DateRange2 extend(DateRange2 range) {
		LocalDate minLower = min(range.begin, begin);
		LocalDate maxUpper = max(range.end, end);
		if (minLower.isBefore(maxUpper)) {
			return new DateRange2(minLower, maxUpper);
		}
		return DateRange2.EMPTY;
	}

	private LocalDate max(LocalDate p1, LocalDate p2) {
		return p1.isAfter(p2) ? p1 : p2;
	}

	private LocalDate min(LocalDate p1, LocalDate p2) {
		return p1.isBefore(p2) ? p1 : p2;
	}

	@Override
	public String toString() {
		return " (" + begin + "," + end + ")";
	}
}