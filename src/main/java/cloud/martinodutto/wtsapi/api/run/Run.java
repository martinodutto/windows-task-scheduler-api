package cloud.martinodutto.wtsapi.api.run;

import cloud.martinodutto.wtsapi.configuration.ConfigurationParameters;
import cloud.martinodutto.wtsapi.exceptions.TaskNotFoundException;
import cloud.martinodutto.wtsapi.exceptions.WtsInvocationException;
import cloud.martinodutto.wtsapi.internal.services.CommandHolder;
import cloud.martinodutto.wtsapi.internal.services.WtsInvoker;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static cloud.martinodutto.wtsapi.api.run.Parameters.*;

/**
 * Wraps calls to the Windows task scheduler with parameter <code>/Run</code>.
 *
 * @see <a href="https://docs.microsoft.com/en-us/windows/win32/taskschd/schtasks#running-a-task">Microsoft documentation</a>
 */
public final class Run {

    private static final String RUN = "/Run";
    private ConfigurationParameters config;

    private Run(ConfigurationParameters config) {
        this.config = config;
    }

    public static Run of(ConfigurationParameters config) {
        return new Run(config);
    }

    public void runTask(@Nonnull String taskName) throws IOException, InterruptedException {
        runTask(taskName, false);
    }

    public void runTask(@Nonnull String taskName, boolean immediately) throws IOException, InterruptedException {
        Objects.requireNonNull(taskName, "You have to provide a task name");
        CommandHolder ch = new CommandHolder.Builder(config.getSchTasksCommand())
                .add(RUN)
                .add(TASKNAME.getCommand(), taskName)
                .addIfNotNull(immediately ? IMMEDIATELY.getCommand() : null)
                .addIfNotNull(SYSTEM.getCommand(), config.getRemoteSystem())
                .addIfNotNull(USERNAME.getCommand(), config.getRemoteUser())
                .addIfNotNull(PASSWORD.getCommand(), config.getRemotePassword())
                .build();
        List<String> commands = ch.commands();

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
