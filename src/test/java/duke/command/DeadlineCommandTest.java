package duke.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import duke.task.TaskList;
import duke.ui.CapturingUi;

public class DeadlineCommandTest {

    @Test
    public void execute_validDeadline_addsTask() {
        TaskList tasks = new TaskList();
        CapturingUi ui = new CapturingUi();

        new DeadlineCommand("return book", "2026-08-25").execute(tasks, ui, null);

        assertNull(ui.lastErrorMessage);
        assertEquals(1, tasks.size());
        assertEquals("D | 0 | return book | 2026-08-25", tasks.getLast().toDataString());
        assertEquals(tasks.getLast(), ui.lastAddedTask);
        assertEquals(1, ui.lastAddedTaskCount);
    }

    @Test
    public void execute_validDeadline_withWhitespace_addsTask() {
        TaskList tasks = new TaskList();
        CapturingUi ui = new CapturingUi();

        new DeadlineCommand("  return book  ", "  2026-08-25  ").execute(tasks, ui, null);

        assertNull(ui.lastErrorMessage);
        assertEquals(1, tasks.size());
        assertEquals("D | 0 | return book | 2026-08-25", tasks.getLast().toDataString());
        assertEquals(tasks.getLast(), ui.lastAddedTask);
        assertEquals(1, ui.lastAddedTaskCount);
    }

    @Test
    public void execute_leapDayValid_addsTask() {
        TaskList tasks = new TaskList();
        CapturingUi ui = new CapturingUi();

        new DeadlineCommand("pay taxes", "2024-02-29").execute(tasks, ui, null);

        assertNull(ui.lastErrorMessage);
        assertEquals(1, tasks.size());
        assertEquals("D | 0 | pay taxes | 2024-02-29", tasks.getLast().toDataString());
        assertEquals(tasks.getLast(), ui.lastAddedTask);
        assertEquals(1, ui.lastAddedTaskCount);
    }

    @Test
    public void execute_validDeadline_nonPaddedMonthDay_showsError_noTaskAdded() {
        TaskList tasks = new TaskList();
        CapturingUi ui = new CapturingUi();

        new DeadlineCommand("return book", "2026-8-5").execute(tasks, ui, null);

        assertEquals(0, tasks.size());
        assertEquals("Please provide a valid deadline in the format YYYY-MM-DD.", ui.lastErrorMessage);
        assertNull(ui.lastAddedTask);
        assertNull(ui.lastAddedTaskCount);
    }

    @Test
    public void execute_emptyDescription_showsError_noTaskAdded() {
        TaskList tasks = new TaskList();
        CapturingUi ui = new CapturingUi();

        new DeadlineCommand("   ", "2026-08-25").execute(tasks, ui, null);

        assertEquals(0, tasks.size());
        assertEquals("""
                The Deadline description and due date cannot be empty!
                Use ' /by ' to indicate a deadline!
                """, ui.lastErrorMessage);
        assertNull(ui.lastAddedTask);
        assertNull(ui.lastAddedTaskCount);
    }

    @Test
    public void execute_emptyDueDate_showsError_noTaskAdded() {
        TaskList tasks = new TaskList();
        CapturingUi ui = new CapturingUi();

        new DeadlineCommand("return book", "   ").execute(tasks, ui, null);

        assertEquals(0, tasks.size());
        assertEquals("""
                The Deadline description and due date cannot be empty!
                Use ' /by ' to indicate a deadline!
                """, ui.lastErrorMessage);
        assertNull(ui.lastAddedTask);
        assertNull(ui.lastAddedTaskCount);
    }

    @Test
    public void execute_invalidFormatSlashDate_showsError_noTaskAdded() {
        TaskList tasks = new TaskList();
        CapturingUi ui = new CapturingUi();

        new DeadlineCommand("return book", "2026/08/25").execute(tasks, ui, null);

        assertEquals(0, tasks.size());
        assertEquals("Please provide a valid deadline in the format YYYY-MM-DD.", ui.lastErrorMessage);
        assertNull(ui.lastAddedTask);
        assertNull(ui.lastAddedTaskCount);
    }

    @Test
    public void execute_invalidMonth_showsError_noTaskAdded() {
        TaskList tasks = new TaskList();
        CapturingUi ui = new CapturingUi();

        new DeadlineCommand("return book", "2026-13-01").execute(tasks, ui, null);

        assertEquals(0, tasks.size());
        assertEquals("Please provide a valid deadline in the format YYYY-MM-DD.", ui.lastErrorMessage);
        assertNull(ui.lastAddedTask);
        assertNull(ui.lastAddedTaskCount);
    }

    @Test
    public void execute_invalidDay_showsError_noTaskAdded() {
        TaskList tasks = new TaskList();
        CapturingUi ui = new CapturingUi();

        new DeadlineCommand("return book", "2026-02-30").execute(tasks, ui, null);

        assertEquals(0, tasks.size());
        assertEquals("Please provide a valid deadline in the format YYYY-MM-DD.", ui.lastErrorMessage);
        assertNull(ui.lastAddedTask);
        assertNull(ui.lastAddedTaskCount);
    }

    @Test
    public void execute_validPrefixButExtraText_showsError_noTaskAdded() {
        TaskList tasks = new TaskList();
        CapturingUi ui = new CapturingUi();

        new DeadlineCommand("return book", "2026-08-25 extra").execute(tasks, ui, null);

        assertEquals(0, tasks.size());
        assertEquals("Please provide a valid deadline in the format YYYY-MM-DD.", ui.lastErrorMessage);
        assertNull(ui.lastAddedTask);
        assertNull(ui.lastAddedTaskCount);
    }

}
