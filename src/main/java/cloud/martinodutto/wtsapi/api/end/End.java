package cloud.martinodutto.wtsapi.api.end;

import cloud.martinodutto.wtsapi.configuration.ConfigurationParameters;
import cloud.martinodutto.wtsapi.exceptions.TaskNotFoundException;
import cloud.martinodutto.wtsapi.exceptions.WtsInvocationException;
import cloud.martinodutto.wtsapi.internal.services.CommandProducer;
import cloud.martinodutto.wtsapi.internal.services.WtsInvoker;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static cloud.martinodutto.wtsapi.api.end.Parameters.*;

/**
 * Wraps calls to the Windows task scheduler with parameter <code>/End</code>.
 *
 * @see <a href="https://docs.microsoft.com/en-us/windows/win32/taskschd/schtasks#ending-a-running-task">Microsoft documentation</a>
 */
public final class End {

    private static final String END = "/End";
    private ConfigurationParameters config;

    private End(ConfigurationParameters config) {
        this.config = config;
    }

    static End of(ConfigurationParameters config) {
        return new End(config);
    }

    public void endTask(@Nonnull String taskName) throws IOException, InterruptedException {
        Objects.requireNonNull(taskName, "You have to provide a task name");
        CommandProducer cp = new CommandProducer.Builder(config.getSchTasksCommand())
                .add(END)
                .add(TASKNAME.getCommand(), taskName)
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
