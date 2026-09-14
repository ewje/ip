package duke;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class DukeTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    public void getCommandResponse_unknownCommand_returnsErrorResponse() {
        Duke duke = new Duke(temporaryDirectory.resolve("duke.txt").toString());

        CommandResponse response = duke.getCommandResponse("unknown");

        assertTrue(response.isError());
        assertEquals("I'm sorry, but Gary doesn't know what that means!", response.text());
    }

    @Test
    public void getCommandResponse_invalidCommandArguments_returnsErrorResponse() {
        Duke duke = new Duke(temporaryDirectory.resolve("duke.txt").toString());

        CommandResponse response = duke.getCommandResponse("todo");

        assertTrue(response.isError());
        assertEquals("The Todo description cannot be empty!", response.text());
    }

    @Test
    public void getCommandResponse_validCommand_returnsStandardResponse() {
        Duke duke = new Duke(temporaryDirectory.resolve("duke.txt").toString());

        CommandResponse response = duke.getCommandResponse("todo read book");

        assertFalse(response.isError());
        assertEquals("Got it! Here's the task you added:\n"
                + "  [T] [ ] read book\n"
                + "Now you have 1 tasks in your list!", response.text());
    }

    @Test
    public void getCommandResponse_emptyInput_returnsErrorResponse() {
        Duke duke = new Duke(temporaryDirectory.resolve("duke.txt").toString());

        CommandResponse response = duke.getCommandResponse("   ");

        assertTrue(response.isError());
        assertEquals("Please enter a command.", response.text());
    }
}
