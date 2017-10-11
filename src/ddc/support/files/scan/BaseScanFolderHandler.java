package ddc.support.files.scan;

import java.io.File;

import ddc.support.files.scan.ScanFolder.ScanResult;

public class BaseScanFolderHandler implements ScanFolderHandler {
	public ScanResult handleFile(File file, ScanFolderContext ctx) {
		return ScanResult.continueScan;
	}
	
	public ScanResult preHandleFolder(File folder, ScanFolderContext ctx)  {
		return ScanResult.continueScan;
	}
	
	public ScanResult postHandleFolder(File folder, ScanFolderContext ctx) {
		return ScanResult.continueScan;
	}

	@Override
	public ScanResult startScan(ScanFolderContext ctx) throws Exception {
		return ScanResult.continueScan;
	}

	@Override
	public ScanResult endScan(ScanFolderContext ctx) throws Exception {
		return ScanResult.continueScan;
	}
}
