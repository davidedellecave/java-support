package ddc.support.files.scan;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ddc.support.files.scan.ScanFolder.ScanResult;
import ddc.support.util.FileUtil;

public class ScanFolderUtil {

	public static List<Path> getFiles(Path folder, boolean recursive, final String[] includeExtension, final String[] excludeExtension) throws Exception {
		return getFiles(folder, recursive, -1, includeExtension, excludeExtension);
	}

	public static List<Path> getFiles(Path folder, boolean recursive, long olderThanMillis, final String[] includeExtension, final String[] excludeExtension)
			throws Exception {
		if (!folder.toFile().isDirectory()) {
			return Collections.emptyList();
		}
		
		ScanFolder s = new ScanFolder();
		final List<Path> list = new ArrayList<>();
		s.deepFirstScan(folder.toFile(), recursive, new BaseScanFolderHandler() {
			@Override
			public ScanResult handleFile(File file, ScanFolderContext ctx) {
				boolean toAdd = false;
				
				if (olderThanMillis>0 && FileUtil.isNewerThan(file, olderThanMillis)) {
					return ScanResult.continueScan;
				}
				
				if (includeExtension.length > 0) {
					for (String ext : includeExtension) {
						if (file.getName().endsWith(ext))
							toAdd = true;
					}				
				} else {
					toAdd=true;
				}
				
				if (excludeExtension.length > 0) {
					for (String ext : excludeExtension) {
						if (file.getName().endsWith(ext))
							toAdd = false;
					}				
				}

				if (toAdd)
					list.add(file.toPath());
				return ScanResult.continueScan;
			}
		});
		return list;
	}

	public static List<Path> getFiles(Path folder) throws Exception {
		if (!folder.toFile().isDirectory()) {
			return Collections.emptyList();
		}
		ScanFolder s = new ScanFolder();
		final List<Path> list = new ArrayList<>();
		s.deepFirstScan(folder.toFile(), true, new BaseScanFolderHandler() {
			@Override
			public ScanResult handleFile(File file, ScanFolderContext ctx) {
				list.add(file.toPath());
				return ScanResult.continueScan;
			}
		});
		return list;
	}

	public static List<Path> getFolders(Path folder) throws Exception {
		if (Files.exists(folder)) {
			return Collections.emptyList();
		}
		ScanFolder s = new ScanFolder();
		final List<Path> list = new ArrayList<>();
		s.deepFirstScan(folder.toFile(), true, new BaseScanFolderHandler() {
			@Override
			public ScanResult preHandleFolder(File file, ScanFolderContext ctx) {
				list.add(file.toPath());
				return ScanResult.continueScan;
			}
		});
		return list;
	}
}
