package duke.command;

import duke.exception.GaryException;
import duke.storage.Storage;
import duke.task.TaskList;
import duke.ui.Ui;

/**
 * Represents an unrecognised command.
 *
 * <p>Executing this command throws a {@link GaryException} with a user-friendly message.</p>
 */
public class UnknownCommand extends Command {

    /**
     * Always throws a {@link GaryException} because the user input was not understood.
     *
     * @param tasks Unused.
     * @param ui Unused.
     * @param storage Unused.
     * @throws GaryException Always thrown.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        throw new GaryException("I'm sorry, but Gary doesn't know what that means!");
    }
}
