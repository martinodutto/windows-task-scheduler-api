package cloud.martinodutto.wtsapi.query;

import cloud.martinodutto.wtsapi.CommandProducer;
import cloud.martinodutto.wtsapi.WtsInvoker;
import cloud.martinodutto.wtsapi.configuration.ConfigurationParameters;
import cloud.martinodutto.wtsapi.exceptions.TaskNotFoundException;
import cloud.martinodutto.wtsapi.exceptions.WtsInvocationException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
