package ddc.support.files.scan;

import java.io.File;

import ddc.support.files.scan.ScanFolder.ScanResult;

public class DefaultScanFolderHandler implements ScanFolderHandler {
	public ScanResult handleFile(File file, ScanFolderContext ctx) {
		System.out.println("File:" + file);
		return ScanResult.continueScan;
	}
	
	public ScanResult preHandleFolder(File folder, ScanFolderContext ctx) {
		System.out.println("Pre folder->>:" + folder);
		return ScanResult.continueScan;
	}
	
	public ScanResult postHandleFolder(File folder, ScanFolderContext ctx) {
		System.out.println("Post folder<<-:" + folder);
		return ScanResult.continueScan;
	}

	@Override
	public ScanResult startScan(ScanFolderContext ctx) throws Exception {
		System.out.println("Start Scan >>");
		return ScanResult.continueScan;
	}

	@Override
	public ScanResult endScan(ScanFolderContext ctx) throws Exception {
		System.out.println("End Scan <<");
		return ScanResult.continueScan;
	}
}
