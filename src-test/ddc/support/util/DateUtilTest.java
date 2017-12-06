package ddc.support.util;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.Date;

import org.junit.Test;

public class DateUtilTest {

	@Test
	public void testParseToDateStringString() throws ParseException {
		String dateToParse ="Wed, 16 Aug 2017 07:20:38 +0001";
		
		DateUtil.parseToDate(dateToParse, "EEE, d MMM yyyy HH:mm:ss Z");
		
	}
	
	
	@Test
	public void testDuration() throws ParseException {
		Date date = DateUtil.parseToDate("2016/12/06", "yyyy/MM/dd");
//		12/01/2017
		
		ZonedDateTime d = DateUtil.toZonedDateTime(date.getTime());
		System.out.println(d);
		d=d.plusMonths(11);
		System.out.println("-----");
		System.out.println(d);
		
	}


}
