package ddc.support.files.scan;

import java.io.IOException;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ddc.support.util.Chronometer;
import ddc.support.util.FileUtil;
import ddc.support.util.LogListener;

public class ScanFolder {
	private boolean stop = false;
	private ScanFolderContext context = null;
	private LogListener logListener = null;
	private ScanFolderConfig config = new ScanFolderConfig();

	public ScanFolder() {
	}

	public ScanFolder(LogListener logger) {
		this.logListener = logger;
	}

	public ScanFolderConfig getConfig() {
		return config;
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

	public void deepFirstScan(Path rootFolder, boolean recursive, ScanFolderHandler scanHandler) throws Exception {
		config.setRootFolder(rootFolder);
		config.setRecursive(recursive);
		deepFirstScan(config, scanHandler);
	}

	public void deepFirstScan(Path rootFolder, boolean recursive, ScanFolderHandler scanHandler, boolean zipEnabled) throws Exception {
		ScanFolderConfig c = new ScanFolderConfig();
		c.setRootFolder(rootFolder);
		c.setRecursive(recursive);
		c.setZipEnabled(zipEnabled);
		deepFirstScan(c, scanHandler);
	}

	public void deepFirstScan(ScanFolderHandler scanHandler) throws Exception {
		if (scanHandler == null)
			scanHandler = new DefaultScanFolderHandler();
		context = new ScanFolderContext();
		context.setConfig(config);
		context.setStats(new ScanFolderStats());
		proxyStartScan(context, scanHandler);
		doDeepFirstTraversing(config.getRootFolder(), context, scanHandler, ScanResult.continueScan, config.isZipEnabled());
		proxyEndScan(context, scanHandler);
	}

	public void deepFirstScan(ScanFolderConfig config, ScanFolderHandler scanHandler) throws Exception {
		if (scanHandler == null)
			scanHandler = new DefaultScanFolderHandler();
		context = new ScanFolderContext();
		context.setConfig(config);
		context.setStats(new ScanFolderStats());
		proxyStartScan(context, scanHandler);
		doDeepFirstTraversing(config.getRootFolder(), context, scanHandler, ScanResult.continueScan, config.isZipEnabled());
		proxyEndScan(context, scanHandler);
	}

	protected void sleep(ScanFolderConfig config) {
		if (config.getSleepMillis() > 0)
			Chronometer.sleep(config.getSleepMillis());
	}

	private static final String INFO_TITLE = "ScanFile - ";

	public static List<Path> fileList(Path folder) {
		List<Path> fileNames = new ArrayList<>();
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(folder)) {
			for (Path path : directoryStream) {
				fileNames.add(path);
			}
		} catch (IOException ex) {
		}
		return fileNames;
	}

	private void doDeepFirstTraversing(Path folder, ScanFolderContext ctx, ScanFolderHandler scanHandler, ScanResult currentScanMode, boolean zipEnabled) throws Exception {
		if (currentScanMode == ScanResult.stopScan)
			return;

		if (!Files.isDirectory(folder)) {
			if (logListener != null)
				logListener.error(INFO_TITLE + "path is not a folder:[" + folder + "]");
			return;
		}
		if (!Files.isReadable(folder)) {
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
		List<Path> subItems = fileList(folder);
		ArrayList<Path> files = new ArrayList<Path>();
		ArrayList<Path> subfolders = new ArrayList<Path>();
		for (Path item : subItems) {
			if (Files.isDirectory(item))
				subfolders.add(item);
			if (!Files.isDirectory(item))
				files.add(item);
		}
		// deep first traversing
		if (config.isRecursive()) {
			for (Path subFolder : subfolders) {
				doDeepFirstTraversing(subFolder, ctx, scanHandler, currentScanMode, zipEnabled);
			}
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
		for (Path file : files) {
			if (isZipFile(file) && zipEnabled) {
				currentScanMode = handleZip(file, ctx, scanHandler, currentScanMode);
				if (currentScanMode == ScanResult.stopScan)
					return;
				if (currentScanMode == ScanResult.skipSibling)
					break;
			}
			currentScanMode = proxyFile(file, ctx, scanHandler);
			if (currentScanMode == ScanResult.stopScan)
				return;
			if (currentScanMode == ScanResult.skipSibling)
				break;
		}
		currentScanMode = proxyPostFolder(folder, ctx, scanHandler);
	}

	private ScanResult proxyStartScan(ScanFolderContext ctx, ScanFolderHandler scanHandler) throws Exception {
		// logger.info("Start Scan");
		return scanHandler.startScan(ctx);
	}

	private ScanResult proxyEndScan(ScanFolderContext ctx, ScanFolderHandler scanHandler) throws Exception {
		// logger.info("End Scan");
		ScanFolderStats stats = ctx.getStats();

		String sizeProcessed = FileUtil.byteCountToDisplaySize(stats.getBytesProcessed());
		stats.setSizeProcessed(sizeProcessed);
		float throughput = 0;
		String throughputSize = "";
		if ((stats.getChron().getElapsed() / 1000) > 0) {
			throughput = stats.getBytesProcessed() / (stats.getChron().getElapsed() / 1000);
			throughputSize = FileUtil.byteCountToDisplaySize((long) throughput) + "/sec";
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

	private ScanResult proxyPreFolder(Path folder, ScanFolderContext ctx, ScanFolderHandler scanHandler) throws Exception {
		ScanFolderConfig conf = ctx.getConfig();
		ScanFolderStats stats = ctx.getStats();
		try {
			stats.incFolderScanned();
			stats.incFolderProcessed();
			return scanHandler.preHandleFolder(folder, ctx);
		} catch (Exception e) {
			stats.getExceptions().add(e.getMessage());
			System.err.println(e);
			if (conf.isContinueToHandleFileOnError()) {
				return ScanResult.continueScan;
			} else {
				throw e;
			}
		}
	}

	private ScanResult proxyPostFolder(Path folder, ScanFolderContext ctx, ScanFolderHandler scanHandler) throws Exception {
		ScanFolderConfig conf = ctx.getConfig();
		ScanFolderStats stats = ctx.getStats();
		try {
			return scanHandler.postHandleFolder(folder, ctx);
		} catch (Exception e) {
			stats.getExceptions().add(e.getMessage());
			if (conf.isContinueToHandleFileOnError()) {
				return ScanResult.continueScan;
			} else {
				throw e;
			}
		}
	}

	private ScanResult proxyFile(Path file, ScanFolderContext ctx, ScanFolderHandler scanHandler) throws Exception {
		ScanFolderConfig conf = ctx.getConfig();
		ScanFolderStats stats = ctx.getStats();
		try {
			stats.incFileScanned();
			stats.incFileProcessed();
			stats.incBytesProcessed(Files.size(file));
			return scanHandler.handleFile(file, ctx);
		} catch (Exception e) {
			stats.getExceptions().add(e.getMessage());
			if (conf.isContinueToHandleFileOnError()) {
				return ScanResult.continueScan;
			} else {
				throw e;
			}
		}
	}

	// Handle zip file

	private static boolean isZipFile(Path file) {
		return file.getFileName().toString().toLowerCase().endsWith("zip");
	}

	private static FileSystem createZipFileSystem(Path path, boolean create) throws IOException {
		URI uri = URI.create("jar:" + path.toUri());
		final Map<String, String> env = new HashMap<>();
		if (create) {
			env.put("create", "true");
		}
		return FileSystems.newFileSystem(uri, env);
	}

	private ScanResult handleZip(Path file, ScanFolderContext ctx, ScanFolderHandler scanHandler, ScanResult currentScanMode) throws IOException {
		// create the file system
		try (FileSystem zipFileSystem = createZipFileSystem(file, false)) {

			final Path root = zipFileSystem.getPath("/");
			// walk the file tree and print out the directory and filenames
			Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					ScanResult scanMode = null;
					try {
						scanMode = proxyFile(file, ctx, scanHandler);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (scanMode == ScanResult.stopScan)
						return FileVisitResult.TERMINATE;
					if (scanMode == ScanResult.skipSibling)
						return FileVisitResult.SKIP_SUBTREE;
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					return FileVisitResult.CONTINUE;
				}
			});
		}
		return ScanResult.continueScan;
	}

}
