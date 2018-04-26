package ddc.support.files.scan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
		s.deepFirstScan(folder, recursive, new BaseScanFolderHandler() {
			@Override
			public ScanResult handleFile(Path file, ScanFolderContext ctx) throws IOException {
				boolean toAdd = false;
				
				if (olderThanMillis>0 && FileUtil.isNewerThan(file, olderThanMillis)) {
					return ScanResult.continueScan;
				}
				
				if (includeExtension.length > 0) {
					for (String ext : includeExtension) {
						if (file.getFileName().endsWith(ext))
							toAdd = true;
					}				
				} else {
					toAdd=true;
				}
				
				if (excludeExtension.length > 0) {
					for (String ext : excludeExtension) {
						if (file.getFileName().endsWith(ext))
							toAdd = false;
					}				
				}

				if (toAdd)
					list.add(file);
				return ScanResult.continueScan;
			}
		});
		return list;
	}
	
	public static void handleFiles(Path folder, ScanFolderHandlerFile handler)  throws Exception {
		ScanFolder s = new ScanFolder();
		s.deepFirstScan(folder, true, new BaseScanFolderHandler() {
			@Override
			public ScanResult handleFile(Path file, ScanFolderContext ctx) throws Exception {
				return handler.handleFile(file, ctx);
			}
		});
	}

	public static List<Path> getFiles(Path folder) throws Exception {
		if (!folder.toFile().isDirectory()) {
			return Collections.emptyList();
		}
		ScanFolder s = new ScanFolder();
		final List<Path> list = new ArrayList<>();
		s.deepFirstScan(folder, true, new BaseScanFolderHandler() {
			@Override
			public ScanResult handleFile(Path file, ScanFolderContext ctx) {
				list.add(file);
				return ScanResult.continueScan;
			}
		});
		return list;
	}
	
	
	public static List<Path> getLastModifiedSorted(Path folder) throws Exception {
		List<Path> list = ScanFolderUtil.getFiles(folder);
		Collections.sort(list, new Comparator<Path>() {
			@Override
			public int compare(Path path1, Path path2) {				
				try {
					if (path1.toString().equals(path2.toString())) return 0;
					long m1 = Files.getLastModifiedTime(path1).toMillis();
					long m2 = Files.getLastModifiedTime(path2).toMillis();					
					if (m1==m2) return 0;
					return m1 < m2 ? 1 : -1;
				} catch (IOException e) {
					e.printStackTrace();
				}
				return 0;
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
		s.deepFirstScan(folder, true, new BaseScanFolderHandler() {
			@Override
			public ScanResult preHandleFolder(Path file, ScanFolderContext ctx) {
				list.add(file);
				return ScanResult.continueScan;
			}
		});
		return list;
	}
}
