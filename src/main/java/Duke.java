import java.util.Scanner;

public class Duke {
    private static void printLine() {
        System.out.println("____________________________________________________________");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String userInput;
        Task[] userList = new Task[100];
        int count = 0;

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
            userInput = scanner.nextLine().trim();
            String[] userSplit = userInput.split(" ");

            switch (userSplit[0]) {
                case "bye":
                    System.out.println(goodbye);
                    return;
                case "list":
                    printLine();
                    for(int i = 0; i < count; i++) {
                        System.out.println((i + 1) + ". " + userList[i]);
                    }
                    printLine();
                    break;
                case "mark":
                    if(userSplit.length < 2) {
                        System.out.println("Please indicate which task to mark!");
                        break;
                    }

                    int done = Integer.parseInt(userSplit[1]);

                    userList[done - 1].markAsDone();

                    printLine();
                    System.out.println("Gary marked task " + done + " as done!");
                    printLine();

                    break;
                case "unmark":
                    if(userSplit.length < 2) {
                        System.out.println("Please indicate which task to unmark!");
                        break;
                    }

                    int undone = Integer.parseInt(userSplit[1]);

                    userList[undone - 1].markUndone();

                    printLine();
                    System.out.println("Gary marked task " + undone + " as undone!");
                    printLine();

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
}
