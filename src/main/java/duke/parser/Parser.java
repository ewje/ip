package duke.parser;

import duke.command.ByeCommand;
import duke.command.Command;
import duke.command.DeadlineCommand;
import duke.command.DeleteCommand;
import duke.command.EventCommand;
import duke.command.FindCommand;
import duke.command.ListCommand;
import duke.command.MarkCommand;
import duke.command.TodoCommand;
import duke.command.UnknownCommand;

/**
 * Parses user input lines into {@link duke.command.Command} objects.
 *
 * <p>This class is responsible for:
 * <ul>
 *   <li>Identifying the command keyword (the first token).</li>
 *   <li>Extracting the argument string (the remainder of the line).</li>
 *   <li>Constructing the corresponding {@code Command} object.</li>
 * </ul>
 *
 * <p>Validation of argument correctness (e.g., date formats, missing fields) is delegated to
 * the individual {@code Command} implementations.</p>
 */
public class Parser {

    /**
     * Parses a full user input line and returns a {@link Command} representing that input.
     *
     * @param userInput Full user input line.
     * @return A {@code Command} instance; returns {@link UnknownCommand} if the command keyword is not recognised.
     */
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
            case "FIND" -> new FindCommand(arguments);
            default -> new UnknownCommand();
        };
    }

    /**
     * Parses the arguments for a {@code deadline} command.
     *
     * <p>Expected format: {@code <description> /by <YYYY-MM-DD>}</p>
     *
     * @param arguments The raw argument string after the command keyword.
     * @return A {@link DeadlineCommand} carrying the extracted description and due date strings.
     */
    private Command parseDeadline(String arguments) {
        String[] parts = arguments.split(" /by ", 2);
        String description = parts.length > 0 ? parts[0] : "";
        String dueDate = parts.length > 1 ? parts[1] : "";
        return new DeadlineCommand(description, dueDate);
    }

    /**
     * Parses the arguments for an {@code event} command.
     *
     * <p>Expected format: {@code <description> /from <YYYY-MM-DD> /to <YYYY-MM-DD>}</p>
     *
     * @param arguments The raw argument string after the command keyword.
     * @return An {@link EventCommand} carrying the extracted description, start date string, and end date string.
     */
    private Command parseEvent(String arguments) {
        String[] fromParts = arguments.split(" /from ", 2);
        String description = fromParts.length > 0 ? fromParts[0] : "";
        String[] timeParts = (fromParts.length > 1) ? fromParts[1].split(" /to ", 2) : new String[0];
        String start = timeParts.length > 0 ? timeParts[0] : "";
        String end = timeParts.length > 1 ? timeParts[1] : "";
        return new EventCommand(description, start, end);
    }
}
