package duke.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.ToDo;

public class StorageTest {

    @TempDir
    Path tempDir;

    @Test
    public void load_missingFile_returnsEmptyList() {
        Path missing = tempDir.resolve("missing.txt");
        Storage storage = new Storage(missing.toString());

        ArrayList<Task> tasks = storage.load();

        assertEquals(0, tasks.size());
    }

    @Test
    public void saveThenLoad_roundTrip_preservesTasksAndDoneStatus() {
        Path file = tempDir.resolve("duke.txt");
        Storage storage = new Storage(file.toString());

        ArrayList<Task> toSave = new ArrayList<>();
        ToDo todo = new ToDo("read book");
        Deadline deadline = new Deadline("return book", LocalDate.of(2026, 8, 25));
        Event event = new Event("project meeting", LocalDate.of(2026, 8, 25), LocalDate.of(2026, 8, 26));
        deadline.markAsDone();

        toSave.add(todo);
        toSave.add(deadline);
        toSave.add(event);

        storage.save(toSave);

        ArrayList<Task> loaded = storage.load();
        assertEquals(3, loaded.size());
        assertEquals("T | 0 | read book", loaded.get(0).toDataString());
        assertEquals("D | 1 | return book | 2026-08-25", loaded.get(1).toDataString());
        assertEquals("E | 0 | project meeting | 2026-08-25 | 2026-08-26", loaded.get(2).toDataString());
    }

    @Test
    public void load_ignoresBlankAndInvalidLines() throws Exception {
        Path file = tempDir.resolve("duke.txt");
        Files.writeString(file, """

                not a valid line
                T|0
                T | 0 | read book
                D | 1 | missing date
                D | 1 | return book | 2026-08-25
                E | 0 | project meeting | 2026-08-25 | 2026-08-26
                """);
        Storage storage = new Storage(file.toString());

        ArrayList<Task> loaded = storage.load();
        assertEquals(3, loaded.size());
        assertEquals("T | 0 | read book", loaded.get(0).toDataString());
        assertEquals("D | 1 | return book | 2026-08-25", loaded.get(1).toDataString());
        assertEquals("E | 0 | project meeting | 2026-08-25 | 2026-08-26", loaded.get(2).toDataString());
    }

    @Test
    public void save_createsParentDirectories() {
        Path nested = tempDir.resolve("nested").resolve("more").resolve("duke.txt");
        Storage storage = new Storage(nested.toString());

        ArrayList<Task> toSave = new ArrayList<>();
        toSave.add(new ToDo("read book"));

        storage.save(toSave);

        assertEquals(true, Files.exists(nested));
    }

    @Test
    public void load_unknownTaskType_ignoresLine() throws Exception {
        Path file = tempDir.resolve("duke.txt");
        Files.writeString(file, """
                X | 0 | some task
                T | 0 | read book
                """);
        Storage storage = new Storage(file.toString());

        ArrayList<Task> loaded = storage.load();
        assertEquals(1, loaded.size());
        assertEquals("T | 0 | read book", loaded.get(0).toDataString());
    }

    @Test
    public void load_invalidDate_throwsDateTimeParseException() throws Exception {
        Path file = tempDir.resolve("duke.txt");
        Files.writeString(file, """
                D | 0 | return book | 2026-02-30
                """);
        Storage storage = new Storage(file.toString());

        assertThrows(DateTimeParseException.class, storage::load);
    }
}
