package ddc.support.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.security.CodeSource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;

public class FileUtil {
	public static String loadContent(String path) throws IOException {
		return new String(Files.readAllBytes(Paths.get(path)), "UTF-8");
	}

	public static String loadContent(Path path) throws IOException {
		return new String(Files.readAllBytes(path), "UTF-8");
	}

	public static Path replaceFilename(Path path, String filename) {
		return path.resolveSibling(Paths.get(filename));
	}

	public static Path prefixFileName(Path path, String prefix) {
		String newFilename = prefix + path.getFileName();
		return replaceFilename(path, newFilename);
	}

	public static Path getPath(Path path, Path appendPath) {
		return path.resolve(appendPath.toString());
	}

	public static Path getPath(Path path, String appendPath) {
		return path.resolve(appendPath);
	}

	public static Path postfixFileName(Path path, String postfix) {
		String newFilename = path.getFileName() + postfix;
		return replaceFilename(path, newFilename);
	}

	public static boolean areEqualSizeAndModifiedTime(Path f1, Path f2) throws IOException {
		long size1 = Files.size(f1);
		long size2 = Files.size(f2);
		FileTime time1 = Files.getLastModifiedTime(f1);
		FileTime time2 = Files.getLastModifiedTime(f2);
		return (size1 == size2 && time1.toMillis() == time2.toMillis());
	}

	public static boolean areEqualModifiedTime(Path f1, Path f2) throws IOException {
		FileTime time1 = Files.getLastModifiedTime(f1);
		FileTime time2 = Files.getLastModifiedTime(f2);
		return (time1.toMillis() == time2.toMillis());
	}

	/**
	 * Rename file on filesystem
	 * 
	 * @param path
	 * @param newName
	 * @throws IOException
	 */
	// public static void rename(Path path, String newName) throws IOException {
	// Files.move(path, path.resolveSibling(newName));
	// }

	public static boolean isOlderThan(File file, long duration) {
		long date = System.currentTimeMillis() - duration;
		return (file.lastModified() < date);
	}

	public static boolean isNewerThan(File file, long duration) {
		long date = System.currentTimeMillis() - duration;
		return (file.lastModified() >= date);
	}

	public static boolean isNewerThan(Path file, long duration) throws IOException {
		long date = System.currentTimeMillis() - duration;
		return (Files.getLastModifiedTime(file).toMillis() >= date);
	}

	// public static String loadContent(String path) throws IOException {
	// return new String(Files.readAllBytes(Paths.get(path)), "UTF-8");
	// }
	//
	// public static String loadContent(Path path) throws IOException {
	// return new String(Files.readAllBytes(path), "UTF-8");
	// }
	//
	// public static Path replaceFilename(Path path, String filename) {
	// return path.resolveSibling(Paths.get(filename));
	// }
	//
	// public static Path prefixFileName(Path path, String prefix) {
	// String newFilename = prefix + path.getFileName();
	// return replaceFilename(path, newFilename);
	// }

	// public static Path postfixFileName(Path path, String postfix) {
	// String newFilename = path.getFileName() + postfix;
	// return replaceFilename(path, newFilename);
	// }

	/**
	 * Rename file on filesystem
	 * 
	 * @param path
	 * @param newName
	 * @throws IOException
	 */
	public static void rename(Path path, String newName) throws IOException {
		Files.move(path, path.resolveSibling(newName));
	}

	public static File getDailyRollerFilename(File file) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String date = "_" + formatter.format(new Date());
		return postfixFileName(file.toPath(), date).toFile();
	}

	public static boolean isReadbleFolder(String folder) {
		if (folder == null)
			return false;
		return isReadbleFolder(new File(folder));
	}

	public static boolean isReadbleFolder(File folder) {
		if (folder == null)
			return false;
		if (!folder.exists() || !folder.canRead())
			return false;
		return true;
	}

	public static boolean createWritebleFolder(String folder) {
		return createWritebleFolder(new File(folder));
	}

	public static boolean createWritebleFolder(File folder) {
		if (folder == null)
			return false;
		if (!folder.exists()) {
			if (!folder.mkdirs())
				return false;
		} else {
			if (!folder.canWrite())
				return false;
		}
		return true;
	}

	// public static FileName buildFileName(String path) {
	// FileName fn = new FileName();
	// String p = FilenameUtils.getFullPathNoEndSeparator(path);
	// fn.path = FilenameUtils.getPath(p);
	// fn.name = FilenameUtils.getName(p);
	// fn.ext = FilenameUtils.getExtension(p);
	// return fn;
	// }

	public static String getFileExtension(File file) {
		return getFileExtension(file.getName());
	}

	public static File renameFileDailyRoller(File file) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String date = "_" + formatter.format(new Date());
		return renameFileAddSuffix(file, date);
	}

	public static File renameFileTimestampRoller(File file) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String date = "_" + formatter.format(new Date());
		return renameFileAddSuffix(file, date);
	}

	public static File renameFileIdRoller(File file) {
		String id = "_" + UUID.randomUUID().toString();
		return renameFileAddSuffix(file, id);
	}

	public static File renameFileAddSuffix(File file, String suffix) {
		String newName = getFilenameWhithoutExtension(file.getName()) + suffix;
		return renameFile(file, newName, true);
	}

	public static FileName buildFileName(String path) {
		FileName fn = new FileName();
		String p = FilenameUtils.getFullPathNoEndSeparator(path);
		fn.path = FilenameUtils.getPath(p);
		fn.name = FilenameUtils.getName(p);
		fn.ext = FilenameUtils.getExtension(p);
		return fn;
	}

	/**
	 * Add a progressive index to make the filename unique Example. doc.xml ->
	 * doc-1.xml
	 * 
	 * @param file
	 * @return
	 */
	public static File getUniqueFileName(File file) {
		int counter = 0;
		while (file.exists()) {
			counter++;
			file = renameFileAddSuffix(file, "-" + String.valueOf(counter));
		}
		return file;
	}

	public static String getFileExtension(String name) {
		return FilenameUtils.getExtension(name);
	}

	public static String getFilenameWhithoutExtension(String name) {
		return FilenameUtils.removeExtension(name);
	}

	public static String getFilenameWhithoutExtension(File file) {
		return getFilenameWhithoutExtension(file.getName());
	}

	public static File renameFile(File file, String newName, boolean preserveExtension) {
		String ext = getFileExtension(file);
		String path = file.getParent();
		if (preserveExtension) {
			return new File(path, newName + "." + ext);
		}
		return new File(path, newName);
	}

	public static Path renameFile(Path path, String newName, boolean preserveExtension) {
		File file = renameFile(path.toFile(), newName, preserveExtension);
		return file.toPath();
	}

	public static File renameFileExtension(File file, String extension) {
		String name = getFilenameWhithoutExtension(file);
		String path = file.getParent();
		if (extension.length() > 0) {
			return new File(path, name + "." + extension);
		} else {
			return new File(path, name);
		}
	}

	public static File renameFileExtension(String filename, String extension) {
		return renameFileExtension(new File(filename), extension);
	}

	public static String getRootFolder(String path) {
		int pos = path.indexOf(File.separator);
		if (pos >= 0)
			return path.substring(0, pos);
		return path;
	}

	public static void saveObject(String filename, Serializable object) throws IOException {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(object);
		} finally {
			out.close();
		}
	}

	public static Object loadObject(String filename) throws IOException, ClassNotFoundException {
		FileInputStream fis = null;
		ObjectInputStream in = null;
		Object obj = null;
		try {
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
			obj = in.readObject();
		} finally {
			in.close();
		}
		return obj;
	}

	/**
	 * Remove path separator from end string
	 * 
	 * @param path
	 * @return
	 */
	public static String normalizePathSeparator(String path) {
		if (path == null)
			return "";
		if (path.endsWith(File.separator))
			path = path.substring(0, path.lastIndexOf(File.separator));
		if (path.startsWith(File.separator))
			path = path.substring(1);
		return path;
	}

	public static String appendPathSeparator(String path) {
		if (path == null)
			return File.separator;
		if (!path.endsWith(File.separator))
			path += File.separator;
		return path;
	}

	public static String appendSeparator(String path, String separator) {
		if (path == null)
			return separator;
		if (!path.endsWith(separator))
			path += separator;
		return path;
	}

	public static String removeEndSeparator(String path) {
		return FilenameUtils.getFullPathNoEndSeparator(path);
	}

	// String path = FileUtils.relocatePath(source.getAbsolutePath(), sourceFolder,
	// targetFolder);
	public static String relocatePath(String path, String basePath, String targetBasePath) {
		path = normalizePathSeparator(path);
		basePath = normalizePathSeparator(basePath);
		if (path.startsWith(basePath))
			path = normalizePathSeparator(path.substring(basePath.length()));
		path = path.replace(":", "");
		return appendPathSeparator(targetBasePath) + path;
	}

	// public static String byteCountToDisplaySize(long size) {
	//
	// return String.format("%.2f", f);
	//
	// }

	// The number of bytes in an kilobyte.
	public static final long ONE_KB = 1024;
	public static final BigInteger ONE_KB_BI = BigInteger.valueOf(ONE_KB);
	// The number of bytes in an megabyte.
	public static final long ONE_MB = ONE_KB * ONE_KB;
	public static final BigInteger ONE_MB_BI = ONE_KB_BI.multiply(ONE_KB_BI);
	// The number of bytes in an gigabyte.
	public static final long ONE_GB = ONE_KB * ONE_MB;
	public static final BigInteger ONE_GB_BI = ONE_KB_BI.multiply(ONE_MB_BI);
	// The number of bytes in an terabyte.
	public static final long ONE_TB = ONE_KB * ONE_GB;
	public static final BigInteger ONE_TB_BI = ONE_KB_BI.multiply(ONE_GB_BI);
	// The number of bytes in an petabyte.
	public static final long ONE_PB = ONE_KB * ONE_TB;
	public static final BigInteger ONE_PB_BI = ONE_KB_BI.multiply(ONE_TB_BI);
	// The number of bytes in an exabyte.
	public static final long ONE_EB = ONE_KB * ONE_PB;
	public static final BigInteger ONE_EB_BI = ONE_KB_BI.multiply(ONE_PB_BI);
	// The number of bytes in a zettabyte.
	public static final BigInteger ONE_ZB = BigInteger.valueOf(ONE_KB).multiply(BigInteger.valueOf(ONE_EB));
	// The number of bytes in a yottabyte.
	public static final BigInteger ONE_YB = BigInteger.valueOf(ONE_KB).multiply(ONE_ZB);

	public static String byteCountToDisplaySize(final long size) {
		return byteCountToDisplaySize(BigInteger.valueOf(size));
	}

	public static String byteCountToDisplaySize(final BigInteger size) {
		String displaySize;

		if (size.divide(ONE_EB_BI).compareTo(BigInteger.ZERO) > 0) {
			displaySize = String.valueOf(size.divide(ONE_EB_BI)) + " EB";
		} else if (size.divide(ONE_PB_BI).compareTo(BigInteger.ZERO) > 0) {
			displaySize = String.valueOf(size.divide(ONE_PB_BI)) + " PB";
		} else if (size.divide(ONE_TB_BI).compareTo(BigInteger.ZERO) > 0) {
			displaySize = String.valueOf(size.divide(ONE_TB_BI)) + " TB";
		} else if (size.divide(ONE_GB_BI).compareTo(BigInteger.ZERO) > 0) {
			displaySize = String.valueOf(size.divide(ONE_GB_BI)) + " GB";
		} else if (size.divide(ONE_MB_BI).compareTo(BigInteger.ZERO) > 0) {
			displaySize = String.valueOf(size.divide(ONE_MB_BI)) + " MB";
		} else if (size.divide(ONE_KB_BI).compareTo(BigInteger.ZERO) > 0) {
			displaySize = String.valueOf(size.divide(ONE_KB_BI)) + " KB";
		} else {
			displaySize = String.valueOf(size) + " bytes";
		}
		return displaySize;
	}

	public static String getJarFolder(Object packageClass) {
		URL url = packageClass.getClass().getProtectionDomain().getCodeSource().getLocation();
		File jarFile = new File(url.getPath());
		return jarFile.getParent();
	}

	// new Properties().load(Foo.class.getResourceAsStream("file.properties"))
	public static Properties loadProperties(File file) throws FileNotFoundException, IOException {
		// Read properties file.
		Properties properties = new Properties();
		properties.load(new FileInputStream(file));
		return properties;
	}

	public static String readFileAsString(File file) throws IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}

	/**
	 * Ritorna l'oggetto File per un file con il nome specificato, che si trovi
	 * nella cartella dove si trova il file .class della classe specificata. (nel
	 * caso di file jar, nella directory del jar).
	 * 
	 * NB sotto un Servlet Container (Tomcat) potrebbe non funzionare come ci si
	 * aspetta
	 * 
	 * @param clazz
	 * @param filename
	 * @return
	 * @throws URISyntaxException
	 * @throws UnsupportedEncodingException
	 */
	public static File getLocalFile(Class<? extends Object> clazz, String filename) throws URISyntaxException, UnsupportedEncodingException {
		CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();
		String location = codeSource.getLocation().toString();
		String urlEncoded = location.replaceAll(" ", "%20");
		URI uri = new URI(urlEncoded);
		File jarFile = new File(uri.getPath());
		File jarDir = jarFile.getParentFile();
		File propFile = null;
		if (jarDir != null && jarDir.isDirectory()) {
			propFile = new File(jarDir, filename);
		}
		return propFile;
	}

//	public void watchFile(Path path, Callable<T> callback) {
//		System.out.println(path);
//		try (final WatchService watchService = FileSystems.getDefault().newWatchService()) {
//			final WatchKey watchKey = path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
//			while (true) {
//				final WatchKey wk = watchService.take();
//				for (WatchEvent<?> event : wk.pollEvents()) {
//					// we only register "ENTRY_MODIFY" so the context is always a Path.
//					final Path changed = (Path) event.context();
//					System.out.println(changed);
//					if (changed.endsWith("myFile.txt")) {
//						System.out.println("My file has changed");
//					}
//				}
//				// reset the key
//				boolean valid = wk.reset();
//				if (!valid) {
//					System.out.println("Key has been unregisterede");
//				}
//			}
//		}
//	}
}
