package ddc.support.jack;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class RepoName<T> {
    private static final String SEP = ".";
    private static final boolean classSubfolderEnabled = true;
    private static long backupMillisRatio = 10000;
    private Path path = null;
    private String simpleClassname = null;
    private String id = null;
    private String backupId = null;


    public Path buildRepository(Path basePath, Class clazz, String id) {
        return buildRepository(basePath, clazz.getSimpleName(), id, null);
    }

    private String buildBackupId() {
        return String.valueOf(System.currentTimeMillis() / backupMillisRatio);
    }

    private Path buildBackupRepository(Path basePath, Class<T> clazz, String id) {
        return buildBackupRepository(basePath, clazz.getSimpleName(), id);
    }

    private Path buildBackupRepository(Path basePath, String simpleClassname, String id) {
        return buildRepository(basePath, simpleClassname, id, buildBackupId());
    }

    public Path buildBackupRepository(Path path) {
        RepoName<T> name = parseFilename(path);
        if (name != null) {
            Path parent = path.getParent();
            if (classSubfolderEnabled) {
                parent = parent.getParent();
            }
            return buildBackupRepository(parent, name.getSimpleClassname(), name.id);
        } else {
            return null;
        }
    }

    private static Path buildRepository(Path basePath, String simpleClassname, String id, String backupId) {
        String filename = simpleClassname + SEP + id;
        if (backupId != null)
            filename += SEP + backupId;
        filename += ".json";
        if (classSubfolderEnabled) {
            return basePath.resolve(simpleClassname).resolve(filename);
        } else {
            return basePath.resolve(filename);
        }
    }


    private RepoName<T> parseFilename(Path path) {
        String filename = path.getFileName().toString();
        String[] toks = filename.split("\\" + SEP);
        if (toks.length < 3)
            return null;
        RepoName<T> name = new RepoName<>();
        name.setPath(path);
        name.setSimpleClassname(toks[0]);
        name.setId(toks[1]);
        if (toks.length > 3) {
            name.setBackupId(toks[2]);
        }
        return name;
    }

    public List<RepoName<T>> read(Path folder, Class<T> clazz) throws IOException {
        if (!Files.exists(folder)) {
            throw new IOException("Folder not found:[" + folder + "]");
        }
        if (classSubfolderEnabled) {
            folder = folder.resolve(clazz.getSimpleName());
        }
        List<RepoName<T>> list = new LinkedList<>();
        try (Stream<Path> filePathStream = Files.walk(folder, 1)) {
            filePathStream.forEach(filePath -> {
                if (Files.isRegularFile(filePath) &&
                        filePath.getFileName().toString().toLowerCase().endsWith("json")
                ) {
                    RepoName<T> name = parseFilename(filePath);
                    if (name != null && name.getSimpleClassname().equals(clazz.getSimpleName()))
                        list.add(name);
                }
            });
        }
        return list;
    }

    public List<RepoName<T>> read(Path folder, Class<T> clazz, boolean excludeBackup) throws IOException {
        var list = read(folder, clazz);
        if (excludeBackup) {
            return list.stream().filter(RepoName::isNotBackup).toList();
        } else {
            return list;
        }
    }

    public List<RepoName<T>> readBackupOf(Path folder, Class<T> clazz, String id) throws IOException {
        var list = read(folder, clazz);
        return list.stream().filter(x -> (
                x.isBackup() && x.getId().equals(id))).toList();
    }

    public String getSimpleClassname() {
        return simpleClassname;
    }

    public void setSimpleClassname(String simpleClassname) {
        this.simpleClassname = simpleClassname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBackupId() {
        return backupId;
    }

    public void setBackupId(String backupId) {
        this.backupId = backupId;
    }

    public boolean isBackup() {
        return backupId != null;
    }

    public boolean isNotBackup() {
        return !isBackup();
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    @Override
    public String toString() {
        try {
            return new JsonTools().toPrettifiedString(this);
        } catch (JsonProcessingException e) {
            return e.toString();
        }
    }

}
