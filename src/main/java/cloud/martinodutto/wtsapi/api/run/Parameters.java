package cloud.martinodutto.wtsapi.api.run;

enum Parameters {
    SYSTEM("/S"),
    USERNAME("/U"),
    PASSWORD("/P"),
    TASKNAME("/TN");

    private String command;

    Parameters(String command) {
        this.command = command;
    }

    String getCommand() {
        return command;
    }
}
