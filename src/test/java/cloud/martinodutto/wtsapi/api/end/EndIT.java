package cloud.martinodutto.wtsapi.api.end;

import cloud.martinodutto.wtsapi.api.run.Run;
import cloud.martinodutto.wtsapi.configuration.LocalConfigurationParameters;
import cloud.martinodutto.wtsapi.exceptions.TaskNotFoundException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EndIT {

    @Test
    void canEndARunningTask() throws IOException, InterruptedException {
        Run run = Run.of(LocalConfigurationParameters.getInstance());
        run.runTask("LONG_RUNNING_TASK", true);
        Thread.sleep(2000);
        End end = End.of(LocalConfigurationParameters.getInstance());
        assertDoesNotThrow(() -> end.endTask("LONG_RUNNING_TASK"));
    }

    @Test
    void canEndANonRunningTask() {
        End end = End.of(LocalConfigurationParameters.getInstance());
        assertDoesNotThrow(() -> end.endTask("LONG_RUNNING_TASK"));
    }

    @Test
    void cannotEndANonExistentTask() {
        End end = End.of(LocalConfigurationParameters.getInstance());
        assertThrows(TaskNotFoundException.class, () -> end.endTask("I don't exist!"));
    }
}
