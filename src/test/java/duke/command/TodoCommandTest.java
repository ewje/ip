package duke.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import duke.task.TaskList;
import duke.ui.CapturingUi;

public class TodoCommandTest {

    @Test
    public void execute_validDescription_addsTask() {
        TaskList tasks = new TaskList();
        CapturingUi ui = new CapturingUi();

        new TodoCommand("read book").execute(tasks, ui, null);

        assertNull(ui.lastErrorMessage);
        assertEquals(1, tasks.size());
        assertEquals("T | 0 | read book", tasks.getLast().toDataString());
        assertEquals(tasks.getLast(), ui.lastAddedTask);
        assertEquals(1, ui.lastAddedTaskCount);
    }

    @Test
    public void execute_descriptionWithWhitespace_trimsAndAddsTask() {
        TaskList tasks = new TaskList();
        CapturingUi ui = new CapturingUi();

        new TodoCommand("  read book  ").execute(tasks, ui, null);

        assertNull(ui.lastErrorMessage);
        assertEquals(1, tasks.size());
        assertEquals("T | 0 | read book", tasks.getLast().toDataString());
    }

    @Test
    public void execute_emptyDescription_showsError_noTaskAdded() {
        TaskList tasks = new TaskList();
        CapturingUi ui = new CapturingUi();

        new TodoCommand("   ").execute(tasks, ui, null);

        assertEquals(0, tasks.size());
        assertEquals("The Todo description cannot be empty!", ui.lastErrorMessage);
        assertNull(ui.lastAddedTask);
        assertNull(ui.lastAddedTaskCount);
    }
}

