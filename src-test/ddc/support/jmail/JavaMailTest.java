package ddc.support.jmail;

import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;


class JavaMailTest {

    @Test
    public void Test_SendMail_1() throws MessagingException, UnsupportedEncodingException {
        JavaMailConf c = new JavaMailConf();
        c.setSmtpHost("smtp100.ext.armada.it");
        c.setPort(JavaMailConf.PORT_SSL);
        //
        c.setUsername("SMTP-BASIC-9476");
        c.setPassword("m877UsKn8s");
        c.setSslEnabled(true);
        //
        c.setFrom("info@medisportgottardo.it");
        //
        c.setReceipient("davide.dellecave@gmail.com");
        c.setSubject("Test 1");
        c.setBody("Ciao davide");
        //
        c.setDebugEnabled(true);

        JavaMail m = new JavaMail();
        m.send(c);
        System.out.println("Mail Sent!!");
    }
}