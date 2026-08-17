import java.util.Scanner;

public class Duke {
    private static void printLine() {
        System.out.println("____________________________________________________________");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String user;

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
            user = scanner.nextLine();

            if (user.equals("bye")) {
                System.out.println(goodbye);
                break;
            }
            printLine();
            System.out.println("User Input: " + user);
            printLine();
        }
    }
}
