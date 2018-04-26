package ddc.support.files.scan;

import java.nio.file.Path;

public class BaseScanFolderHandler implements ScanFolderHandler {
	@Override
	public ScanResult handleFile(Path file, ScanFolderContext ctx) throws Exception {
		return ScanResult.continueScan;
	}
	@Override
	public ScanResult preHandleFolder(Path folder, ScanFolderContext ctx)  throws Exception {
		return ScanResult.continueScan;
	}
	@Override
	public ScanResult postHandleFolder(Path folder, ScanFolderContext ctx) throws Exception {
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
