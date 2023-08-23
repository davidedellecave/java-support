package ddc.support.jack;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import ddc.support.util.CsvUtil;

class JackUtilTest {

//	@Test
	void testToMap() {
		Map<String, Object> m = JackUtil.toMap(new test());
		System.out.println(m);
		String line = CsvUtil.getLine(m, ';');
		System.out.println(line);
	}
	
	
	private void setCommon(Map log, String eventAction) {
		log.put("log.level", "INFO");
		log.put("event.action", eventAction);
		log.put("@timestamp", "2022-03-23 13:30:49,110");		
		log.put("user.id", "6133709");
		log.put("session.id", "qDl8m2RYnDH1rKRLN66eHHW");
		log.put("ContrattoWeb", "000000862250001");
		log.put("client.ip", "37.159.130.218");
		log.put("Canale", "IH");
		log.put("Abi", "05034");
	}
	
	@Test
	void test2ToMap() throws JsonProcessingException {
		Map log = new LinkedHashMap();
		setCommon(log, "Login");
		log.put("Disp.id", "");
		log.put("Operazione", "SMASH");
		log.put("Operazione.desc", "java.lang.RuntimeException");
		log.put("error.type", "Alimentazione smash");
		log.put("error.message", "test");
		log.put("error.stack_trace", "java.lang.RuntimeException: test\n org.apache.logging.log4j.JsonTemplateLayoutDemo.main(JsonTemplateLayoutDemo.java:11)");
		
		System.out.println(JackUtil.toPrettifiedString(log));
		
		//INIZIALIZZAZIONE BONIFICO
		log = new LinkedHashMap<String, String>();
		setCommon(log, "INIZIALIZZAZIONE_BONIFICO");
		log.put("Disp.id", "");
		log.put("Operazione", "BONIFICO");
		log.put("Operazione.desc", "Il cliente atterra sulla pagina del bonifico");	
		log.put("param.SmashProduct", "BONIFICO_ISTANTANEO");
		log.put("param.Causale", "PAGAMENTO SICUREZZA");
		log.put("param.Iban", "IT69E0357601601010000370336");
		log.put("param.Valuta", "EUR");
		log.put("param.Importo", "999,00");
		log.put("param.NomeBeneficiario", "SCACIA ANNA CASELLA");
		log.put("param.NomeOrdinante", "JANDOLI VINCENZO");
		
		System.out.println(JackUtil.toPrettifiedString(log));		
		
		log = new LinkedHashMap<String, String>();
		setCommon(log, "AUTORIZZAZIONE");
		log.put("Disp.Id", "e5e31d7e-8d2d-4a90-ac36-7c487946882c");
		log.put("Operazione", "AUTORIZZAZIONE");
		log.put("Operazione.desc", "Scenario di sicurezza da applicare");		
		log.put("param.TipoScenarioDispositiva", "SCAD");
		log.put("param.TipoDispositivoDiSicurezza", "OTP HW");
		System.out.println(JackUtil.toPrettifiedString(log));		
		
		log = new LinkedHashMap<String, String>();
		setCommon(log, "FINALIZZAZIONE BONIFICO");
		log.put("Disp.id", "e5e31d7e-8d2d-4a90-ac36-7c487946882c");
		log.put("Operazione", "SMASH");
		log.put("Operazione.desc", "Feedback smash sull'operazione");		
		log.put("param.SmashProduct", "BONIFICO_ISTANTANEO");
		log.put("param.Successful", "true");
		log.put("param.Disp.Id", "21650296-ed09-461a-aaa1-ce64fd9b7166");
		System.out.println(JackUtil.toPrettifiedString(log));		
		
	}
	
	class log extends TreeMap {
		
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
