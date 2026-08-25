import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Duke {
    private static TaskList tasks = new TaskList();
    private static final Storage storage = new Storage("data/duke.txt");
    private static final Ui ui = new Ui();
    private static final Parser parser = new Parser();

    public static void main(String[] args) {
        tasks = new TaskList(storage.load());
        Scanner scanner = new Scanner(System.in);
        ui.showWelcome();

        while (true) {
            if (!scanner.hasNextLine()) {
                storage.save(tasks.asList());
                scanner.close();
                return;
            }

            String userInput = scanner.nextLine().trim();
            if (userInput.isEmpty()) {
                ui.showEmptyInputError();
                continue;
            }

            Parser.ParseResult parsed = parser.parse(userInput);

            try {
                switch (parsed.command()) {
                    case BYE:
                        storage.save(tasks.asList());
                        ui.showGoodbye();
                        scanner.close();
                        return;
                    case LIST:
                        ui.showTaskList(tasks.asList());
                        break;
                    case MARK:
                        setTaskStatus(parsed.arguments(), true);
                        break;
                    case UNMARK:
                        setTaskStatus(parsed.arguments(), false);
                        break;
                    case TODO:
                        setTodo(parsed.arguments());
                        break;
                    case DEADLINE:
                        setDeadline(parsed.arguments());
                        break;
                    case EVENT:
                        setEvent(parsed.arguments());
                        break;
                    case DELETE:
                        deleteTask(parsed.arguments());
                        break;
                    case UNKNOWN:
                    default:
                        throw new GaryException("I'm sorry, but Gary doesn't know what that means!");
                }
            } catch (GaryException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    private static void setTaskStatus(String argument, boolean isDone) {
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
        storage.save(tasks.asList());
        ui.showMarkedTask(task + 1, isDone);
    }

    private static void setTodo(String argument) {
        String description = argument.trim();
        if (description.isEmpty()) {
            ui.showError("The Todo description cannot be empty!");
            return;
        }

        tasks.addTodo(description);
        storage.save(tasks.asList());
        ui.showTaskAdded(tasks.getLast(), tasks.size());
    }

    private static void setDeadline(String argument) {
        String[] task = argument.split(" /by ", 2);
        if (task.length < 2 || task[0].trim().isEmpty() || task[1].trim().isEmpty()) {
            ui.showError("""
                    The Deadline description and due date cannot be empty!
                    Use ' /by ' to indicate a deadline!
                    """);
            return;
        }

        try {
            LocalDate deadlineDate = LocalDate.parse(task[1].trim());
            tasks.addDeadline(task[0].trim(), deadlineDate);
            storage.save(tasks.asList());
            ui.showTaskAdded(tasks.getLast(), tasks.size());
        } catch (DateTimeParseException e) {
            ui.showError("Please provide a valid deadline in the format YYYY-MM-DD.");
        }
    }

    private static void setEvent(String argument) {
        String[] task = argument.split(" /from ", 2);
        if (task.length < 2) {
            ui.showError("""
                    The Event description and dates cannot be empty!
                    Use ' /from ' and ' /to ' to indicate start and end dates!
                    """);
            return;
        }

        String[] times = task[1].split(" /to ", 2);
        if (task[0].trim().isEmpty() || times.length < 2 || times[0].trim().isEmpty() || times[1].trim().isEmpty()) {
            ui.showError("""
                    The Event description and dates cannot be empty!
                    Use ' /from ' and ' /to ' to indicate start and end dates!
                    """);
            return;
        }

        try{
            LocalDate start = LocalDate.parse(times[0].trim());
            LocalDate end = LocalDate.parse(times[1].trim());

            tasks.addEvent(task[0].trim(), start, end);
            storage.save(tasks.asList());
            ui.showTaskAdded(tasks.getLast(), tasks.size());
        } catch (DateTimeParseException e) {
            ui.showError("Please provide valid start and end dates in the format YYYY-MM-DD.");
        }
    }

    private static void deleteTask(String argument) {
        if (argument.isBlank()) {
            throw new GaryException("Please indicate which task number to delete!");
        }

        try {
            int taskIndex = Integer.parseInt(argument.trim()) - 1;

            tasks.validateIndex(taskIndex);

            Task removedTask = tasks.remove(taskIndex);
            storage.save(tasks.asList());

            ui.showTaskRemoved(removedTask, tasks.size());

        } catch (NumberFormatException e) {
            throw new GaryException("Please provide a valid number.");
        }
    }

}
