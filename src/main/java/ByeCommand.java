public class ByeCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.save();
        ui.showGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
