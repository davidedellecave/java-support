package ddc.support.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;


public class LRangeTest {

//	@Test
//	public void testLRangeLongLong() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testLRangeZonedDateTimeZonedDateTime() {
//		fail("Not yet implemented");
//	}

	@Test
    public void consolidate() {
        boolean ONLINE=true;
        boolean OFFLINE=false;
        List<LRange> tsList = new LinkedList<>();
        tsList.add(buildTS("2020-05-12 18:30","2020-05-12 19:30", ONLINE));
        tsList.add(buildTS("2020-05-12 08:30","2020-05-12 03:30", ONLINE));
        tsList.add(buildTS("2020-05-12 08:30","2020-05-12 08:30", ONLINE));

        LRange ex = buildTS("2020-05-12 08:30","2020-05-12 08:30", ONLINE);
//        List<LRange> out = LRange.explode(tsList, ex);

//        out.stream().forEach(x -> System.out.println(x));
    }

    private LRange buildTS(String d1, String d2, boolean online) {
        LocalDateTime start = LocalDateTime.parse(d1, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime end = LocalDateTime.parse(d2, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        //Timeslice ts = new Timeslice(start, end);
//        LRange ts = new LRange(start, end);
        LRange ts = null;
        //ts.setOnLine(online);
        return ts;
    }
}
