package ddc.support.files.scan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import ddc.support.util.DateUtil;

public class ScanUtilTest {

	// @Test
	// public void test() throws Exception {
	// Path path = Paths.get("/Users/dellecave/temp/in/");
	// String d = "3 DAYS";
	// long millis = ParseUtil.parseTimeAndUnit(d);
	// System.out.println(millis);
	// List<Path> list = ScanFolderUtil.getFiles(path, true, millis, new String[]
	// {}, new String[] {});
	// System.out.println(list);
	// }

	@Test
	public void testGetLastFile() throws Exception, IOException {
		String p = "/Users/davide/OneDrive/OneDrive - S2E Sprint srl/ACIGlobal/KpiRete/s2e-src-Kpi_Rete/tag/rc";
		Path path = Paths.get(p);

		List<Path> list = ScanFolderUtil.getLastModifiedSorted(path);

		// List<Path> list = ScanFolderUtil.getFiles(path);
		//
		// Collections.sort(list, new Comparator<Path>() {
		//
		//// @Override
		//// public int compare(Path path1, Path path2) {
		//// System.out.println(path1 + " - " + path2);
		//// return 0;
		//// }
		//
		// @Override
		// public int compare(Path path1, Path path2) {
		// try {
		// System.out.println(path1 + " - " + path2);
		// if (Files.getLastModifiedTime(path1).toMillis() ==
		// Files.getLastModifiedTime(path2).toMillis()) return 0;
		// return Files.getLastModifiedTime(path1).toMillis() <
		// Files.getLastModifiedTime(path2).toMillis() ? 1 : -1;
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// return 0;
		//// System.out.println(path1 + " - " + path2);
		//// return FileUtils.isFileNewer(path1.toFile(), path2.toFile()) ? 1 : -1;
		// }
		//
		// });
		System.out.println("\n\n\nSorted----------");
		list.forEach(x -> {
			try {

				long millis = Files.getLastModifiedTime(x).toMillis();
				System.out.println(StringUtils.rightPad(x.toString(), 190) + "\t " + DateUtil.format(new Date(millis), DateUtil.DATE_PATTERN_ISO));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

}
