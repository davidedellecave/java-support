package ddc.support.util;

import static ddc.support.util.Chronometer.instance;

import org.junit.Test;

public class ChronometerTest {

	@Test
	public void testChron() throws Exception {
		Chronometer c = instance();
		String out = c.chronIt(() -> aMethod("pippo"));
		System.out.println(out);
		System.out.println(c);
		
	}
	
	
	private String aMethod(String echo) {
		Chronometer.sleep(5000);
		return " >" + echo;
	}

}
