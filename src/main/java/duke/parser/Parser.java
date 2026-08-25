package duke.parser;

import duke.command.*;

public class Parser {
    public Command parse(String userInput) {
        String[] userSplit = userInput.trim().split(" ", 2);
        String commandWord = userSplit[0].toUpperCase();
        String arguments = (userSplit.length > 1) ? userSplit[1] : "";

        return switch (commandWord) {
            case "BYE" -> new ByeCommand();
            case "LIST" -> new ListCommand();
            case "MARK" -> new MarkCommand(arguments, true);
            case "UNMARK" -> new MarkCommand(arguments, false);
            case "TODO" -> new TodoCommand(arguments);
            case "DEADLINE" -> parseDeadline(arguments);
            case "EVENT" -> parseEvent(arguments);
            case "DELETE" -> new DeleteCommand(arguments);
            default -> new UnknownCommand();
        };
    }

    private Command parseDeadline(String arguments) {
        String[] parts = arguments.split(" /by ", 2);
        String description = parts.length > 0 ? parts[0] : "";
        String dueDate = parts.length > 1 ? parts[1] : "";
        return new DeadlineCommand(description, dueDate);
    }

    private Command parseEvent(String arguments) {
        String[] fromParts = arguments.split(" /from ", 2);
        String description = fromParts.length > 0 ? fromParts[0] : "";
        String[] timeParts = (fromParts.length > 1) ? fromParts[1].split(" /to ", 2) : new String[0];
        String start = timeParts.length > 0 ? timeParts[0] : "";
        String end = timeParts.length > 1 ? timeParts[1] : "";
        return new EventCommand(description, start, end);
    }
}
