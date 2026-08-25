package duke.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import duke.exception.GaryException;
import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.ToDo;

/**
 * Handles persistence of tasks to and from a text file on disk.
 *
 * <p>The storage file is line-based. Each line represents one task in the following formats:
 * <ul>
 *   <li>Todo: {@code T | <0/1> | <description>}</li>
 *   <li>Deadline: {@code D | <0/1> | <description> | <YYYY-MM-DD>}</li>
 *   <li>Event: {@code E | <0/1> | <description> | <YYYY-MM-DD> | <YYYY-MM-DD>}</li>
 * </ul>
 *
 * <p>For invalid/blank lines, this class skips the line. For file I/O failures, it throws a
 * {@link GaryException} with a user-friendly message.</p>
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates a {@code Storage} that reads from/writes to the given path.
     *
     * @param filePath Path to the storage file (e.g., {@code data/duke.txt}).
     */
    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /**
     * Loads all tasks from disk.
     *
     * <p>If the file does not exist yet, returns an empty list.</p>
     *
     * @return List of tasks loaded from disk.
     * @throws GaryException If an I/O error occurs while reading the file.
     */
    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return tasks;
        }

        try {
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                Task task = parseTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            throw new GaryException("Could not load tasks from disk.");
        }

        return tasks;
    }

    /**
     * Saves the given list of tasks to disk, overwriting any existing file.
     *
     * <p>Parent directories are created automatically if they do not exist.</p>
     *
     * @param tasks List of tasks to save.
     * @throws GaryException If an I/O error occurs while writing the file.
     */
    public void save(ArrayList<Task> tasks) {
        try {
            Files.createDirectories(filePath.getParent());
            ArrayList<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(task.toDataString());
            }
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new GaryException("Could not save tasks to disk.");
        }
    }

    /**
     * Parses a single line of the storage file into a {@link Task}.
     *
     * @param line A single line from the storage file.
     * @return The parsed {@code Task}, or {@code null} if the line is blank/invalid.
     */
    private Task parseTask(String line) {
        if (line.isBlank()) {
            return null;
        }

        String[] parts = line.split("\\|", -1);
        if (parts.length < 3) {
            return null;
        }

        String type = parts[0].trim();
        boolean isDone = parts[1].trim().equals("1");
        String description = parts[2].trim();

        Task task = switch (type) {
            case "T" -> new ToDo(description);
            case "D" -> {
                if (parts.length < 4) {
                    yield null;
                }
                yield new Deadline(description, LocalDate.parse(parts[3].trim()));
            }
            case "E" -> {
                if (parts.length < 5) {
                    yield null;
                }
                yield new Event(
                        description,
                        LocalDate.parse(parts[3].trim()),
                        LocalDate.parse(parts[4].trim()));
            }
            default -> null;
        };

        if (task != null && isDone) {
            task.markAsDone();
        }
        return task;
    }
}
