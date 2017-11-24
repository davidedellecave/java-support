package ddc.support.files.scan;

import java.io.File;

import ddc.support.files.scan.ScanFolder.ScanResult;

public interface ScanFolderHandlerFile {
	public ScanResult handleFile(File file, ScanFolderContext ctx) throws Exception;
}
