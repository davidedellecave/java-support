package ddc.support.crypto;

import ddc.support.task.TaskInfo;

public class CryptoResult extends TaskInfo {
	public String data;

	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer(256);
		buff.append(" data:[").append(data).append("]");
		buff.append(" exitCode:[").append(this.getExitCode().toString()).append("]");
		buff.append(" exitMessage:[").append(this.getException()).append("]");
		return buff.toString();
	}
}
