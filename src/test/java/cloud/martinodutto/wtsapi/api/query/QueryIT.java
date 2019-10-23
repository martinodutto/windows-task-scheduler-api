package cloud.martinodutto.wtsapi.api.query;

import cloud.martinodutto.wtsapi.configuration.ConfigurationParameters;
import cloud.martinodutto.wtsapi.configuration.LocalConfigurationParameters;
import cloud.martinodutto.wtsapi.exceptions.TaskNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QueryIT {

    private Properties p;

    @BeforeAll
    void beforeAll() throws IOException {
        p = new Properties();
        try (FileReader fr = new FileReader("src/test/resources/test.properties")) {
            p.load(fr);
        }
    }

    @Test
    void aNonexistentTaskCannotBeFound() {
        Query q = Query.of(LocalConfigurationParameters.getInstance());
        assertThrows(TaskNotFoundException.class, () -> q.queryForTask("I don't exist!"));
    }

    @Test
    void canQueryForAllTasks() throws IOException, InterruptedException {
        Query q = Query.of(LocalConfigurationParameters.getInstance());
        final List<Map<String, String>> tasksInfos = q.queryForAllTasks();
        assertFalse(tasksInfos.isEmpty());
    }

    @Test
    void canQueryForATask() throws IOException, InterruptedException {
        Query q = Query.of(LocalConfigurationParameters.getInstance());
        final List<Map<String, String>> tasksInfos =
                q.queryForTask("Microsoft\\Windows\\Time Synchronization\\SynchronizeTime");
        assertEquals(1, tasksInfos.size());
        final Map<String, String> row = tasksInfos.get(0);
        assertEquals("Microsoft\\Windows\\Time Synchronization\\SynchronizeTime", row.get("TaskName"));
        assertEquals("Microsoft Corporation", row.get("Author"));
    }

    @Test
    void canQueryForAllTasksOnARemoteSystem() throws IOException, InterruptedException {
        Query q = Query.of(new ConfigurationParameters() {
            @Nonnull
            @Override
            public String getSchTasksCommand() {
                return "schtasks.exe";
            }

            @Nullable
            @Override
            public String getRemoteSystem() {
                return p.getProperty("remote.system");
            }

            @Nullable
            @Override
            public String getRemoteUser() {
                return p.getProperty("remote.user");
            }

            @Nullable
            @Override
            public String getRemotePassword() {
                return p.getProperty("remote.password");
            }
        });
        final List<Map<String, String>> tasksInfos = q.queryForAllTasks();
        assertFalse(tasksInfos.isEmpty());
    }
}
