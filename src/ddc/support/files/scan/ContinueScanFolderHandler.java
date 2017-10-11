package ddc.support.files.scan;

import java.io.File;

import ddc.support.files.scan.ScanFolder.ScanResult;

public class ContinueScanFolderHandler implements ScanFolderHandler{

	@Override
	public ScanResult startScan(ScanFolderContext ctx) throws Exception {
		return ScanResult.continueScan;
	}

	@Override
	public ScanResult endScan(ScanFolderContext ctx) throws Exception {
		return ScanResult.continueScan;	}

	@Override
	public ScanResult handleFile(File file, ScanFolderContext ctx) throws Exception {
		return ScanResult.continueScan;	}

	@Override
	public ScanResult preHandleFolder(File folder, ScanFolderContext ctx)
			throws Exception {
		return ScanResult.continueScan;	}

	@Override
	public ScanResult postHandleFolder(File folder, ScanFolderContext ctx)
			throws Exception {
		return ScanResult.continueScan;
	}

}
