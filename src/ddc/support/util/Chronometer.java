package ddc.support.util;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Chronometer {
	private long startFirstTime = getNowMillis();
	private long startTime = -1;
	private long endTime = -1;
	private long countdown = 0;
	private long countdownCounter = 0;

	public static Chronometer instance() {
		return new Chronometer();
	}

	public Chronometer(long startTime, long endTime) {
		if (startTime <= endTime) {
			this.startTime = startTime;
			this.endTime = endTime;
		}
	}

	public Chronometer() {
		start();
	}

	public void reset() {
		startTime = -1;
		endTime = -1;
	}

	public Chronometer(long countdown) {
		this.countdown = countdown;
		start();
	}


	public long start() {
		startTime = getNowMillis();
		endTime = -1;
		return startTime;
	}

	public void setCountdownMillis(long countdown) {
		this.countdown = countdown;
	}

	public long getCountdownMillis() {
		return this.countdown;
	}

	/**
	 * Return true if countdown is passed one time (from start) The chronometer is
	 * not stopped
	 * 
	 * @return
	 * @see isCountdownCycle to ask more than one time
	 */
	public boolean isCountdownOver() {
		if (getElapsed() >= countdown) {
			return true;
		}
		return false;
	}

	/**
	 * Return true if countdown period is passed from last call
	 * 
	 * @param millis
	 * @return
	 */
	public boolean isCountdownCycle() {
		long times = countdownCycle();
		if (times > countdownCounter) {
			countdownCounter = times;
			return true;
		}
		return false;
	}

	/**
	 * Return how many times the countdown period is passed
	 * 
	 * @return
	 */
	public long countdownCycle() {
		if (countdown > 0)
			return (long) (getElapsed() / countdown);
		else
			return 0;
	}

	/**
	 * Return the millis elapsed
	 * 
	 * @return
	 */
	public long getElapsed() {
		if (startTime == -1)
			return 0;
		// end time is -1 when the chron is not stopped
		if (endTime == -1)
			return getNowMillis() - startTime;
		return endTime - startTime;
	}

	public long getElapsed(int index) {
		if (0 <= index && index <= partialCounter) {
			return partial[index];
		} else {
			return getElapsed();
		}
	}

	private final static int CAPACITY = 10;
	long[] partial = new long[CAPACITY];
	int partialCounter = 0;

	public long stop() {
		endTime = getNowMillis();
		long elapsed = endTime - startTime;
		if (partialCounter < CAPACITY) {
			partial[partialCounter] = elapsed;
			partialCounter++;
		}
		return elapsed;
	}

	/**
	 * Get elapsed time from chronometer is created, without consider start/stop
	 * 
	 * @return
	 */
	public long getTotalElapsed() {
		return getNowMillis() - startFirstTime;
	}

	public String toString() {
		return getHumanReadable(getElapsed());
	}

	public static long getNowMillis() {
		return System.currentTimeMillis();
	}

	public static long get1January1970Millis() {
		return 1317427200;
	}

	public long getStartTime() {
		return startTime;
	}

	public Date getStartDate() {
		return new Date(startTime);
	}

	public Date getEndDate() {
		return new Date(endTime);
	}

	public long getEndTime() {
		return endTime;
	}

	public static void sleep(Duration duration) {
		if (duration.isZero()) return;
		sleep(duration.toMillis());
	}
	
	public static void sleep(long millis) {
		if (millis <= 0)
			return;
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			System.err.println("sleep() Exception:[" + e.getMessage() + "]");
		}
	}

	public static void sleepMinutes(int minutes) {
        sleep(Duration.of(minutes, ChronoUnit.MINUTES));
	}

	public <T> T chronIt(Callable<T> task) throws Exception {
		T call = null;
		start();
		call = task.call();
		stop();
		return call;
	}
	
	public static long getElapsed(long start, long end) {
		if (start <=0)
			return 0;
		if (end <=0)
			return 0;
		return end - start;
	}
	
	public static String getElapsedString(long start, long end) {
		return getHumanReadable(getElapsed(start, end));
	}
	
	public static String getHumanReadable(long millis) {
		if (millis < 0) {
			return String.valueOf(millis);
		}
		if (millis == 0) {
			return "0 ms";
		}
//		return DurationFormatUtils.formatDurationHMS(millis);
		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= TimeUnit.DAYS.toMillis(days);

		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);

		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);

		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
		millis -= TimeUnit.SECONDS.toMillis(seconds);

		StringBuilder sb = new StringBuilder(64);

		if (days > 0) {
			sb.append(days);
			sb.append(" days");
		}

		if (hours > 0 || (days > 0 && (minutes > 0 || seconds > 0))) {
			sb.append(" " + hours);
			sb.append(" h");
		}

		if (minutes > 0 || ((days > 0 || hours > 0) && seconds > 0)) {
			sb.append(" " + minutes);
			sb.append(" mins");
		}

		if (seconds > 0) {
			sb.append(" " + seconds);			
			if (millis > 0) {
				sb.append(".");
				sb.append(millis);
			}
			sb.append(" secs");
		} else if (millis > 0) {
			sb.append(millis);
			sb.append(" ms");
		}

		return (sb.toString().trim());
	}
}