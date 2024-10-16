package ddc.support.jack;

import ddc.support.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE;

public class SimpleJsonRepo<T> {
    private static Logger logger = LoggerFactory.getLogger(SimpleJsonRepo.class);
    RepoName<T> repoName = new RepoName<>();
    private JsonTools jsonTools;
    private Path basePath = Paths.get(System.getProperty("user.dir"));
    private boolean backupEnabled;

    public SimpleJsonRepo() {
        jsonTools = new JsonTools();
    }

    public SimpleJsonRepo(Path basePath) {
        this.basePath = basePath;
        jsonTools = new JsonTools();
    }

    private String loadTextfile(Path path) throws UnsupportedEncodingException, IOException {
        return Files.readString(path);
    }

    private void createTextfile(Path path, String text) throws UnsupportedEncodingException, IOException {
        Path tmpPath = FileUtil.postfixFileName(path, "_tmp");
        if (Files.exists(path)) {
            Files.move(path, tmpPath);
        }
        Files.write(path, text.getBytes(), CREATE);
        if (Files.exists(tmpPath))
            Files.delete(tmpPath);
    }

    public void setBasePath(Path basePath) {
        this.basePath = basePath;
    }

    public boolean isBackupEnabled() {
        return backupEnabled;
    }

    public void setBackupEnabled(boolean backupEnabled) {
        this.backupEnabled = backupEnabled;
    }

    public List<T> load(Class<T> clazz) throws IOException {
        List<T> items = new LinkedList<>();
        List<RepoName<T>> names = loadRepoNames(clazz);
        for (RepoName<T> repoName : names) {
            T item = load(repoName.getPath(), clazz);
            items.add(item);
        }
        return items;
    }

    private List<RepoName<T>> loadRepoNames(Class<T> clazz) throws IOException {
        return repoName.read(basePath, clazz, true);
    }

    public T load(Class<T> clazz, String id) throws IOException {
        Path repos = repoName.buildRepository(basePath, clazz, id);
        return load(repos, clazz);
    }

    private T createEntity(Class<T> clazz) throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Constructor<T> constructor = clazz.getConstructor();
        return (T) constructor.newInstance();
    }

    public T load(Path path, Class<T> clazz) throws IOException {
        try {
            if (Files.exists(path)) {
                String data = loadTextfile(path);
                return (T) clazz.cast(jsonTools.parse(data, clazz));
            } else {
                create(path, clazz);
                return null;
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    private void create(Path path, Class<T> clazz) throws IOException {
        try {
            Constructor<T> constructor = clazz.getConstructor();
            Object entity = constructor.newInstance();
            String data = jsonTools.toPrettifiedString(entity);
            createTextfile(path, data);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public void store(T entity, String id) throws IOException {
        Path repos = repoName.buildRepository(basePath, entity.getClass(), id);
        store(repos, entity);
    }

    private void store(Path path, T entity) throws IOException {
        try {
            String data = jsonTools.toPrettifiedString(entity);
            if (isBackupEnabled())
                backup(path);
            createTextfile(path, data);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public void remove(T entity, String id) throws IOException {
        Path repos = repoName.buildRepository(basePath, entity.getClass(), id);
        if (Files.exists(repos))
            Files.delete(repos);
        else
            logger.warn("File not found: " + repos);

    }

    private Path backup(Path path) throws UnsupportedEncodingException, IOException {
        if (Files.exists(path)) {
            Path bakPath = repoName.buildBackupRepository(path);
            Files.move(path, bakPath, StandardCopyOption.REPLACE_EXISTING);
            return bakPath;
        }
        return null;
    }

}
