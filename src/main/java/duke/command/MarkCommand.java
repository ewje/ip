package duke.command;

import duke.exception.GaryException;
import duke.storage.Storage;
import duke.task.TaskList;
import duke.ui.Ui;

public class MarkCommand extends Command {
    private final String argument;
    private final boolean isDone;

    public MarkCommand(String argument, boolean isDone) {
        this.argument = argument;
        this.isDone = isDone;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (argument.isBlank()) {
            throw new GaryException("Please indicate which task number to update!");
        }

        int task;
        try {
            task = Integer.parseInt(argument.trim()) - 1;
        } catch (NumberFormatException e) {
            throw new GaryException("Please provide a valid number.");
        }

        tasks.validateIndex(task);
        tasks.mark(task, isDone);
        ui.showMarkedTask(task + 1, isDone);
    }
}
