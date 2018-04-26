package ddc.support.files.scan;

import java.nio.file.Path;
import java.util.List;

public class ScanFolderContext {
		private ScanFolderConfig config = null;
		private ScanFolderStats stats = null;
		private Path folder = null;
		private List<Path> siblingFiles = null;
		private List<Path> siblingFolder = null;

		
		public ScanFolderConfig getConfig() {
			return config;
		}
		public void setConfig(ScanFolderConfig config) {
			this.config = config;
		}
		public ScanFolderStats getStats() {
			return stats;
		}
		public void setStats(ScanFolderStats stats) {
			this.stats = stats;
		}
		public Path getFolder() {
			return folder;
		}
		public void setFolder(Path folder) {
			this.folder = folder;
		}
		public List<Path> getSiblingFiles() {
			return siblingFiles;
		}
		public void setSiblingFiles(List<Path> siblingFiles) {
			this.siblingFiles = siblingFiles;
		}
		public List<Path> getSiblingFolder() {
			return siblingFolder;
		}
		public void setSiblingFolder(List<Path> siblingFolder) {
			this.siblingFolder = siblingFolder;
		}		
}
