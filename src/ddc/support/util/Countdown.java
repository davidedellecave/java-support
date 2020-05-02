package ddc.support.util;

import java.time.Duration;

public class Countdown {
	private long countdown = 0;
	private long startTime = -1;

	public Countdown(Duration countdown) {
		this.countdown = countdown.toMillis();
		start();
	}
	
	public Countdown(long countdown) {
		this.countdown = countdown;
		start();
	}

	public long start() {
		startTime = getNowMillis();
		return startTime;
	}

	private static long getNowMillis() {
		return System.currentTimeMillis();
	}

	public boolean isOver() {
		return getElapsed() >= countdown;
	}

	private long getElapsed() {
		return startTime == -1 ? 0 : getNowMillis() - startTime;
	}
}
