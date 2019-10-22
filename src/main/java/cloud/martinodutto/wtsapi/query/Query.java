package cloud.martinodutto.wtsapi.query;

import cloud.martinodutto.wtsapi.configuration.ConfigurationParameters;
import cloud.martinodutto.wtsapi.exceptions.TaskNotFoundException;
import cloud.martinodutto.wtsapi.exceptions.WtsInvocationException;
import cloud.martinodutto.wtsapi.query.parsers.CSVQueryParser;
import cloud.martinodutto.wtsapi.services.CommandProducer;
import cloud.martinodutto.wtsapi.services.WtsInvoker;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cloud.martinodutto.wtsapi.query.Parameters.*;

/**
 * A class that can be used to wrap calls to the Windows task scheduler with parameter <code>/Query</code>.
 */
public final class Query {

    private static final String QUERY = "/Query";
    private ConfigurationParameters config;

    private Query(ConfigurationParameters config) {
        this.config = config;
    }

    static Query of(ConfigurationParameters config) {
        return new Query(config);
    }

    /**
     * Queries the information for all the tasks.
     *
     * @return The information for all the tasks.
     * @throws IOException          An I/O error occurred.
     * @throws InterruptedException An interrupt was issued while expecting the output of the Windows command.
     */
    public List<Map<String, String>> queryForAllTasks() throws IOException, InterruptedException {
        CommandProducer cp = new CommandProducer.Builder(config.getSchTasksCommand())
                .add(QUERY)
                .add(FORMAT.getCommand(), "CSV")
                .add(VERBOSE.getCommand())
                .addIfNotNull(SYSTEM.getCommand(), config.getRemoteSystem())
                .addIfNotNull(USERNAME.getCommand(), config.getRemoteUser())
                .addIfNotNull(PASSWORD.getCommand(), config.getRemotePassword())
                .build();
        List<String> commands = cp.commands();
        String rawCommandOutput = WtsInvoker.submitCommands(commands);
        CSVQueryParser csvQueryParser = new CSVQueryParser();
        return csvQueryParser.parse(rawCommandOutput);
    }

    /**
     * Queries the information for a task.
     *
     * <p>This method returns a <em>list</em> of results because multiple triggers could be defined for the same task.</p>
     *
     * @param taskName The name of the task to query for.
     * @return The information for this task.
     * @throws IOException          An I/O error occurred.
     * @throws InterruptedException An interrupt was issued while expecting the output of the Windows command.
     */
    public List<Map<String, String>> queryForTask(String taskName) throws IOException, InterruptedException {
        Objects.requireNonNull(taskName, "You have to provide a task name");
        CommandProducer cp = new CommandProducer.Builder(config.getSchTasksCommand())
                .add(QUERY)
                .add(FORMAT.getCommand(), "CSV")
                .add(TASKNAME.getCommand(), "\"" + taskName + "\"")
                .add(VERBOSE.getCommand())
                .build();
        List<String> commands = cp.commands();

        String rawCommandOutput;
        try {
            rawCommandOutput = WtsInvoker.submitCommands(commands);
        } catch (WtsInvocationException wie) {
            if ("ERROR: The system cannot find the file specified.".equals(wie.getMessage().trim())) {
                throw new TaskNotFoundException(taskName);
            } else {
                throw wie;
            }
        }
        CSVQueryParser csvQueryParser = new CSVQueryParser();
        return csvQueryParser.parse(rawCommandOutput);
    }
}
