package cloud.martinodutto.wtsapi.api.run;

import cloud.martinodutto.wtsapi.configuration.LocalConfigurationParameters;
import cloud.martinodutto.wtsapi.exceptions.TaskNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RunIT {

    @Test
    void canRunATask() {
        Run run = Run.of(LocalConfigurationParameters.getInstance());
        assertDoesNotThrow(() -> run.runTask("Test"));
    }

    @Test
    void canRunATaskImmediately() {
        Run run = Run.of(LocalConfigurationParameters.getInstance());
        assertDoesNotThrow(() -> run.runTask("Test", true));
    }

    @Test
    void cannotRunANonexistentTask() {
        Run run = Run.of(LocalConfigurationParameters.getInstance());
        assertThrows(TaskNotFoundException.class, () -> run.runTask("I don't exist!"));
    }
}
