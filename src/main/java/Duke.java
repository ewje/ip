import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;

public class Duke {
    private static ArrayList<Task> userList = new ArrayList<>();
    private static final Path DATA_FILE = Path.of("data", "duke.txt");

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
        loadTasks();
        Scanner scanner = new Scanner(System.in);
        String banner = """
                 ==================================================================\s
                  ____                 \s
                 / ___| __ _ _ __ _   _\s
                | |  _ / _` | '__| | | |
                | |_| | (_| | |  | |_| |
                 \\____|\\__,_|_|   \\__, |
                                  |___/\s
                 ==================================================================\s
                """;
        String greeting = """
                HELLO! I'm GARY!
                How can I help you today?
                (Type bye to exit)
                """;
        String goodbye = "Bye! Hope to see you again soon!\n";

        System.out.println(banner + greeting);

        while (true) {
            if (!scanner.hasNextLine()) {
                saveTasks();
                scanner.close();
                return;
            }

            String userInput = scanner.nextLine().trim();
            if (userInput.isEmpty()) {
                showError("Please enter a command.");
                continue;
            }

            String[] userSplit = userInput.split(" ", 2);
            Command command = Command.fromString(userSplit[0]);
            String arguments = (userSplit.length > 1) ? userSplit[1] : "";

            try {
                switch (command) {
                    case BYE:
                        saveTasks();
                        System.out.println(goodbye);
                        scanner.close();
                        return;
                    case LIST:
                        printLine();
                        System.out.println("These are the tasks you have in your list!");
                        for (int i = 0; i < userList.size(); i++) {
                            System.out.println((i + 1) + ". " + userList.get(i));
                        }
                        printLine();
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
                printLine();
                System.out.println(e.getMessage());
                printLine();
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
        saveTasks();
        printLine();
        System.out.println("Gary marked task " + (task + 1) + ((isDone) ? " as done!": " as undone!"));
        printLine();
    }

    private static void setTodo(String argument) {
        String description = argument.trim();
        if (description.isEmpty()) {
            showError("The Todo description cannot be empty!");
            return;
        }

        userList.add(new ToDo(description));
        saveTasks();
        printTask();
    }

    private static void setDeadline(String argument) {
        String[] task = argument.split(" /by ", 2);
        if (task.length < 2 || task[0].trim().isEmpty() || task[1].trim().isEmpty()) {
            showError("""
                    The Deadline description and due date cannot be empty!
                    Use ' /by ' to indicate a deadline!
                    """);
            return;
        }

        try {
            LocalDate deadlineDate = LocalDate.parse(task[1].trim());
            userList.add(new Deadline(task[0].trim(), deadlineDate));
            saveTasks();
            printTask();
        } catch (DateTimeParseException e) {
            showError("Please provide a valid deadline in the format YYYY-MM-DD.");
        }
    }

    private static void setEvent(String argument) {
        String[] task = argument.split(" /from ", 2);
        if (task.length < 2) {
            showError("""
                    The Event description and dates cannot be empty!
                    Use ' /from ' and ' /to ' to indicate start and end dates!
                    """);
            return;
        }

        String[] times = task[1].split(" /to ", 2);
        if (task[0].trim().isEmpty() || times.length < 2 || times[0].trim().isEmpty() || times[1].trim().isEmpty()) {
            showError("""
                    The Event description and dates cannot be empty!
                    Use ' /from ' and ' /to ' to indicate start and end dates!
                    """);
            return;
        }

        try{
            LocalDate start = LocalDate.parse(times[0].trim());
            LocalDate end = LocalDate.parse(times[1].trim());

            userList.add(new Event(task[0].trim(), start, end));
            saveTasks();
            printTask();
        } catch (DateTimeParseException e) {
            showError("Please provide valid start and end dates in the format YYYY-MM-DD.");
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
            saveTasks();

            printLine();
            System.out.println("Noted. This task has been removed:");
            System.out.println("  " + removedTask);
            System.out.println("Now you have " + userList.size() + " tasks in the list.");
            printLine();

        } catch (NumberFormatException e) {
            throw new GaryException("Please provide a valid number.");
        }
    }

    private static void printLine() {
        System.out.println("____________________________________________________________");
    }

    private static void printTask() {
        printLine();
        System.out.println("Got it! Here's the task you added:\n"
                + "  " + userList.getLast().toString()
                + "\nNow you have " + userList.size() + " tasks in your list!");
        printLine();
    }

    private static void printNullError() {
        System.out.println("Please include details of the task!");
    }

    private static void saveTasks() {
        try {
            Files.createDirectories(DATA_FILE.getParent());
            ArrayList<String> lines = new ArrayList<>();
            for (Task task : userList) {
                lines.add(task.toDataString());
            }
            Files.write(DATA_FILE, lines);
        } catch (IOException e) {
            throw new GaryException("Could not save tasks to disk.");
        }
    }

    private static void loadTasks() {
        if (!Files.exists(DATA_FILE)) {
            return;
        }

        try {
            List<String> lines = Files.readAllLines(DATA_FILE);
            for (String line : lines) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split("\\|", -1);
                if (parts.length < 3) {
                    continue;
                }

                String type = parts[0].trim();
                boolean isDone = parts[1].trim().equals("1");
                String description = parts[2].trim();

                Task task = switch (type) {
                    case "T" -> new ToDo(description);
                    case "D" -> {
                        if (parts.length < 4) {
                            yield null;
                        }
                        yield new Deadline(description, LocalDate.parse(parts[3].trim()));
                    }
                    case "E" -> {
                        if (parts.length < 5) {
                            yield null;
                        }
                        yield new Event(
                                description,
                                LocalDate.parse(parts[3].trim()),
                                LocalDate.parse(parts[4].trim()));
                    }
                    default -> null;
                };

                if (task != null) {
                    if (isDone) {
                        task.markAsDone();
                    }
                    userList.add(task);
                }
            }
        } catch (IOException e) {
            showError("Could not load tasks from disk.");
        }
    }

    private static void showError(String message) {
        printLine();
        System.out.println(message);
        printLine();
    }

}
