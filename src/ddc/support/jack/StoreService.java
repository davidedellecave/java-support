package ddc.support.jack;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static java.nio.file.StandardOpenOption.CREATE;

@Deprecated
public class StoreService {
    private static JsonTools jackUtil;

    static {
        var mapper = JsonTools.buildMapperByPolymorphicTypes(new String[]{"java.util", "ddc"});
        jackUtil = new JsonTools(mapper);
    }

    synchronized static public Object load(Path path, Class clazz) throws IOException {
        try {
            if (Files.exists(path)) {
                String data = loadTextfile(path);
                Object context = clazz.cast(jackUtil.parse(data, clazz));
                return context;
            } else {
                create(path, clazz);
                return null;
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    synchronized static private void create(Path path, Class clazz) throws IOException {
        try {
            Constructor constructor = clazz.getConstructor();
            Object entity = constructor.newInstance();
            String data = jackUtil.toPrettifiedString(entity);
            createTextfile(path, data);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    synchronized static public void store(Path path, Object entity) throws IOException {
        try {
            String data = jackUtil.toPrettifiedString(entity);
            createTextfile(path, data);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    synchronized static public String loadTextfile(Path path) throws UnsupportedEncodingException, IOException {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    synchronized static public void createTextfile(Path path, String text) throws UnsupportedEncodingException, IOException {
        backup(path);
        Files.write(path, text.getBytes(), CREATE);
    }

    synchronized static private void backup(Path path) throws UnsupportedEncodingException, IOException {
        if (Files.exists(path)) {
            Path parent = path.getParent();
            Files.move(path, parent.resolve(path.getFileName() + ".bak"), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
