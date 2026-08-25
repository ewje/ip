package duke.command;

import duke.exception.GaryException;
import duke.storage.Storage;
import duke.task.TaskList;
import duke.ui.Ui;

public class UnknownCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        throw new GaryException("I'm sorry, but Gary doesn't know what that means!");
    }
}
