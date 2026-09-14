package duke;

/**
 * Represents the text and display type of a response to a user command.
 *
 * @param text Text to display to the user.
 * @param isError Whether the response describes an error.
 */
public record CommandResponse(String text, boolean isError) {
}
