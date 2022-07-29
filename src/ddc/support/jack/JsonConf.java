package ddc.support.jack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import ddc.support.util.TextFile;

public class JsonConf {
	
	public static Object loadConfiguration(Path path, Object conf) throws IOException {
		try {
			if (Files.exists(path)) {
				String data = TextFile.load(path);
				Object context = JackUtil.parse(data, conf.getClass());
				return context;
			} else {
				storeConfiguration(path, conf);
				return null;
			}
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	
	
	public static void storeConfiguration(Path path, Object conf) throws IOException {
		try {
			String data = JackUtil.toPrettifiedString(conf);
			TextFile.create(path, data);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
}
