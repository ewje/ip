public class Parser {
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

    public record ParseResult(Command command, String arguments) {
    }

    public ParseResult parse(String userInput) {
        String[] userSplit = userInput.trim().split(" ", 2);
        Command command = Command.fromString(userSplit[0]);
        String arguments = (userSplit.length > 1) ? userSplit[1] : "";
        return new ParseResult(command, arguments);
    }
}
