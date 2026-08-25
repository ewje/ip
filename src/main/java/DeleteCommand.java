public class DeleteCommand extends Command {
    private final String argument;

    public DeleteCommand(String argument) {
        this.argument = argument;
    }

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
