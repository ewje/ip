package duke.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TaskTest {

    @Test
    public void getStatusIcon_initiallyNotDone_returnsSpace() {
        Task task = new Task("read book");
        assertEquals(" ", task.getStatusIcon());
    }

    @Test
    public void getStatusIcon_markedDone_returnsX() {
        Task task = new Task("read book");
        task.markAsDone();
        assertEquals("X", task.getStatusIcon());
    }

    @Test
    public void markUndone_afterMarkDone_resetsStatus() {
        Task task = new Task("read book");
        task.markAsDone();
        task.markUndone();
        assertEquals(" ", task.getStatusIcon());
    }

    @Test
    public void toDataString_notDone_correctFormat() {
        Task task = new Task("read book");
        assertEquals("T | 0 | read book", task.toDataString());
    }

    @Test
    public void toDataString_done_correctFormat() {
        Task task = new Task("read book");
        task.markAsDone();
        assertEquals("T | 1 | read book", task.toDataString());
    }

    @Test
    public void toString_notDone_correctFormat() {
        Task task = new Task("read book");
        assertEquals("[ ] read book", task.toString());
    }

    @Test
    public void toString_done_correctFormat() {
        Task task = new Task("read book");
        task.markAsDone();
        assertEquals("[X] read book", task.toString());
    }

    @Test
    public void toString_emptyDescription_keepsSpaceAfterBracket() {
        Task task = new Task("");
        assertEquals("[ ] ", task.toString());
    }
}

