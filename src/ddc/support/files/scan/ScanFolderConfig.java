package ddc.support.files.scan;

import java.nio.file.Path;

import ddc.support.util.LogListener;

public class ScanFolderConfig {
	private Path rootFolder = null;
	private boolean isRecursive = true;
	private long sleepMillis = 10;
	private boolean continueToHandleFileOnError = false;
	private boolean zipEnabled = false;
	private LogListener logListener = null;
	
	public Path getRootFolder() {
		return rootFolder;
	}

	public ScanFolderConfig setRootFolder(Path rootFolder) {
		this.rootFolder = rootFolder;
		return this;
	}

	public boolean isRecursive() {
		return isRecursive;
	}

	public ScanFolderConfig setRecursive(boolean isRecursive) {
		this.isRecursive = isRecursive;
		return this;
	}

	public long getSleepMillis() {
		return sleepMillis;
	}

	public ScanFolderConfig setSleepMillis(long sleepMillis) {
		this.sleepMillis = sleepMillis;
		return this;
	}


	public boolean isContinueToHandleFileOnError() {
		return continueToHandleFileOnError;
	}

	public ScanFolderConfig setContinueToHandleFileOnError(boolean continueToHandleFileOnError) {
		this.continueToHandleFileOnError = continueToHandleFileOnError;
		return this;
	}

	public boolean isZipEnabled() {
		return zipEnabled;
	}
	public ScanFolderConfig setZipEnabled(boolean zipEnabled) {
		this.zipEnabled = zipEnabled;
		return this;
	}

	public LogListener getLogListener() {
		return logListener;
	}

	public ScanFolderConfig setLogListener(LogListener logListener) {
		this.logListener = logListener;
		return this;
	}
	
}
