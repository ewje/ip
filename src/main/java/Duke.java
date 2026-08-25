import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Duke {
    private static ArrayList<Task> userList = new ArrayList<>();
    private static final Storage storage = new Storage("data/duke.txt");
    private static final Ui ui = new Ui();

    public enum Command {
        BYE, LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, UNKNOWN;

        public static Command fromString(String text) {
            try {
                return Command.valueOf(text.toUpperCase());
            } catch (IllegalArgumentException e) {
                return UNKNOWN;
            }
        }
    }

    public static void main(String[] args) {
        userList = storage.load();
        Scanner scanner = new Scanner(System.in);
        ui.showWelcome();

        while (true) {
            if (!scanner.hasNextLine()) {
                storage.save(userList);
                scanner.close();
                return;
            }

            String userInput = scanner.nextLine().trim();
            if (userInput.isEmpty()) {
                ui.showEmptyInputError();
                continue;
            }

            String[] userSplit = userInput.split(" ", 2);
            Command command = Command.fromString(userSplit[0]);
            String arguments = (userSplit.length > 1) ? userSplit[1] : "";

            try {
                switch (command) {
                    case BYE:
                        storage.save(userList);
                        ui.showGoodbye();
                        scanner.close();
                        return;
                    case LIST:
                        ui.showTaskList(userList);
                        break;
                    case MARK:
                        setTaskStatus(arguments, true);
                        break;
                    case UNMARK:
                        setTaskStatus(arguments, false);
                        break;
                    case TODO:
                        setTodo(arguments);
                        break;
                    case DEADLINE:
                        setDeadline(arguments);
                        break;
                    case EVENT:
                        setEvent(arguments);
                        break;
                    case DELETE:
                        deleteTask(arguments);
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

        if (task < 0 || task >= userList.size()) {
            throw new GaryException("I can't find a task with that number!");
        }

        if (isDone) {
            userList.get(task).markAsDone();
        } else {
            userList.get(task).markUndone();
        }
        storage.save(userList);
        ui.showMarkedTask(task + 1, isDone);
    }

    private static void setTodo(String argument) {
        String description = argument.trim();
        if (description.isEmpty()) {
            ui.showError("The Todo description cannot be empty!");
            return;
        }

        userList.add(new ToDo(description));
        storage.save(userList);
        ui.showTaskAdded(userList.getLast(), userList.size());
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
            userList.add(new Deadline(task[0].trim(), deadlineDate));
            storage.save(userList);
            ui.showTaskAdded(userList.getLast(), userList.size());
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

            userList.add(new Event(task[0].trim(), start, end));
            storage.save(userList);
            ui.showTaskAdded(userList.getLast(), userList.size());
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

            if (taskIndex < 0 || taskIndex >= userList.size()) {
                throw new GaryException("I can't find a task with that number!");
            }

            Task removedTask = userList.remove(taskIndex);
            storage.save(userList);

            ui.showTaskRemoved(removedTask, userList.size());

        } catch (NumberFormatException e) {
            throw new GaryException("Please provide a valid number.");
        }
    }

}
