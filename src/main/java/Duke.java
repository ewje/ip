import java.util.ArrayList;
import java.util.Scanner;

public class Duke {
    private static ArrayList<Task> userList = new ArrayList<>();

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
            String userInput = scanner.nextLine().trim();

            String[] userSplit = userInput.split(" ", 2);
            Command command = Command.fromString(userSplit[0]);
            String arguments = (userSplit.length > 1) ? userSplit[1] : "";

            try {
                switch (command) {
                    case BYE:
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
                    case UNMARK":
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
        if (argument.isEmpty()) {
            System.out.println("PLease indicate which task!");
        }
        int task = Integer.parseInt(argument) - 1;

        if (isDone) {
            userList.get(task).markAsDone();
        } else {
            userList.get(task).markUndone();
        }
        printLine();
        System.out.println("Gary marked task " + (task + 1) + ((isDone) ? " as done!": " as undone!"));
        printLine();
    }

    private static void setTodo(String argument) {
        if (argument.isEmpty()) {
            printLine();
            System.out.println("The Todo description cannot be empty!");
            printLine();
        } else {
            userList.add(new ToDo(argument));
            printTask();
        }
    }

    private static void setDeadline(String argument) {
        if (!argument.contains(" /by ")) {
            printLine();
            System.out.println("""
                    The Deadline description and due date cannot be empty!
                    Use ' /by ' to indicate a deadline!
                    """);
            printLine();
        } else {
            String[] task = argument.split("/by");

            userList.add(new Deadline(task[0], task[1]));
            printTask();
        }
    }

    private static void setEvent(String argument) {
        if (!argument.contains(" /from ") || !argument.contains(" /to ")) {
            printLine();
            System.out.println("""
                    The Event description and times cannot be empty!
                    Use ' /from ' and ' /to ' to indicate start and end times!
                    """);
            printLine();
        } else {
            String[] task = argument.split("/from");

            String[] times = task[1].split("/to");

            userList.add(new Event(task[0], times[0], times[1]));
            printTask();
        }
    }

    private static void deleteTask(String argument) {
        if (argument.isEmpty()) {
            throw new GaryException("Please indicate which task number to delete!");
        }

        try {
            int taskIndex = Integer.parseInt(argument) - 1;

            if (taskIndex < 0 || taskIndex >= userList.size()) {
                throw new GaryException("I can't find a task with that number!");
            }

            Task removedTask = userList.remove(taskIndex);

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

}
