package duke.parser;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import duke.command.ByeCommand;
import duke.command.Command;
import duke.command.DeadlineCommand;
import duke.command.DeleteCommand;
import duke.command.EventCommand;
import duke.command.ListCommand;
import duke.command.MarkCommand;
import duke.command.TodoCommand;
import duke.command.UnknownCommand;
import duke.task.TaskList;
import duke.ui.CapturingUi;

public class ParserTest {

    private final Parser parser = new Parser();

    @Test
    public void parse_bye_returnsByeCommand() {
        Command command = parser.parse("bye");
        assertInstanceOf(ByeCommand.class, command);
    }

    @Test
    public void parse_list_returnsListCommand() {
        Command command = parser.parse("list");
        assertInstanceOf(ListCommand.class, command);
    }

    @Test
    public void parse_mark_returnsMarkCommand() {
        Command command = parser.parse("mark 2");
        assertInstanceOf(MarkCommand.class, command);
    }

    @Test
    public void parse_unmark_returnsMarkCommand() {
        Command command = parser.parse("unmark 2");
        assertInstanceOf(MarkCommand.class, command);
    }

    @Test
    public void parse_todo_returnsTodoCommand() {
        Command command = parser.parse("todo read book");
        assertInstanceOf(TodoCommand.class, command);
    }

    @Test
    public void parse_deadline_returnsDeadlineCommand() {
        Command command = parser.parse("deadline return book /by 2026-08-25");
        assertInstanceOf(DeadlineCommand.class, command);
    }

    @Test
    public void parse_event_returnsEventCommand() {
        Command command = parser.parse("event project meeting /from 2026-08-25 /to 2026-08-26");
        assertInstanceOf(EventCommand.class, command);
    }

    @Test
    public void parse_delete_returnsDeleteCommand() {
        Command command = parser.parse("delete 1");
        assertInstanceOf(DeleteCommand.class, command);
    }

    @Test
    public void parse_unknown_returnsUnknownCommand() {
        Command command = parser.parse("what is this");
        assertInstanceOf(UnknownCommand.class, command);
    }

    @Test
    public void parse_ignoresLeadingAndTrailingWhitespace() {
        Command command = parser.parse("   list   ");
        assertInstanceOf(ListCommand.class, command);
    }

    @Test
    public void parse_isCaseInsensitive() {
        Command command = parser.parse("LiSt");
        assertInstanceOf(ListCommand.class, command);
    }

    @Test
    public void parse_deadlineWithoutByIndicator_createsCommandThatErrorsOnExecute() {
        Command command = parser.parse("deadline return book");
        assertInstanceOf(DeadlineCommand.class, command);

        TaskList tasks = new TaskList();
        CapturingUi ui = new CapturingUi();
        command.execute(tasks, ui, null);

        assertEquals(0, tasks.size());
        assertNotNull(ui.lastErrorMessage);
        assertNull(ui.lastAddedTask);
    }

    @Test
    public void parse_eventWithoutToIndicator_createsCommandThatErrorsOnExecute() {
        Command command = parser.parse("event project meeting /from 2026-08-25");
        assertInstanceOf(EventCommand.class, command);

        TaskList tasks = new TaskList();
        CapturingUi ui = new CapturingUi();
        command.execute(tasks, ui, null);

        assertEquals(0, tasks.size());
        assertNotNull(ui.lastErrorMessage);
        assertNull(ui.lastAddedTask);
    }
}
