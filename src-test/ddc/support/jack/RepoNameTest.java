package ddc.support.jack;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

class RepoNameTest {
    RepoName repoName = new RepoName();
    private Path getBasePath() {
        String source = "/Users/davide/ddc/src/intellij-workspace/ddc-todolist/data";
        Path basePath = Paths.get(source);
        return basePath;
    }


    public void Test_ReadFolder() throws IOException {
        List<RepoName> list = repoName.read(getBasePath(), JsonEntity.class);
        for (RepoName name : list) {
            System.out.println(name.toString());
        }
    }


    public void Test_ReadFolder_NoBackup() throws IOException {
        List<RepoName> list = repoName.read(getBasePath(), JsonEntity.class,true);
        for (RepoName name : list) {
            System.out.println(name.toString());
        }
    }

    public void Test_ReadFolder_Backup() throws IOException {
        List<RepoName> list = repoName.read(getBasePath(), JsonEntity.class,true);
        for (RepoName name : list) {
            var backupList = repoName.readBackupOf(getBasePath(), JsonEntity.class, name.getId());
            System.out.println("Backup Of:[" + name.getId() + "]");
            for (Object bakName : backupList) {
                System.out.println(bakName.toString());
            }
        }
    }

    @Test
    public void Test_RepoFolder() throws IOException {
        String id = UUID.randomUUID().toString();
        Path path = repoName.buildRepository(getBasePath(), String.class, id);
        System.out.println(path.toString());

        Path pathBak = repoName.buildBackupRepository(path);
        System.out.println(pathBak.toString());

    }

}