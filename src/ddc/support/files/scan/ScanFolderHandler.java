package ddc.support.files.scan;

import java.nio.file.Path;

public interface ScanFolderHandler extends ScanFolderHandlerFile {
	public ScanResult startScan(ScanFolderContext ctx) throws Exception;

	public ScanResult endScan(ScanFolderContext ctx) throws Exception;

	public ScanResult preHandleFolder(Path folder, ScanFolderContext ctx) throws Exception;

	public ScanResult postHandleFolder(Path folder, ScanFolderContext ctx) throws Exception;
}
