package ddc.support.util;

public class LogTimer {
    private LogListener logger = null;
    private Chronometer chron = new Chronometer();
    private long counter = 0;

    public LogTimer(Class<?> clazz) {
        logger = new LogConsole(clazz);
    }

    public void setLogger(LogListener log) {
        this.logger = log;
    }

    public void start(String message) {
        chron.start();
        counter = 0;
        logger.info(message + "...");
    }

    public void debug(String message) {
        logger.debug(message);
    }

    public void info(String message) {
        logger.info(message);
    }

    public void warn(String message) {
        logger.warn(message);
    }

    public void error(String message, Throwable t) {
        logger.error(message, t);
    }

    public void error(Throwable t) {
        logger.error(t);
    }

    public void running(String message) {
        running(message, -1);
    }

    public Chronometer getChron() {
        return chron;
    }

    public void running(String message, long countdownMillis) {
        counter++;
        if (chron.getCountdownMillis() == 0 && countdownMillis > 0) {
            chron.setCountdownMillis(countdownMillis);
        }
        if (chron.getCountdownMillis() > 0) {
            if (chron.isCountdownCycle()) {
                logger.info(message + " - counter:[" + counter + "] - elapsed:[" + chron.toString() + "]");
            }
        } else {
            logger.info(message + " - counter:[" + counter + "] - elapsed:[" + chron.toString() + "]");
        }
    }

    public void stop(String message) {
        chron.stop();
        logger.info(message + " - terminated - counter:[" + counter + "] - elapsed:[" + chron.toString() + "]");
    }

}
