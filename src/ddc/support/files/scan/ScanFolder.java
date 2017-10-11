package ddc.support.files.scan;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import ddc.support.util.Chronometer;
import ddc.support.util.LogListener;

public class ScanFolder {
	private boolean stop = false;
	private ScanFolderContext context = null;
	private LogListener logListener = null;

	public ScanFolder() {}
	
	public ScanFolder(LogListener logger) {
		this.logListener = logger;
	}

	public boolean isStopped() {
		return stop;
	}

	public void stop() {
		this.stop = true;
	}

	public ScanFolderContext getContext() {
		return context;
	}

	public void deepFirstScan(File rootFolder, boolean recursive, ScanFolderHandler scanHandler) throws Exception {
		ScanFolderConfig c = new ScanFolderConfig();
		c.setRootFolder(rootFolder);
		c.setRecursive(recursive);
		deepFirstScan(c, scanHandler);
	}

	public void deepFirstScan(ScanFolderConfig config, ScanFolderHandler scanHandler) throws Exception {
		if (scanHandler == null)
			return;
		context = new ScanFolderContext();
		context.setConfig(config);
		context.setStats(new ScanFolderStats());
		proxyStartScan(context, scanHandler);
		doDeepFirstTraversing(config.getRootFolder(), context, scanHandler, ScanResult.continueScan);
		proxyEndScan(context, scanHandler);
	}

	protected void sleep(ScanFolderConfig config) {
		if (config.getSleepMillis() > 0)
			Chronometer.sleep(config.getSleepMillis());
	}

	private static final String INFO_TITLE = "ScanFile - ";

	private void doDeepFirstTraversing(File folder, ScanFolderContext ctx, ScanFolderHandler scanHandler, ScanResult currentScanMode) throws Exception {
		if (currentScanMode == ScanResult.stopScan)
			return;
		if (!folder.canRead()) {
			if (logListener != null)
				logListener.error(INFO_TITLE + "cannot read folder:[" + folder + "]");
			return;
		}
		if (isStopped()) {
			if (logListener != null)
				logListener.info(INFO_TITLE + "stop requested - folder:[" + folder + "]");
			return;
		}
		// get files and subfolder of folder
		File[] subItems = folder.listFiles();
		ArrayList<File> files = new ArrayList<File>();
		ArrayList<File> subfolders = new ArrayList<File>();
		for (File item : subItems) {
			if (item.isDirectory())
				subfolders.add(item);
			if (item.isFile())
				files.add(item);
		}
		// deep first traversing
		for (File subFolder : subfolders) {
			doDeepFirstTraversing(subFolder, ctx, scanHandler, currentScanMode);
		}
		// context
		ctx.setFolder(folder);
		ctx.setSiblingFolder(subfolders);
		ctx.setSiblingFiles(files);
		// proxing the handler
		currentScanMode = proxyPreFolder(folder, ctx, scanHandler);
		if (currentScanMode == ScanResult.stopScan)
			return;
		if (currentScanMode == ScanResult.skipFolder)
			return;
		for (File file : files) {
			currentScanMode = proxyFile(file, ctx, scanHandler);
			if (currentScanMode == ScanResult.stopScan)
				return;
			if (currentScanMode == ScanResult.skipSibling)
				break;
		}
		currentScanMode = proxyPostFolder(folder, ctx, scanHandler);
	}

	public enum ScanResult {
		continueScan, stopScan, /* stopDeepFirstScan, stopBreathScan, */ skipFolder, skipSibling
	};

	private ScanResult proxyStartScan(ScanFolderContext ctx, ScanFolderHandler scanHandler) throws Exception {
		// logger.info("Start Scan");
		return scanHandler.startScan(ctx);
	}

	private ScanResult proxyEndScan(ScanFolderContext ctx, ScanFolderHandler scanHandler) throws Exception {
		// logger.info("End Scan");
		ScanFolderStats stats = ctx.getStats();

		String sizeProcessed = FileUtils.byteCountToDisplaySize(stats.getBytesProcessed());
		stats.setSizeProcessed(sizeProcessed);
		float throughput = 0;
		String throughputSize = "";
		if ((stats.getChron().getElapsed() / 1000) > 0) {
			throughput = stats.getBytesProcessed() / (stats.getChron().getElapsed() / 1000);
			throughputSize = FileUtils.byteCountToDisplaySize((long) throughput) + "/sec";
			stats.setThroughput(throughput);
			stats.setThroughputSize(throughputSize);
		}
		return scanHandler.endScan(ctx);
	}

	public static String readableFileSize(long size) {
		if (size <= 0)
			return "0";
		final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	private ScanResult proxyPreFolder(File folder, ScanFolderContext ctx, ScanFolderHandler scanHandler) throws Exception {
		// logger.info("PRE Folder >>>>>>>>>>>>>>>>>>>" + folder + "
		// <<<<<<<<<<<<<<<<<<<");
		ScanFolderStats stats = ctx.getStats();
		ScanFolderConfig conf = ctx.getConfig();
		stats.incFolderScanned();
		if (conf.getDirFilter().accept(folder)) {
			stats.incFolderProcessed();
			return scanHandler.preHandleFolder(folder, ctx);
		} else {
			return ScanResult.skipFolder;
		}
	}

	private ScanResult proxyPostFolder(File folder, ScanFolderContext ctx, ScanFolderHandler scanHandler) throws Exception {
		// logger.info("POST folder >>>>>>>>>>>>>>>>>>>" + folder + "
		// <<<<<<<<<<<<<<<<<<<");
		return scanHandler.postHandleFolder(folder, ctx);
	}

	private ScanResult proxyFile(File file, ScanFolderContext ctx, ScanFolderHandler scanHandler) throws Exception {
		// logger.info(ctx.getFolder().getPath() + " > "+ file.getName());
		ScanFolderStats stats = ctx.getStats();
		ScanFolderConfig conf = ctx.getConfig();
		stats.incFileScanned();
		if (conf.getFileFilter().accept(file)) {
			stats.incFileProcessed();
			stats.incBytesProcessed(file.length());
			return scanHandler.handleFile(file, ctx);
		}
		return ScanResult.continueScan;
	}
}
