package ddc.support.jack;

import java.time.LocalDateTime;
import java.util.Map;

import org.junit.jupiter.api.Test;

import ddc.support.util.CsvUtil;

class JackUtilTest {

	@Test
	void testToMap() {
		Map<String, Object> m = JackUtil.toMap(new test());
		System.out.println(m);
		String line = CsvUtil.getLine(m, ';');
		System.out.println(line);
	}
	
	
	class test {
		private String var1 = "value1";
		private LocalDateTime var2 = LocalDateTime.now();
		private long var3 = 23423;
		public String getVar1() {
			return var1;
		}
		public void setVar1(String var1) {
			this.var1 = var1;
		}
		public LocalDateTime getVar2() {
			return var2;
		}
		public void setVar2(LocalDateTime var2) {
			this.var2 = var2;
		}
		public long getVar3() {
			return var3;
		}
		public void setVar3(long var3) {
			this.var3 = var3;
		}
		
	}

}
