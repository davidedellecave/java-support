package ddc.support.files.scan;

import java.io.File;

import ddc.support.files.scan.ScanFolder.ScanResult;

public interface ScanFolderHandler extends ScanFolderHandlerFile {
	public ScanResult startScan(ScanFolderContext ctx) throws Exception;

	public ScanResult endScan(ScanFolderContext ctx) throws Exception;

	public ScanResult preHandleFolder(File folder, ScanFolderContext ctx) throws Exception;

	public ScanResult postHandleFolder(File folder, ScanFolderContext ctx) throws Exception;
}
