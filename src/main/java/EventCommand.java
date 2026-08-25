import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class EventCommand extends Command {
    private final String description;
    private final String start;
    private final String end;

    public EventCommand(String description, String start, String end) {
        this.description = description;
        this.start = start;
        this.end = end;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (description.trim().isEmpty() || start.trim().isEmpty() || end.trim().isEmpty()) {
            ui.showError("""
                    The Event description and dates cannot be empty!
                    Use ' /from ' and ' /to ' to indicate start and end dates!
                    """);
            return;
        }

        try {
            LocalDate startDate = LocalDate.parse(start.trim());
            LocalDate endDate = LocalDate.parse(end.trim());
            tasks.addEvent(description.trim(), startDate, endDate);
            ui.showTaskAdded(tasks.getLast(), tasks.size());
        } catch (DateTimeParseException e) {
            ui.showError("Please provide valid start and end dates in the format YYYY-MM-DD.");
        }
    }
}
