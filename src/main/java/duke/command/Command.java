package duke.command;

import duke.storage.Storage;
import duke.task.TaskList;
import duke.ui.Ui;

/**
 * Represents an executable user command.
 *
 * <p>Each command implementation encapsulates the logic required to handle a specific user instruction
 * (e.g., add a task, list tasks, exit the application).</p>
 */
public abstract class Command {

    /**
     * Executes the command.
     *
     * <p>Implementations may mutate the task list and/or display messages via the {@link Ui}. The
     * {@code storage} parameter is provided for completeness, but most commands should interact with
     * persistence through {@link TaskList} instead.</p>
     *
     * @param tasks The task list to operate on.
     * @param ui The UI used to show messages/errors.
     * @param storage Storage instance used for persistence (may be {@code null} and may be unused).
     */
    public void execute(TaskList tasks, Ui ui, Storage storage) {
    }

    /**
     * Indicates whether executing this command should cause the application to exit.
     *
     * @return {@code true} if the application should terminate after this command, otherwise {@code false}.
     */
    public boolean isExit() {
        return false;
    }
}
