package duke;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class DukeTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    public void getWelcomeMessage_returnsGreetingWithoutTerminalBanner() {
        Duke duke = new Duke(temporaryDirectory.resolve("duke.txt").toString());

        String welcomeMessage = duke.getWelcomeMessage();

        assertEquals("HELLO! I'm GARY!\n"
                + "How can I help you today?\n"
                + "(Type bye to exit)", welcomeMessage);
    }

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

    @Test
    public void getCommandResponse_duplicateTask_returnsErrorAndKeepsOriginal() {
        Duke duke = new Duke(temporaryDirectory.resolve("duke.txt").toString());
        duke.getCommandResponse("todo read book");

        CommandResponse duplicateResponse = duke.getCommandResponse("todo READ   BOOK");
        CommandResponse listResponse = duke.getCommandResponse("list");

        assertTrue(duplicateResponse.isError());
        assertEquals("That task already exists in your list.", duplicateResponse.text());
        assertEquals("These are the tasks you have in your list!\n1. [T] [ ] read book", listResponse.text());
    }

    @Test
    public void getCommandResponse_malformedEvent_returnsFormatError() {
        Duke duke = new Duke(temporaryDirectory.resolve("duke.txt").toString());

        CommandResponse response = duke.getCommandResponse("event meeting /from 2026-08-25");

        assertTrue(response.isError());
        assertEquals("Use: event <description> /from <YYYY-MM-DD> /to <YYYY-MM-DD>.", response.text());
    }

    @Test
    public void constructor_invalidStoredData_reportsStartupErrorAndRemainsUsable() throws Exception {
        Path taskFile = temporaryDirectory.resolve("duke.txt");
        Files.writeString(taskFile, "D | 0 | return book | 2026-02-30\n");

        Duke duke = new Duke(taskFile.toString());
        CommandResponse response = duke.getCommandResponse("list");

        assertTrue(duke.getStartupError().isPresent());
        assertTrue(duke.getStartupError().orElseThrow().contains("Task file data is invalid on line 1"));
        assertFalse(response.isError());
        assertEquals("These are the tasks you have in your list!", response.text());
    }

    @Test
    public void getCommandResponse_storageSaveFails_returnsErrorAndRollsBackTask() throws Exception {
        Path blockingParent = temporaryDirectory.resolve("not-a-directory");
        Duke duke = new Duke(blockingParent.resolve("duke.txt").toString());
        Files.writeString(blockingParent, "content");

        CommandResponse addResponse = duke.getCommandResponse("todo read book");
        CommandResponse listResponse = duke.getCommandResponse("list");

        assertTrue(addResponse.isError());
        assertEquals("Could not save tasks. Check file permissions and available disk space.", addResponse.text());
        assertEquals("These are the tasks you have in your list!", listResponse.text());
    }

    @Test
    public void getCommandResponse_byeCommandSucceeds_requestsExit() {
        Duke duke = new Duke(temporaryDirectory.resolve("duke.txt").toString());

        CommandResponse response = duke.getCommandResponse("bye");

        assertFalse(response.isError());
        assertTrue(response.shouldExit());
    }

    @Test
    public void getCommandResponse_byeSaveFails_doesNotRequestExit() throws Exception {
        Path blockingParent = temporaryDirectory.resolve("not-a-directory");
        Duke duke = new Duke(blockingParent.resolve("duke.txt").toString());
        Files.writeString(blockingParent, "content");

        CommandResponse response = duke.getCommandResponse("bye");

        assertTrue(response.isError());
        assertFalse(response.shouldExit());
        assertEquals("Could not save tasks. Check file permissions and available disk space.", response.text());
    }
}
