package ddc.support.jack;

import ddc.support.util.Chronometer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class JsonRepoTest {
    private Path getBasePath() {
        String source = "/Users/davide/ddc/src/intellij-workspace/ddc-todolist/data";
        Path basePath = Paths.get(source);
        return basePath;
    }

    public void Test_Store() throws IOException {
        JsonRepo<JsonEntity> repo = new JsonRepo<>(getBasePath());
        JsonEntity e = new JsonEntity();
        repo.store(e);
    }


    public void Test_Backup() throws IOException {
        JsonRepo<JsonEntity> repo = new JsonRepo<>(getBasePath());
        repo.setBackupEnabled(true);
        JsonEntity e = new JsonEntity();
        repo.store(e);
        //
        Chronometer.sleep(1000);
        repo.store(e);
        //
        Chronometer.sleep(5*1000);
        repo.store(e);
    }


    public void Test_Load() throws IOException {
        JsonRepo<JsonEntity> repo = new JsonRepo<>(getBasePath());
        repo.setBackupEnabled(true);
        JsonEntity in = new JsonEntity();
        repo.store(in);
        System.out.println(in);

        JsonEntity out = repo.load(JsonEntity.class, in.getId());
        System.out.println(out);
    }

}