import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DeadlineCommand extends Command {
    private final String description;
    private final String dueDate;

    public DeadlineCommand(String description, String dueDate) {
        this.description = description;
        this.dueDate = dueDate;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (description.trim().isEmpty() || dueDate.trim().isEmpty()) {
            ui.showError("""
                    The Deadline description and due date cannot be empty!
                    Use ' /by ' to indicate a deadline!
                    """);
            return;
        }

        try {
            LocalDate deadlineDate = LocalDate.parse(dueDate.trim());
            tasks.addDeadline(description.trim(), deadlineDate);
            ui.showTaskAdded(tasks.getLast(), tasks.size());
        } catch (DateTimeParseException e) {
            ui.showError("Please provide a valid deadline in the format YYYY-MM-DD.");
        }
    }
}
