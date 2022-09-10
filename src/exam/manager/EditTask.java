package exam.manager;

import java.util.List;

public class EditTask {
    private final List<String> types;
    private final Task task;

    public EditTask(Task task, List<String> types) {
        this.task = task;
        this.types = types;
    }

    public Task getTask() {
        return task;
    }

    public List<String> getTypes() {
        return types;
    }

}
