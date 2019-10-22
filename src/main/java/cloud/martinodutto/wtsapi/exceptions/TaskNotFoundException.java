package cloud.martinodutto.wtsapi.exceptions;

public class TaskNotFoundException extends WtsInvocationException {

    public TaskNotFoundException(String taskName) {
        super("The task \"" + taskName + "\" could not be found");
    }
}
