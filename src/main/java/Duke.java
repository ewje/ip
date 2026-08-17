public class Duke {
    public static void main(String[] args) {
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

        System.out.println(banner + greeting + goodbye);

    }
}
