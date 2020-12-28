package ddc.support.files.scan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;

public class FileSelector {

	public static boolean isSizeBetween(Path path, long lowerBoundBytes, long upperBoundBytes) throws IOException {
		long bytes = Files.size(path);
		if (lowerBoundBytes < 0 && upperBoundBytes < 0)
			return false;

		if (lowerBoundBytes >= 0 && upperBoundBytes >= 0) {
			return (lowerBoundBytes <= bytes && bytes <= upperBoundBytes);
		}
		if (lowerBoundBytes >= 0) {
			return (lowerBoundBytes <= bytes);
		}
		if (upperBoundBytes >= 0) {
			return (bytes <= upperBoundBytes);
		}
		return false;
	}

	public static boolean isSizeGreaterThan(Path path, long boundBytes) throws IOException {
		long bytes = Files.size(path);
		return (bytes > boundBytes);
	}

	public static boolean isSizeLessThan(Path path, long boundBytes) throws IOException {
		long bytes = Files.size(path);
		return (bytes < boundBytes);
	}

	public static boolean isCreateBefore(Path path, Instant instant) throws IOException {
		BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
		return attr.creationTime().toInstant().isBefore(instant);
	}

	public static boolean isCreateAfter(Path path, Instant instant) throws IOException {
		BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
		return attr.creationTime().toInstant().isAfter(instant);
	}

	public static boolean isCreateBetween(Path path, Instant lowerInstant, Instant upperInstant) throws IOException {
		BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
		return attr.creationTime().toInstant().isAfter(lowerInstant)
				&& attr.creationTime().toInstant().isBefore(upperInstant);
	}

	public static boolean isModifiedBefore(Path path, Instant instant) throws IOException {
		BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
		return attr.lastModifiedTime().toInstant().isBefore(instant);
	}

	public static boolean isModifiedAfter(Path path, Instant instant) throws IOException {
		BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
		return attr.lastModifiedTime().toInstant().isAfter(instant);
	}

	public static boolean isModifiedBetween(Path path, Instant lowerInstant, Instant upperInstant) throws IOException {
		BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
		return attr.lastModifiedTime().toInstant().isAfter(lowerInstant)
				&& attr.lastModifiedTime().toInstant().isBefore(upperInstant);
	}

}
