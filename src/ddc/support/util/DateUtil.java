package ddc.support.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * @author davidedc, 30/ott/2010
 */
public class DateUtil {

    public static String DATE_PATTERN_ISO = "yyyy-MM-dd HH:mm:ss";
    public static String DATE_PATTERN_YMD = "yyyy-MM-dd";
    public static String DATE_PATTERN_YM = "yyyy-MM";
    public static ZoneId ZONE_ROME = ZoneId.of("Europe/Rome");
    public static ZoneId ZONE_UTC = ZoneId.of("UTC");
    public static ZoneId ZONE_DEFAULT = ZoneId.systemDefault();
    private static DateTimeFormatter FORMATTER_ISO_DATETIME_LOCAL = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static DateTimeFormatter FORMATTER_ISO_DATETIME = DateTimeFormatter.ISO_DATE_TIME;
    private static SimpleDateFormat dateISOFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat dateHumanReadableFormatter = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
    private static SimpleDateFormat dateFormatterForFile = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    private static SimpleDateFormat dateFormatterForTimestamp = new SimpleDateFormat("yyyyMMddHHmmss");

    // Date and Time Pattern Result
    // "yyyy.MM.dd G 'at' HH:mm:ss z" 2001.07.04 AD at 12:08:56 PDT
    // "EEE, MMM d, ''yy" Wed, Jul 4, '01
    // "h:mm a" 12:08 PM
    // "hh 'o''clock' a, zzzz" 12 o'clock PM, Pacific Daylight Time
    // "K:mm a, z" 0:08 PM, PDT
    // "yyyyy.MMMMM.dd GGG hh:mm aaa" 02001.July.04 AD 12:08 PM
    // "EEE, d MMM yyyy HH:mm:ss Z" Wed, 4 Jul 2001 12:08:56 -0700
    // "yyMMddHHmmssZ" 010704120856-0700
    // "yyyy-MM-dd'T'HH:mm:ss.SSSZ" 2001-07-04T12:08:56.235-0700
    // "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" 2001-07-04T12:08:56.235-07:00
    // "YYYY-'W'ww-u" 2001-W27-3
    // ---------------- Parse

    public static LocalDate parseToLocalDate(String formattedDate, String pattern) throws ParseException {
        return LocalDate.parse(formattedDate, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime parseToLocalDateTime(String formattedDateTime, String pattern) throws ParseException {
        return LocalDateTime.parse(formattedDateTime, DateTimeFormatter.ofPattern(pattern));
    }


    public static Instant parseToInstant(String formattedDate, String pattern) throws ParseException {
        return parseToDate(formattedDate, pattern).toInstant();
    }

    public static Instant parseToInstant(String formattedDate, String[] pattern) throws ParseException {
        ParseException exception = null;
        for (String p : pattern) {
            try {
                return parseToInstant(formattedDate, p);
            } catch (ParseException e) {
                exception = e;
            }
        }
        throw exception;
    }

    public static Date parseToDate(String formattedDate, String[] pattern) throws ParseException {
        ParseException exception = null;
        for (String p : pattern) {
            try {
                return parseToDate(formattedDate, p);
            } catch (ParseException e) {
                exception = e;
            }
        }
        throw exception;
    }

    public static Date parseToDate(String formattedDate, String pattern) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Date fromDate = formatter.parse(formattedDate);
        return fromDate;
    }

    public static String parseToString(String date, String[] fromPattern, String toPattern) throws ParseException {
        ParseException exception = null;
        for (String pattern : fromPattern) {
            try {
                return parseToString(date, pattern, toPattern);
            } catch (ParseException e) {
                exception = e;
            }
        }
        throw exception;
    }

    private static String parseToString(String formattedDate, String fromPattern, String toPattern) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(fromPattern);
        java.util.Date fromDate = formatter.parse(formattedDate);
        formatter = new SimpleDateFormat(toPattern);
        return formatter.format(fromDate);
    }


    public static ZonedDateTime fromLocalToRome(LocalDateTime local) {
        return local.atZone(ZONE_ROME);
    }

    public static ZonedDateTime fromRomeToUTC(ZonedDateTime zoned) {
        // from epoch
        Instant istant = zoned.toInstant();
        return istant.atZone(ZoneOffset.UTC);
    }

    public static ZonedDateTime fromUTCToRome(ZonedDateTime zoned) {
        // from epoch
        Instant istant = zoned.toInstant();
        return istant.atZone(ZONE_ROME);
    }

    // ---------------- format
    public static String format(LocalDate date, String pattern) {
        // "yyyy-MM-dd HH:mm:ss"
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(LocalDateTime date, String pattern) {
        // "yyyy-MM-dd HH:mm:ss"
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(ZonedDateTime date, String pattern) {
        // "yyyy-MM-dd HH:mm:ss"
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(long timestamp, String pattern) {
        Date d = new Date(timestamp);
        return format(d, pattern);
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    public static String formatDate(Date date, String pattern) {
        if (date == null)
            return "";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    public static String formatISO(Date date) {
        if (date == null)
            return "";
        return FORMATTER_ISO_DATETIME.format(date.toInstant());
        //return dateISOFormatter.format(date);
    }

    public static String formatISO(long millis) {
        ZonedDateTime zdt = toSystemDefault(millis);
        return FORMATTER_ISO_DATETIME.format(zdt);
    }

    public static String formatISOLocal(long millis) {
        ZonedDateTime zdt = toSystemDefault(millis);
        return FORMATTER_ISO_DATETIME_LOCAL.format(zdt);
    }

    public static String formatISO(ZonedDateTime zdt) {
        return FORMATTER_ISO_DATETIME.format(zdt);
    }


    public static String formatISO() {
        return formatISO(ZonedDateTime.now());
    }

    public static String formatForHuman(Date date) {
        if (date == null)
            return "";
        return dateHumanReadableFormatter.format(date);
    }

    public static String formatForHuman(Object date) {
        if (date == null)
            return "";
        return dateHumanReadableFormatter.format(date);
    }

    public static String formatForFilename(Object date) {
        if (date == null)
            return "";
        return dateFormatterForFile.format(date);
    }

    public static String formatForFilename(Date date) {
        if (date == null)
            return "";
        return dateFormatterForFile.format(date);
    }

    public static String formatForTimestamp(Date date) {
        if (date == null)
            return "";
        return dateFormatterForTimestamp.format(date);
    }

    public static boolean isDateIntoTimeRange(Date d, String lowerBoundTime, String upperboudTime) throws ParseException {
        long timeMillis = getHSMFromDateMillis(d);

        Date lowerDate = parseToDate(lowerBoundTime, "HH:mm:ss");
        long lowerBoundMillis = getHSMFromDateMillis(lowerDate);

        Date upperDate = parseToDate(upperboudTime, "HH:mm:ss");
        long upperBoundMillis = getHSMFromDateMillis(upperDate);

        return (lowerBoundMillis <= timeMillis && timeMillis <= upperBoundMillis);
    }

    // ---------------- Create
//	public static Date create(int year, int monthZeroBased, int day) {
//		GregorianCalendar c = new GregorianCalendar(year, monthZeroBased, day);
//		return c.getTime();
//	}
//
//	public static Date create(int year, int monthZeroBased, int day, int hour, int minute, int second) {
//		GregorianCalendar c = new GregorianCalendar(year, monthZeroBased, day, hour, minute, second);
//		return c.getTime();
//	}

//	public static Date createStartDay(int year, Month month, int day) {
//		GregorianCalendar c = new GregorianCalendar(year, month.getMonthValue(), day, 0, 0, 0);
//		return c.getTime();
//	}
//
//	public static Date createEndDay(int year, java.time.Month month, int day) {
//		GregorianCalendar c = new GregorianCalendar(year, month.getMonthValue(), day, 23, 59, 59);
//		return c.getTime();
//	}

    private static long getHSMFromDateMillis(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return (cal.get(Calendar.HOUR_OF_DAY) * 60 * 60 + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND)) * 1000;
    }

//	public static Instant toInstant(Date date) {
//		return date.toInstant();
//	}
//	

    public static ZonedDateTime toSystemDefault(long millis) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
    }

    public static ZonedDateTime toUTC(Instant instant) {
        return ZonedDateTime.ofInstant(instant, ZONE_UTC);
    }

    public static ZonedDateTime toUTC(long millis) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), ZONE_UTC);
    }

    public static ZonedDateTime toRome(Instant instant) {
        return ZonedDateTime.ofInstant(instant, ZONE_ROME);
    }

    public static ZonedDateTime toRome(long millis) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), ZONE_ROME);
    }

    public static LocalDateTime toLocal(ZonedDateTime zoned) {
        return zoned.toLocalDateTime();
    }

    public static long toMillis(Instant instant) {
        return instant.toEpochMilli();
    }

    public static long toMillis(LocalDateTime ldt) {
        ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();
    }

    public static long toMillis(ZonedDateTime zdt) {
        return zdt.toInstant().toEpochMilli();
    }

    public static Instant toInstant(LocalDateTime ldt) {
        return ldt.atZone(ZoneId.systemDefault()).toInstant();
    }

    public static LocalDateTime toLocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static LocalDate toLocalDate(Instant instant) {
        return LocalDate.ofInstant(instant, ZoneId.systemDefault());
    }

    public static LocalDateTime toLocalDateTime(long millis) {
        return toLocalDateTime(Instant.ofEpochMilli(millis));
    }

    public static LocalDate toLocalDate(long millis) {
        return toLocalDate(Instant.ofEpochMilli(millis));
    }

    public static Instant toInstant(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    public static long toMillis(LocalDate ld) {
        return ld.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static Instant toInstant(long millis) {
        return Instant.ofEpochMilli(millis);
    }

    public static boolean isBeforeNow(ZonedDateTime dt) {
        return dt.isBefore(ZonedDateTime.now());
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static Date LocalDateToDate(LocalDate ld) {
        return Date.from(toInstant(ld));
    }

    public static long diffToMillis(Instant t1, Instant t2) {
        return Duration.between(t1, t2).toMillis();
    }


}
