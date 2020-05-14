package ddc.support.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DTRange {
//	public static final DTRange EMPTY = new DTRange(ZonedDateTime. LocalDate.of(1900, 1, 1), LocalDate.of(1900, 1, 1));

	private ZonedDateTime begin;

	public ZonedDateTime getBegin() {
		return begin;
	}

	public ZonedDateTime getEnd() {
		return end;
	}

	private ZonedDateTime end;

	public DTRange(ZonedDateTime begin, ZonedDateTime end) {
		super();
		this.begin = begin;
		this.end = end;
	}

	private LocalDateTime firstDayOfMonth(Month month) {
		LocalDate d = LocalDate.of(LocalDate.now().getYear(), month, 1);
		return LocalDateTime.of(d, LocalTime.MIN);
	}

	private LocalDateTime firstDayOfMonth(int year, Month month) {
		LocalDate d = LocalDate.of(year, month, 1);
		return LocalDateTime.of(d, LocalTime.MIN);
	}

	private LocalDateTime lastDayOfMonth(Month month) {
		LocalDate d = LocalDate.of(LocalDate.now().getYear(), month, month.maxLength());
		return LocalDateTime.of(d, LocalTime.MIN);
	}

	private LocalDateTime lastDayOfMonth(int year, Month month) {
		LocalDate d = LocalDate.of(year, month, month.maxLength());
		return LocalDateTime.of(d, LocalTime.MIN);
	}

	private ZonedDateTime dt(LocalDateTime d) {
		return ZonedDateTime.of(d, ZoneId.systemDefault());
	}

	public DTRange(ZonedDateTime day) {
		LocalDate date = LocalDate.of(day.getYear(), day.getMonth(), day.getDayOfMonth());
		this.begin = ZonedDateTime.of(date, LocalTime.MIN, ZoneId.systemDefault());
		this.end = ZonedDateTime.of(date, LocalTime.MAX, ZoneId.systemDefault());
	}

	public DTRange(Month month) {
		this.begin = dt(firstDayOfMonth(month));
		this.end = dt(lastDayOfMonth(month));
	}

	public DTRange(int year, Month month) {
		this.begin = dt(firstDayOfMonth(year, month));
		this.end = dt(lastDayOfMonth(year, month));
	}

	public LRange toLRange() {
		return LRange.create(begin.toInstant().toEpochMilli(), end.toInstant().toEpochMilli());
	}

	@Override
	public String toString() {
		return " (" + begin.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))  + ", " + end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")";
	}
}
