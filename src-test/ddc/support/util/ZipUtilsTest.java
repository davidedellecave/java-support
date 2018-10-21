package ddc.support.util;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class ZipUtilsTest {

	@Test
	public void testUnzipFileString() throws IOException {
		String p = "/Users/davide/tmp/LogPref/LogAvivaPreferred.zip";

		Path path = Paths.get(p);
//		ZipUtils.unzip(p, "/Users/davide/tmp/LogPref/unzip", true);
	}

}
