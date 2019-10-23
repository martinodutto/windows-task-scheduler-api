package cloud.martinodutto.wtsapi.api.delete;

import cloud.martinodutto.wtsapi.configuration.LocalConfigurationParameters;
import cloud.martinodutto.wtsapi.exceptions.TaskNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeleteIT {

    @Test
    void canDeleteATask() {
        Delete delete = Delete.of(LocalConfigurationParameters.getInstance());
        assertDoesNotThrow(() -> delete.deleteTask("Test"));
    }

    @Test
    void cannotDeleteANonexistentTask() {
        Delete delete = Delete.of(LocalConfigurationParameters.getInstance());
        assertThrows(TaskNotFoundException.class, () -> delete.deleteTask("I don't exist!"));
    }
}
