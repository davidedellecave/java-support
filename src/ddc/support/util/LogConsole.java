package ddc.support.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogConsole implements LogListener {
    private String classname = "";
    private Chronometer chron = new Chronometer();

    public LogConsole() {
    }

    public LogConsole(Class<?> clazz) {
        classname = " - " + clazz.getName();
    }


    public void debug(Object message) {
        out("DEBUG", message);
    }


    public void info(Object message) {
        out("INFO ", message);
    }


    public void warn(Object message) {
        out("WARN ", message);
    }


    public void error(Object message, Throwable t) {
        err("ERROR", message + " - " + t.getMessage());
    }


    public void error(Object message) {
        err("ERROR", message);
    }

    private void out(String level, Object message) {
        System.out.println(dateISOFormatter.format(new Date()) + " (" + chron.getElapsed() + " ms)" + " - " + level + classname + " - " + message);
        chron.start();
    }

    private void err(String level, Object message) {
        System.out.println(dateISOFormatter.format(new Date()) + " (" + chron.getElapsed() + " ms)" + " - " + level + classname + " - " + message);
        chron.start();
    }

    private static SimpleDateFormat dateISOFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
}