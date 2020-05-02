package ddc.support.util;

public interface StoppableThread extends Runnable {
	public boolean isStopRequested();
}
