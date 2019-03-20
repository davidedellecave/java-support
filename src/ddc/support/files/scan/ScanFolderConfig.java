package ddc.support.files.scan;

import java.nio.file.Path;

public class ScanFolderConfig {
	private Path rootFolder = null;
	private boolean isRecursive = true;
	private long sleepMillis = 10;
//	private IOFileFilter dirFilter = TrueFileFilter.TRUE;
//	private IOFileFilter fileFilter = TrueFileFilter.TRUE;
	private boolean continueToHandleFileOnError = false;
	private boolean zipEnabled = false;

	public Path getRootFolder() {
		return rootFolder;
	}

	public void setRootFolder(Path rootFolder) {
		this.rootFolder = rootFolder;
	}

	public boolean isRecursive() {
		return isRecursive;
	}

	public void setRecursive(boolean isRecursive) {
		this.isRecursive = isRecursive;
	}

	public long getSleepMillis() {
		return sleepMillis;
	}

	public void setSleepMillis(long sleepMillis) {
		this.sleepMillis = sleepMillis;
	}


	public boolean isContinueToHandleFileOnError() {
		return continueToHandleFileOnError;
	}

	public void setContinueToHandleFileOnError(boolean continueToHandleFileOnError) {
		this.continueToHandleFileOnError = continueToHandleFileOnError;
	}

	public boolean isZipEnabled() {
		return zipEnabled;
	}
	public void setZipEnabled(boolean zipEnabled) {
		this.zipEnabled = zipEnabled;
	}
}
