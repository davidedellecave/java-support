package ddc.support.jmail;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Properties;

public class JavaMail {
    public void send(JavaMailConf c) throws MessagingException, UnsupportedEncodingException {
        Session session = buildSession(c);
        session.setDebug(c.isDebugEnabled());
        sendEmail(session, c.getFrom(), c.getReceipient(), c.getSubject(), c.getBody());
    }

    private void sendEmail(Session session, String from, String toEmail, String subject, String body) throws MessagingException, UnsupportedEncodingException {
        MimeMessage msg = new MimeMessage(session);
        //set message headers
        msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
        msg.addHeader("format", "flowed");
        msg.addHeader("Content-Transfer-Encoding", "8bit");
        msg.setFrom(new InternetAddress(from, "no-reply"));
        msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));
        msg.setSubject(subject, "UTF-8");
        msg.setText(body, "UTF-8");
        msg.setSentDate(Date.from(ZonedDateTime.now().toInstant()));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
        Transport.send(msg);
    }

    private Session buildSession(JavaMailConf c) {
        Properties props = new Properties();
        props.put("mail.smtp.host", c.getSmtpHost());
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        if (c.isSslEnabled()) {
            props.put("mail.smtp.ssl.enable", "true");
        } else if (c.isTlsEnabled()) {
            props.put("mail.smtp.starttls.enable", "true");
        }
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                c.getUsername(), c.getPassword());
                    }
                });
        return session;
    }
}
