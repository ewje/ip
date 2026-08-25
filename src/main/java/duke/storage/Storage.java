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

public class Storage {
    private final Path filePath;

    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

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
