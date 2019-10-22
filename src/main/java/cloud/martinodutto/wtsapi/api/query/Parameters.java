package cloud.martinodutto.wtsapi.api.query;

enum Parameters {
    SYSTEM("/S"),
    USERNAME("/U"),
    PASSWORD("/P"),
    FORMAT("/FO"),
    NO_HEADER("/NH"),
    VERBOSE("/V"),
    TASKNAME("/TN"),
    XML("/XML");

    private String command;

    Parameters(String command) {
        this.command = command;
    }

    String getCommand() {
        return command;
    }
}
