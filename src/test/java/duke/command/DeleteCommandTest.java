package duke.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import duke.exception.GaryException;
import duke.task.TaskList;
import duke.ui.CapturingUi;

public class DeleteCommandTest {

    @Test
    public void execute_blankArgument_throwsGaryException() {
        TaskList tasks = new TaskList();
        CapturingUi ui = new CapturingUi();

        GaryException exception = assertThrows(GaryException.class, () ->
                new DeleteCommand("   ").execute(tasks, ui, null));

        assertEquals("Please indicate which task number to delete!", exception.getMessage());
    }

    @Test
    public void execute_nonNumber_throwsGaryException() {
        TaskList tasks = new TaskList();
        tasks.addTodo("read book");
        CapturingUi ui = new CapturingUi();

        GaryException exception = assertThrows(GaryException.class, () ->
                new DeleteCommand("abc").execute(tasks, ui, null));

        assertEquals("Please provide a valid number.", exception.getMessage());
    }

    @Test
    public void execute_outOfRange_throwsGaryException() {
        TaskList tasks = new TaskList();
        tasks.addTodo("read book");
        CapturingUi ui = new CapturingUi();

        GaryException exception = assertThrows(GaryException.class, () ->
                new DeleteCommand("2").execute(tasks, ui, null));

        assertEquals("I can't find a task with that number!", exception.getMessage());
    }

    @Test
    public void execute_validIndex_removesTask() {
        TaskList tasks = new TaskList();
        tasks.addTodo("read book");
        tasks.addTodo("write notes");
        CapturingUi ui = new CapturingUi();

        new DeleteCommand("1").execute(tasks, ui, null);

        assertEquals(1, tasks.size());
        assertEquals("T | 0 | write notes", tasks.get(0).toDataString());

        assertEquals("T | 0 | read book", ui.getLastRemovedTask().toDataString());
        assertEquals(1, ui.getLastRemovedTaskCount());
        assertNull(ui.getLastErrorMessage());
    }
}
