package ddc.support.files;

import javax.persistence.Entity;

@Entity
public class FileRecord {
	private String filename;
	private String extension;
	private long bytes;
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public long getBytes() {
		return bytes;
	}
	public void setBytes(long bytes) {
		this.bytes = bytes;
	}
	
	
	
}

