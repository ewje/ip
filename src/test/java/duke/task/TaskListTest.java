package duke.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import duke.exception.GaryException;
import duke.storage.Storage;

public class TaskListTest {

    @TempDir
    Path tempDir;

    @Test
    public void addTodo_increasesSize_andStoresTodo() {
        TaskList tasks = new TaskList();

        tasks.addTodo("read book");

        assertEquals(1, tasks.size());
        assertEquals("T | 0 | read book", tasks.getLast().toDataString());
    }

    @Test
    public void addDeadline_increasesSize_andStoresDeadline() {
        TaskList tasks = new TaskList();

        tasks.addDeadline("return book", LocalDate.of(2026, 8, 25));

        assertEquals(1, tasks.size());
        assertEquals("D | 0 | return book | 2026-08-25", tasks.getLast().toDataString());
    }

    @Test
    public void addEvent_increasesSize_andStoresEvent() {
        TaskList tasks = new TaskList();

        tasks.addEvent("project meeting", LocalDate.of(2026, 8, 25), LocalDate.of(2026, 8, 26));

        assertEquals(1, tasks.size());
        assertEquals("E | 0 | project meeting | 2026-08-25 | 2026-08-26", tasks.getLast().toDataString());
    }

    @Test
    public void mark_marksTaskDone() {
        TaskList tasks = new TaskList();
        tasks.addTodo("read book");

        tasks.mark(0, true);

        assertEquals("T | 1 | read book", tasks.get(0).toDataString());
    }

    @Test
    public void mark_marksTaskUndone() {
        TaskList tasks = new TaskList();
        tasks.addTodo("read book");
        tasks.mark(0, true);

        tasks.mark(0, false);

        assertEquals("T | 0 | read book", tasks.get(0).toDataString());
    }

    @Test
    public void remove_removesTask_andReturnsRemovedTask() {
        TaskList tasks = new TaskList();
        tasks.addTodo("read book");
        tasks.addTodo("write notes");

        Task removed = tasks.remove(0);

        assertEquals("T | 0 | read book", removed.toDataString());
        assertEquals(1, tasks.size());
        assertEquals("T | 0 | write notes", tasks.get(0).toDataString());
    }

    @Test
    public void validateIndex_negativeIndex_throwsGaryException() {
        TaskList tasks = new TaskList();
        tasks.addTodo("read book");

        assertThrows(GaryException.class, () -> tasks.validateIndex(-1));
    }

    @Test
    public void validateIndex_outOfBounds_throwsGaryException() {
        TaskList tasks = new TaskList();
        tasks.addTodo("read book");

        assertThrows(GaryException.class, () -> tasks.validateIndex(1));
    }

    @Test
    public void findByKeyword_keywordMatches_returnsMatchingTasksInOrder() {
        TaskList tasks = new TaskList();
        tasks.addTodo("read book");
        tasks.addTodo("write notes");
        tasks.addTodo("return book");

        assertEquals(2, tasks.findByKeyword("book").size());
        assertEquals("T | 0 | read book", tasks.findByKeyword("book").get(0).toDataString());
        assertEquals("T | 0 | return book", tasks.findByKeyword("book").get(1).toDataString());
    }

    @Test
    public void findByKeyword_caseInsensitive_matchesTasks() {
        TaskList tasks = new TaskList();
        tasks.addTodo("read book");

        assertEquals(1, tasks.findByKeyword("BOOK").size());
    }

    @Test
    public void findByKeyword_noMatches_returnsEmptyList() {
        TaskList tasks = new TaskList();
        tasks.addTodo("read book");

        assertEquals(0, tasks.findByKeyword("xyz").size());
    }

    @Test
    public void undo_afterAddingTask_removesAddedTask() {
        TaskList tasks = new TaskList();
        tasks.addTodo("read book");

        assertTrue(tasks.undo());

        assertEquals(0, tasks.size());
    }

    @Test
    public void undo_afterDeletingTask_restoresExactTaskAtOriginalPosition() {
        TaskList tasks = new TaskList();
        tasks.addTodo("first");
        tasks.addDeadline("return book", LocalDate.of(2026, 8, 25));
        tasks.addTodo("last");
        tasks.mark(1, true);
        tasks.remove(1);

        assertTrue(tasks.undo());

        assertEquals(3, tasks.size());
        assertEquals("T | 0 | first", tasks.get(0).toDataString());
        assertEquals("D | 1 | return book | 2026-08-25", tasks.get(1).toDataString());
        assertEquals("T | 0 | last", tasks.get(2).toDataString());
    }

    @Test
    public void undo_afterMarkingTask_restoresPreviousStatus() {
        TaskList tasks = new TaskList();
        tasks.addTodo("read book");
        tasks.mark(0, true);

        assertTrue(tasks.undo());

        assertEquals("T | 0 | read book", tasks.get(0).toDataString());
    }

    @Test
    public void undo_afterUnmarkingTask_restoresPreviousStatus() {
        TaskList tasks = new TaskList();
        tasks.addTodo("read book");
        tasks.mark(0, true);
        tasks.mark(0, false);

        assertTrue(tasks.undo());

        assertEquals("T | 1 | read book", tasks.get(0).toDataString());
    }

    @Test
    public void undo_afterNoOpMark_skipsNoOpAndRestoresEarlierChange() {
        TaskList tasks = new TaskList();
        tasks.addTodo("read book");
        tasks.mark(0, true);
        tasks.mark(0, true);

        assertTrue(tasks.undo());

        assertEquals("T | 0 | read book", tasks.get(0).toDataString());
    }

    @Test
    public void undo_repeatedly_reversesChangesFromNewestToOldest() {
        TaskList tasks = new TaskList();
        tasks.addTodo("first");
        tasks.addTodo("second");

        assertTrue(tasks.undo());
        assertEquals(1, tasks.size());
        assertEquals("T | 0 | first", tasks.get(0).toDataString());
        assertTrue(tasks.undo());
        assertEquals(0, tasks.size());
        assertFalse(tasks.undo());
    }

    @Test
    public void undo_afterMoreThanOneHundredChanges_discardsOldestChange() {
        TaskList tasks = new TaskList();
        for (int i = 1; i <= 101; i++) {
            tasks.addTodo("task " + i);
        }

        for (int i = 0; i < 100; i++) {
            assertTrue(tasks.undo());
        }

        assertEquals(1, tasks.size());
        assertEquals("T | 0 | task 1", tasks.get(0).toDataString());
        assertFalse(tasks.undo());
    }

    @Test
    public void undo_withStorageConfigured_savesRestoredTaskList() throws Exception {
        Path file = tempDir.resolve("duke.txt");
        TaskList tasks = new TaskList();
        tasks.setStorage(new Storage(file.toString()));
        tasks.addTodo("read book");

        assertTrue(tasks.undo());

        assertEquals("", Files.readString(file));
    }

    @Test
    public void undo_afterLoadingExistingTasks_hasNoRecordedChange() {
        ArrayList<Task> existingTasks = new ArrayList<>();
        existingTasks.add(new ToDo("read book"));
        TaskList tasks = new TaskList(existingTasks);

        assertFalse(tasks.undo());
        assertEquals(1, tasks.size());
    }
}
