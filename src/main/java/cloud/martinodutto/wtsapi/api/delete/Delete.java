package cloud.martinodutto.wtsapi.api.delete;

import cloud.martinodutto.wtsapi.configuration.ConfigurationParameters;
import cloud.martinodutto.wtsapi.exceptions.TaskNotFoundException;
import cloud.martinodutto.wtsapi.exceptions.WtsInvocationException;
import cloud.martinodutto.wtsapi.internal.services.CommandProducer;
import cloud.martinodutto.wtsapi.internal.services.WtsInvoker;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static cloud.martinodutto.wtsapi.api.delete.Parameters.*;

/**
 * Wraps calls to the Windows task scheduler with parameter <code>/Delete</code>.
 *
 * @see <a href="https://docs.microsoft.com/en-us/windows/win32/taskschd/schtasks#deleting-a-task">Microsoft documentation</a>
 */
public final class Delete {

    private static final String DELETE = "/Delete";
    private ConfigurationParameters config;

    private Delete(ConfigurationParameters config) {
        this.config = config;
    }

    static Delete of(ConfigurationParameters config) {
        return new Delete(config);
    }

    public void deleteTask(@Nonnull String taskName) throws IOException, InterruptedException {
        deleteTask(taskName, false);
    }

    public void deleteTask(@Nonnull String taskName, boolean force) throws IOException, InterruptedException {
        Objects.requireNonNull(taskName, "You have to provide a task name");
        CommandProducer cp = new CommandProducer.Builder(config.getSchTasksCommand())
                .add(DELETE)
                .add(TASKNAME.getCommand(), taskName)
                .addIfNotNull(force ? FORCE.getCommand() : null)
                .addIfNotNull(SYSTEM.getCommand(), config.getRemoteSystem())
                .addIfNotNull(USERNAME.getCommand(), config.getRemoteUser())
                .addIfNotNull(PASSWORD.getCommand(), config.getRemotePassword())
                .build();
        List<String> commands = cp.commands();

        try {
            WtsInvoker.submitCommands(commands);
        } catch (WtsInvocationException wie) {
            if ("ERROR: The system cannot find the file specified.".equals(wie.getMessage().trim())) {
                throw new TaskNotFoundException(taskName);
            } else {
                throw wie;
            }
        }
    }
}
