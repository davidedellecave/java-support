package ddc.support.jack;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static java.nio.file.StandardOpenOption.CREATE;

public class JsonRepo<T extends JsonEntity> {
    private JsonTools jsonTools;
    private String[] defaultTypes = new String[]{"java.util", "ddc"};
    private Path basePath = Paths.get(System.getProperty("user.dir"));
    private boolean backupEnabled = true;
    RepoName repoName = new RepoName();

    public JsonRepo() {
        var mapper = JsonTools.buildMapperByPolymorphicTypes(defaultTypes);
        jsonTools = new JsonTools(mapper);
    }

    public JsonRepo(Path basePath) {
        this.basePath = basePath;
        var mapper = JsonTools.buildMapperByPolymorphicTypes(defaultTypes);
        jsonTools = new JsonTools(mapper);
    }

    public JsonRepo(Path basePath, String... subTypes) {
        this.basePath = basePath;
        var mapper = JsonTools.buildMapperByPolymorphicTypes(subTypes);
        jsonTools = new JsonTools(mapper);
    }

    public JsonRepo(String... subTypes) {
        var mapper = JsonTools.buildMapperByPolymorphicTypes(subTypes);
        jsonTools = new JsonTools(mapper);
    }

    private static String loadTextfile(Path path) throws UnsupportedEncodingException, IOException {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    private static void createTextfile(Path path, String text) throws UnsupportedEncodingException, IOException {
        Files.write(path, text.getBytes(), CREATE);
    }

    public boolean isBackupEnabled() {
        return backupEnabled;
    }

    public void setBackupEnabled(boolean backupEnabled) {
        this.backupEnabled = backupEnabled;
    }

    public T load(Class<T> clazz, String id) throws IOException {
        Path repos = repoName.buildRepository(basePath, clazz, id);
        return load(repos, clazz);
    }

    private T createEntity(Class<T> clazz) throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Constructor<T> constructor = clazz.getConstructor();
        T entity = (T) constructor.newInstance();
        return entity;
    }

    private T load(Path path, Class clazz) throws IOException {
        try {
            if (Files.exists(path)) {
                String data = loadTextfile(path);
                T context = (T) clazz.cast(jsonTools.parse(data, clazz));
                return context;
            } else {
                create(path, clazz);
                return null;
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    private void create(Path path, Class clazz) throws IOException {
        try {
            Constructor constructor = clazz.getConstructor();
            Object entity = constructor.newInstance();
            String data = jsonTools.toPrettifiedString(entity);
            createTextfile(path, data);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public void store(T entity) throws IOException {
        Path repos = repoName.buildRepository(basePath, entity.getClass(), entity.getId());
        store(repos, entity);
    }

    public void remove(T entity) throws IOException {
        Path repos = repoName.buildRepository(basePath, entity.getClass(), entity.getId());
        Files.delete(repos);
    }

    public void store(Path path, T entity) throws IOException {
        try {
            String data = jsonTools.toPrettifiedString(entity);
            if (isBackupEnabled())
                backup(path);
            createTextfile(path, data);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    private void backup(Path path) throws UnsupportedEncodingException, IOException {
        if (Files.exists(path)) {
            Path bakPath = repoName.buildBackupRepository(path);
            Files.move(path, bakPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public Path getBasePath() {
        return basePath;
    }

    public void setBasePath(Path basePath) {
        this.basePath = basePath;
    }
}
