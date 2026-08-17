import java.util.Scanner;

public class Duke {
    private static void printLine() {
        System.out.println("____________________________________________________________");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String userInput;
        String[] userList = new String[100];
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
            userInput = scanner.nextLine();

            switch (userInput) {
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
                default:
                    userList[count] = userInput;
                    count++;
                    printLine();
                    System.out.println("User Input: " + userInput);
                    printLine();
                    break;
            }

        }
    }
}
