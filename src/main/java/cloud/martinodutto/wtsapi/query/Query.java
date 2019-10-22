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

/**
 * A class that can be used to wrap calls to the Windows task scheduler with parameter <code>/Query</code>.
 */
public final class Query {

    private ConfigurationParameters config;

    private Query(ConfigurationParameters config) {
        this.config = config;
    }

    static Query of(ConfigurationParameters config) {
        return new Query(config);
    }

    public List<Map<String, String>> queryForAllTasks() throws IOException, InterruptedException {
        CommandProducer cp = new CommandProducer.Builder(config.getSchTasksCommand())
                .add("/Query")
                .add("/FO")
                .add("CSV")
                .add("/V")
                .build();
        List<String> commands = cp.commands();
        String rawCommandOutput = WtsInvoker.submitCommands(commands);
        CSVQueryParser csvQueryParser = new CSVQueryParser();
        return csvQueryParser.parse(rawCommandOutput);
    }

    public List<Map<String, String>> queryForTask(String taskName) throws IOException, InterruptedException {
        Objects.requireNonNull(taskName, "You have to provide a task name");
        CommandProducer cp = new CommandProducer.Builder(config.getSchTasksCommand())
                .add("/Query")
                .add("/FO")
                .add("CSV")
                .add("/TN")
                .add("\"" + taskName + "\"")
                .add("/V")
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
