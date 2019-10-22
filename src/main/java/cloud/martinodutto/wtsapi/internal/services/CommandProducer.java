package cloud.martinodutto.wtsapi.internal.services;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;

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

        public Builder add(@Nonnull String command) {
            backingList.add(requireNonNull(command, "Invalid null command"));
            return this;
        }

        public Builder add(@Nonnull String commandParameter, @Nonnull String command) {
            backingList.add(requireNonNull(commandParameter, "Invalid null command parameter"));
            backingList.add(requireNonNull(command, "Invalid null command"));
            return this;
        }

        public Builder addIfNotNull(@Nullable String command) {
            if (command != null) {
                backingList.add(command);
            }
            return this;
        }

        public Builder addIfNotNull(@Nonnull String commandParameter, @Nullable String command) {
            if (command != null) {
                backingList.add(requireNonNull(commandParameter, "Invalid null command parameter"));
                backingList.add(command);
            }
            return this;
        }

        public CommandProducer build() {
            return new CommandProducer(backingList);
        }
    }
}
