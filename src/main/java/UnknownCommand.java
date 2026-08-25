public class UnknownCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        throw new GaryException("I'm sorry, but Gary doesn't know what that means!");
    }
}
