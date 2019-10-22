package cloud.martinodutto.wtsapi.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CommandProducer {

    private final List<String> commandList;

    private CommandProducer(List<String> commandList) {
        this.commandList = commandList;
    }

    public List<String> commands() {
        return Collections.unmodifiableList(commandList);
    }

    public static class Builder {

        private final List<String> backingList = new ArrayList<>();

        public Builder(String schTasksCommand) {
            this.backingList.add(schTasksCommand);
        }

        public Builder add(String command) {
            backingList.add(command);
            return this;
        }

        public CommandProducer build() {
            return new CommandProducer(backingList);
        }
    }
}
