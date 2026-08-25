package duke.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class DeadlineTest {

    @Test
    public void toDataString_notDone_correctFormat() {
        Deadline deadline = new Deadline("return book", LocalDate.of(2026, 8, 25));
        assertEquals("D | 0 | return book | 2026-08-25", deadline.toDataString());
    }

    @Test
    public void toDataString_markedDone_correctFormat() {
        Deadline deadline = new Deadline("return book", LocalDate.of(2026, 8, 25));
        deadline.markAsDone();
        assertEquals("D | 1 | return book | 2026-08-25", deadline.toDataString());
    }

    @Test
    public void toString_notDone_correctFormat() {
        Deadline deadline = new Deadline("return book", LocalDate.of(2026, 8, 25));
        assertEquals("[D] [ ] return book (by: 2026-08-25)", deadline.toString());
    }

    @Test
    public void toString_markedDone_correctFormat() {
        Deadline deadline = new Deadline("return book", LocalDate.of(2026, 8, 25));
        deadline.markAsDone();
        assertEquals("[D] [X] return book (by: 2026-08-25)", deadline.toString());
    }
}

