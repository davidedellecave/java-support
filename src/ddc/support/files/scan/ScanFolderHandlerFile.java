package ddc.support.files.scan;

import java.nio.file.Path;

public interface ScanFolderHandlerFile {
	public ScanResult handleFile(Path file, ScanFolderContext ctx) throws Exception;
}
