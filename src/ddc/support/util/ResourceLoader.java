package ddc.support.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResourceLoader {

	public InputStream resourceAsStream(@SuppressWarnings("rawtypes") Class clazz, String resource) {
		return clazz.getClassLoader().getResourceAsStream(resource);
	}

	public String resourceAsString(@SuppressWarnings("rawtypes") Class clazz, String resource) throws IOException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;	
		try (InputStream inputStream = resourceAsStream(clazz, resource)) {
			while ((length = inputStream.read(buffer)) != -1) {
			    result.write(buffer, 0, length);
			}
			return result.toString("UTF-8");
		} catch (IOException e) {
			throw e;
		}
	}
}
