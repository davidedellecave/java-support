package ddc.support.util;

import org.junit.jupiter.api.Test;

class StringUtilTest {

	@Test
	void test() {
		String s ="";
		
		String t1 = StringUtil.removeChar(s, 32, 126);
		
//		StringUtila
		System.out.println(t1);
//		
//		String t2 = StringUtil.toCharset(s, StringUtil.ISO_8859_1,  StringUtil.UTF_8);
//		System.out.println(t2);
//		
//		String t3 = StringUtil.toCharset(s, StringUtil.UTF_8,  StringUtil.ISO_8859_1);
//		System.out.println(t3);
//		
//		String t4 = StringUtil.noAsciiCharsToUnicode(s);
//		System.out.println(t4);
//		
//		String t5 = StringUtil.toCharset(s, StringUtil.UTF_8,  StringUtil.UTF_8);
//		System.out.println(t5);
//		
//		String t6 = StringEscapeUtils.escapeJava(s);
//		System.out.println(t6);
}

}
