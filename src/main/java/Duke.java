import java.util.Scanner;

public class Duke {
    private static Task[] userList = new Task[100];
    private static int count = 0;

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
            String command = userSplit[0];
            String arguments = (userSplit.length > 1) ? userSplit[1] : "";

            switch (command) {
                case "bye":
                    System.out.println(goodbye);
                    scanner.close();
                    return;
                case "list":
                    printLine();
                    System.out.println("These are the tasks you have in your list!");
                    for(int i = 0; i < count; i++) {
                        System.out.println((i + 1) + ". " + userList[i]);
                    }
                    printLine();
                    break;
                case "mark":
                    setTaskStatus(arguments, true);
                    break;
                case "unmark":
                    setTaskStatus(arguments, false);
                    break;
                case "todo":
                    setTodo(arguments);
                    break;
                case "deadline":
                    setDeadline(arguments);
                    break;
                case "event":
                    setEvent(arguments);
                    break;
                default:
                    userList[count] = new Task(userInput);
                    count++;
                    printLine();
                    System.out.println("User Input: " + userInput);
                    printLine();
                    break;
            }
        }
    }

    private static void setTaskStatus(String argument, boolean isDone) {
        if (argument.isEmpty()) {
            System.out.println("PLease indicate which task!");
        }
        int task = Integer.parseInt(argument) - 1;

        if (isDone) {
            userList[task].markAsDone();
        } else {
            userList[task].markUndone();
        }
        printLine();
        System.out.println("Gary marked task " + task + ((isDone) ? " as done!": " as undone!"));
        printLine();
    }

    private static void setTodo(String argument) {
        if (argument.isEmpty()) {
            printLine();
            System.out.println("The Todo description cannot be empty!");
        } else {
            userList[count] = new ToDo(argument);
            printTask();
        }
    }

    private static void setDeadline(String argument) {
        if (!argument.contains(" /by ")) {
            System.out.println("""
                    The Deadline description and due date cannot be empty!
                    Use ' /by ' to indicate a deadline!
                    """);
        } else {
            String[] task = argument.split("/by");

            userList[count] = new Deadline(task[0], task[1]);
            printTask();
            count++;
        }
    }

    private static void setEvent(String argument) {
        if (!argument.contains(" /from ") || !argument.contains(" /to ")) {
            System.out.println("""
                    The Event description and times cannot be empty!
                    Use ' /from ' and ' /to ' to indicate start and end times!
                    """);
        } else {
            String[] task = argument.split("/from");

            String[] times = task[1].split("/to");

            userList[count] = new Event(task[0], times[0], times[1]);
            printTask();
        }
    }

    private static void printLine() {
        System.out.println("____________________________________________________________");
    }

    private static void printTask() {
        printLine();
        System.out.println("Got it! Here's the task you added:\n"
                + "  " + userList[count].toString()
                + "\nNow you have " + (count + 1) + " tasks in your list!");
        count++;
        printLine();
    }

    private static void printNullError() {
        System.out.println("Please include details of the task!");
    }

}
