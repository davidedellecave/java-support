package ddc.support.util;

public interface LogListener {
	public void debug(Object message);
	public void info(Object message);
	public void warning(Object message);
	public void error(Object message, Throwable t);
	public void error(Object message);
}
