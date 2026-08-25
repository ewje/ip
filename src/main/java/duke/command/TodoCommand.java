package duke.command;

import duke.storage.Storage;
import duke.task.TaskList;
import duke.ui.Ui;

public class TodoCommand extends Command {
    private final String description;

    public TodoCommand(String description) {
        this.description = description;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        String cleanedDescription = description.trim();
        if (cleanedDescription.isEmpty()) {
            ui.showError("The Todo description cannot be empty!");
            return;
        }

        tasks.addTodo(cleanedDescription);
        ui.showTaskAdded(tasks.getLast(), tasks.size());
    }
}
