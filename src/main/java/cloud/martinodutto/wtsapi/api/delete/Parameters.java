package cloud.martinodutto.wtsapi.api.delete;

enum Parameters {
    SYSTEM("/S"),
    USERNAME("/U"),
    PASSWORD("/P"),
    TASKNAME("/TN"),
    FORCE("/F");

    private String command;

    Parameters(String command) {
        this.command = command;
    }

    String getCommand() {
        return command;
    }
}
