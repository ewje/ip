package duke.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import duke.exception.GaryException;

public class TaskListTest {

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
}
