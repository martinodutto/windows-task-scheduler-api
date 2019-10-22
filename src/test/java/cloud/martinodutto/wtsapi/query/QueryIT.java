package cloud.martinodutto.wtsapi.query;

import cloud.martinodutto.wtsapi.configuration.DefaultConfigurationParameters;
import cloud.martinodutto.wtsapi.exceptions.TaskNotFoundException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class QueryIT {

    @Test
    void anInexistentTaskCannotBeFound() {
        Query q = Query.of(DefaultConfigurationParameters.getInstance());
        assertThrows(TaskNotFoundException.class, () -> q.queryForTask("I don't exist!"));
    }

    @Test
    void canQueryForAllTasks() throws IOException, InterruptedException {
        Query q = Query.of(DefaultConfigurationParameters.getInstance());
        final List<Map<String, String>> tasksInfos = q.queryForAllTasks();
        assertFalse(tasksInfos.isEmpty());
    }

    @Test
    void canQueryForATask() throws IOException, InterruptedException {
        Query q = Query.of(DefaultConfigurationParameters.getInstance());
        final List<Map<String, String>> tasksInfos =
                q.queryForTask("Microsoft\\Windows\\Time Synchronization\\SynchronizeTime");
        assertEquals(1, tasksInfos.size());
        final Map<String, String> row = tasksInfos.get(0);
        assertEquals("Microsoft\\Windows\\Time Synchronization\\SynchronizeTime", row.get("TaskName"));
        assertEquals("Microsoft Corporation", row.get("Author"));
    }
}
