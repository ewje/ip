package duke.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import duke.task.TaskList;
import duke.ui.CapturingUi;

public class FindCommandTest {

    @Test
    public void execute_keywordMatchesMultipleTasks_showsMatchesInOrder() {
        TaskList tasks = new TaskList();
        tasks.addTodo("read book");
        tasks.addDeadline("return book", LocalDate.of(2026, 6, 6));
        tasks.addTodo("write notes");
        tasks.mark(0, true);
        tasks.mark(1, true);

        CapturingUi ui = new CapturingUi();

        new FindCommand("book").execute(tasks, ui, null);

        assertNull(ui.lastErrorMessage);
        assertEquals(2, ui.lastShownMatchingTasks.size());
        assertEquals("[T] [X] read book", ui.lastShownMatchingTasks.get(0).toString());
        assertEquals("[D] [X] return book (by: 2026-06-06)", ui.lastShownMatchingTasks.get(1).toString());
    }

    @Test
    public void execute_keywordIsCaseInsensitive_showsMatches() {
        TaskList tasks = new TaskList();
        tasks.addTodo("read book");

        CapturingUi ui = new CapturingUi();

        new FindCommand("BOOK").execute(tasks, ui, null);

        assertEquals(1, ui.lastShownMatchingTasks.size());
        assertEquals("[T] [ ] read book", ui.lastShownMatchingTasks.get(0).toString());
    }

    @Test
    public void execute_keywordWithNoMatches_showsEmptyList() {
        TaskList tasks = new TaskList();
        tasks.addTodo("read book");

        CapturingUi ui = new CapturingUi();

        new FindCommand("xyz").execute(tasks, ui, null);

        assertNull(ui.lastErrorMessage);
        assertEquals(0, ui.lastShownMatchingTasks.size());
    }

    @Test
    public void execute_emptyKeyword_showsError_noSearchDone() {
        TaskList tasks = new TaskList();
        tasks.addTodo("read book");

        CapturingUi ui = new CapturingUi();

        new FindCommand("   ").execute(tasks, ui, null);

        assertEquals("The find keyword cannot be empty!", ui.lastErrorMessage);
        assertNull(ui.lastShownMatchingTasks);
    }
}

