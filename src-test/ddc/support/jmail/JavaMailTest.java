package ddc.support.jmail;

import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;


class JavaMailTest {

    public void Test_SendMail_1() throws  UnsupportedEncodingException, MessagingException {

        JavaMailConf c = new JavaMailConf();
        c.setSmtpHost("*");
        c.setPort(JavaMailConf.PORT_SSL);
        //
        c.setUsername("*");
        c.setPassword("*");
        c.setSslEnabled(true);
        //
        c.setFrom("*");
        //
        c.setReceipient("*");
        c.setSubject("Test 1");
        c.setBody("Ciao davide");
        //
        c.setDebugEnabled(true);

        JavaMail m = new JavaMail();
        m.send(c);
        System.out.println("Mail Sent!!");
    }
}