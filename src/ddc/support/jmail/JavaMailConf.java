package ddc.support.jmail;

public class JavaMailConf {
    public static final int PORT=25;
    public static final int PORT_SSL=465;
    public static final int PORT_TLS=587;
    private String smtpHost;
    private String username;
    private String password;
    private boolean sslEnabled = false;
    private boolean tlsEnabled = false;
    private int port = PORT;
    private boolean debugEnabled = false;
    private String from;
    private String receipient;
    private String subject;
    private String body;

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSslEnabled() {
        return sslEnabled;
    }

    public void setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    public boolean isTlsEnabled() {
        return tlsEnabled;
    }

    public void setTlsEnabled(boolean tlsEnabled) {
        this.tlsEnabled = tlsEnabled;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getReceipient() {
        return receipient;
    }

    public void setReceipient(String receipient) {
        this.receipient = receipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
