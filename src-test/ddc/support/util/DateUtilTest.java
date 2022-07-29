package ddc.support.util;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;


public class DateUtilTest {

	@Test
	public void testParseToDateStringString() throws ParseException {
		String dateToParse ="Wed, 16 Aug 2017 07:20:38 +0001";
		
		DateUtil.parseToDate(dateToParse, "EEE, d MMM yyyy HH:mm:ss Z");
		
	}
	
	
	@Test
	public void testDuration() throws ParseException {
//		Date date = DateUtil.parseToDate("2016/12/06", "yyyy/MM/dd");
//		12/01/2017
		
		LocalDateTime local = LocalDateTime.now();
		System.out.println("Local:" + local);
		
		ZonedDateTime romeZoned = DateUtil.fromLocalToRome(local);
		System.out.println("Rome Zoned:" + romeZoned);
		
		ZonedDateTime utcZoned = DateUtil.fromRomeToUTC(romeZoned);
		System.out.println("UTC Zoned:" + utcZoned);
		
		ZonedDateTime romeZoned2 = DateUtil.fromUTCToRome(utcZoned);
		System.out.println("Rome Zoned 2:" + romeZoned2);
		
		LocalDateTime local2 = DateUtil.toLocal(romeZoned2);
		System.out.println("Rome Zoned 2:" + local2);
	}


}
