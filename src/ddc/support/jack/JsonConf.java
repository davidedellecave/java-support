package ddc.support.jack;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;

import ddc.support.util.TextFile;

public class JsonConf {

	public static <T> T loadConfiguration(Path path, Class<T> clazz, T defaultConf) throws IOException {
		try {
			if (Files.exists(path)) {
				String data = TextFile.load(path);
				T context = JackUtil.parse(data, clazz);
				return context;
			} else {
				storeConfiguration(path, defaultConf);
				return defaultConf;
			}
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	public static <T> T loadConfiguration(Path path, Class<T> clazz)
			throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		return loadConfiguration(path, clazz, clazz.getDeclaredConstructor().newInstance());
	}

	public static <T> void storeConfiguration(Path path, T conf) throws IOException {
		try {
			String data = JackUtil.toPrettifiedString(conf);
			TextFile.create(path, data);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}
