package duke.command;

import duke.exception.GaryException;
import duke.storage.Storage;
import duke.task.Task;
import duke.task.TaskList;
import duke.ui.Ui;

/**
 * Deletes a task from the task list.
 *
 * <p>The provided argument is expected to be a 1-based task number.</p>
 */
public class DeleteCommand extends Command {
    /** Raw task number argument provided by the user. */
    private final String argument;

    /**
     * Creates a delete command.
     *
     * @param argument Raw 1-based task number string.
     */
    public DeleteCommand(String argument) {
        this.argument = argument;
    }

    /**
     * Deletes the specified task and shows the removed task via the UI.
     *
     * @param tasks Task list to delete from.
     * @param ui UI used to show the result.
     * @param storage Unused.
     * @throws GaryException If the argument is blank, not a number, or out of range.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (argument.isBlank()) {
            throw new GaryException("Please indicate which task number to delete!");
        }

        try {
            int taskIndex = Integer.parseInt(argument.trim()) - 1;
            tasks.validateIndex(taskIndex);
            Task removedTask = tasks.remove(taskIndex);
            ui.showTaskRemoved(removedTask, tasks.size());
        } catch (NumberFormatException e) {
            throw new GaryException("Please provide a valid number.");
        }
    }
}
