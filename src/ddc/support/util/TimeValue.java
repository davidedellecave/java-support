package ddc.support.util;

public class TimeValue<T> {
	private long timestamp = 0;
	private T value = null;

	public TimeValue(Long timestamp, T value) {
		this.timestamp = timestamp;
		this.value = value;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return timestamp + ", " + String.valueOf(value);
	}
}
