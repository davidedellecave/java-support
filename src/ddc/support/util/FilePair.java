package ddc.support.util;

import java.nio.file.Path;

public class FilePair {
	public Path source = null;
	public Path target = null;

	public FilePair() {
	}

	public FilePair(Path source, Path target) {
		super();
		this.source = source;
		this.target = target;
	}

	public Path getSource() {
		return source;
	}

	public void setSource(Path source) {
		this.source = source;
	}

	public Path getTarget() {
		return target;
	}

	public void setTarget(Path target) {
		this.target = target;
	}

	@Override
	public String toString() {
		return "[" + source.toString() + "] > [" + target.toString() + "]";
	}
}
