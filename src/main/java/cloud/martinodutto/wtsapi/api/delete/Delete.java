package cloud.martinodutto.wtsapi.api.delete;

import cloud.martinodutto.wtsapi.configuration.ConfigurationParameters;
import cloud.martinodutto.wtsapi.exceptions.TaskNotFoundException;
import cloud.martinodutto.wtsapi.exceptions.WtsInvocationException;
import cloud.martinodutto.wtsapi.internal.services.CommandHolder;
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

    public static Delete of(ConfigurationParameters config) {
        return new Delete(config);
    }

    public void deleteTask(@Nonnull String taskName) throws IOException, InterruptedException {
        Objects.requireNonNull(taskName, "You have to provide a task name");
        CommandHolder ch = new CommandHolder.Builder(config.getSchTasksCommand())
                .add(DELETE)
                .add(TASKNAME.getCommand(), taskName)
                .add(FORCE.getCommand()) // must force the deletion, to avoid prompting the user
                .addIfNotNull(SYSTEM.getCommand(), config.getRemoteSystem())
                .addIfNotNull(USERNAME.getCommand(), config.getRemoteUser())
                .addIfNotNull(PASSWORD.getCommand(), config.getRemotePassword())
                .build();
        List<String> commands = ch.commands();

        try {
            WtsInvoker.submitCommands(commands);
        } catch (WtsInvocationException wie) {
            if (wie.getMessage().trim().matches("ERROR: The specified task name \".*\" does not exist in the system.")
                    || wie.getMessage().trim().equals("ERROR: The system cannot find the file specified.")) {
                throw new TaskNotFoundException(taskName);
            } else {
                throw wie;
            }
        }
    }
}
