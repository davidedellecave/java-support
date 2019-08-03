package ddc.support.util;

import org.junit.jupiter.api.Test;

class StringUtilTest {

	@Test
	void test() {
		String s ="¯õó01/02/2013 CLT CONFERMA TUTTI I DATI A VIDEO. CHIAMATA CAMPANIA OK. PROCEDO CON ATTIVAZIONE CARTA AGONHO9 C&amp;C 02/     01/2017 AGONHAK NUOVA PATENTE NA7102504N RILASCIATO IL 31/05/2016 SCADENZA IL 09/02/2017                                +++++IDENTIFICATO CLT CON PAU N U1V5699991K EMESSA IL 02/02/2017 DA UCO X ATTIVAZIONE CARTA. AVERTO 10419";
		
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
