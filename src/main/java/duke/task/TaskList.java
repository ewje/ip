package duke.task;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.stream.Collectors;

import duke.exception.GaryException;
import duke.storage.Storage;

/**
 * Represents the in-memory list of tasks for the application.
 *
 * <p>This class provides operations to add, remove, and update tasks. If a {@link Storage} is configured,
 * mutating operations will automatically trigger {@link #save()}.</p>
 */
public class TaskList {
    private static final int MAX_UNDO_HISTORY_SIZE = 100;
    /** Underlying list of tasks. Indices are zero-based. */
    private final ArrayList<Task> tasks;
    /** Changes that can be undone, with the most recent change first. */
    private final Deque<UndoAction> undoHistory;
    /** Storage used for persistence; may be {@code null} if persistence is not configured. */
    private Storage storage;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
        this.undoHistory = new ArrayDeque<>();
    }

    /**
     * Creates a task list backed by the given existing list.
     *
     * @param tasks Existing tasks to use as the initial contents.
     */
    public TaskList(ArrayList<Task> tasks) {
        assert tasks != null : "Initial task list must not be null";
        assert tasks.stream().noneMatch(task -> task == null) : "Initial task list must not contain null tasks";

        this.tasks = tasks;
        this.undoHistory = new ArrayDeque<>();
    }

    /**
     * Sets the storage used by this task list for persistence.
     *
     * @param storage Storage instance to use.
     */
    public void setStorage(Storage storage) {
        assert storage != null : "Configured storage must not be null";

        this.storage = storage;
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return Number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task at the given index.
     *
     * @param index Zero-based index.
     * @return The task at {@code index}.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the most recently added task.
     *
     * @return Last task in the list.
     */
    public Task getLast() {
        assert !tasks.isEmpty() : "Cannot get the last task from an empty list";

        return tasks.getLast();
    }

    /**
     * Adds a task to the list without auto-saving.
     *
     * <p>This is mainly useful for non-user-triggered additions (e.g., loading from disk). For user commands,
     * prefer the typed add methods that auto-save.</p>
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        assert task != null : "Task to add must not be null";

        tasks.add(task);
    }

    /**
     * Adds a todo task and saves the updated list (if storage is configured).
     *
     * @param description Description of the todo.
     */
    public void addTodo(String description) {
        assert description != null && !description.isBlank() : "Todo description must be non-blank";

        Task addedTask = new ToDo(description);
        tasks.add(addedTask);
        save();
        recordUndo(() -> tasks.remove(addedTask));
    }

    /**
     * Adds a deadline task and saves the updated list (if storage is configured).
     *
     * @param description Description of the deadline.
     * @param deadline Due date.
     */
    public void addDeadline(String description, java.time.LocalDate deadline) {
        assert description != null && !description.isBlank() : "Deadline description must be non-blank";
        assert deadline != null : "Deadline date must not be null";

        Task addedTask = new Deadline(description, deadline);
        tasks.add(addedTask);
        save();
        recordUndo(() -> tasks.remove(addedTask));
    }

    /**
     * Adds an event task and saves the updated list (if storage is configured).
     *
     * @param description Description of the event.
     * @param start Start date.
     * @param end End date.
     */
    public void addEvent(String description, java.time.LocalDate start, java.time.LocalDate end) {
        assert description != null && !description.isBlank() : "Event description must be non-blank";
        assert start != null : "Event start date must not be null";
        assert end != null : "Event end date must not be null";

        Task addedTask = new Event(description, start, end);
        tasks.add(addedTask);
        save();
        recordUndo(() -> tasks.remove(addedTask));
    }

    /**
     * Removes and returns the task at the given index, then saves the updated list (if storage is configured).
     *
     * @param index Zero-based index.
     * @return The removed task.
     */
    public Task remove(int index) {
        assert index >= 0 && index < tasks.size() : "Removal index must have been validated";

        Task removedTask = tasks.remove(index);
        save();
        recordUndo(() -> tasks.add(index, removedTask));
        return removedTask;
    }

    /**
     * Marks the task at the given index as done/undone, then saves the updated list (if storage is configured).
     *
     * @param index Zero-based index.
     * @param isDone {@code true} to mark as done, {@code false} to mark as not done.
     */
    public void mark(int index, boolean isDone) {
        assert index >= 0 && index < tasks.size() : "Mark index must have been validated";

        Task task = tasks.get(index);
        boolean wasDone = task.isDone;
        if (wasDone == isDone) {
            save();
            return;
        }

        if (isDone) {
            task.markAsDone();
        } else {
            task.markUndone();
        }
        save();
        recordUndo(() -> setDone(task, wasDone));
    }

    /**
     * Reverses the most recent task-list change and saves the restored list.
     *
     * @return {@code true} if a change was undone, or {@code false} if no undoable change exists.
     */
    public boolean undo() {
        if (undoHistory.isEmpty()) {
            return false;
        }

        UndoAction action = undoHistory.removeFirst();
        action.undo();
        save();
        return true;
    }

    /**
     * Validates that the index is within the current task list bounds.
     *
     * @param index Zero-based index.
     * @throws GaryException If {@code index} is out of range.
     */
    public void validateIndex(int index) {
        if (index < 0 || index >= tasks.size()) {
            throw new GaryException("I can't find a task with that number!");
        }
    }

    /**
     * Returns the underlying list of tasks.
     *
     * <p>Callers should treat the returned list as read-only to avoid bypassing validation and persistence.</p>
     *
     * @return The underlying task list.
     */
    public ArrayList<Task> asList() {
        return tasks;
    }

    /**
     * Finds tasks whose descriptions contain the given keyword.
     *
     * <p>Matching is case-insensitive. The returned list preserves the order of tasks in the task list.</p>
     *
     * @param keyword Keyword to search for.
     * @return List of matching tasks.
     */
    public ArrayList<Task> findByKeyword(String keyword) {
        assert keyword != null && !keyword.isBlank() : "Search keyword must be non-blank";

        String keywordInLowerCase = keyword.toLowerCase();
        return tasks.stream()
                .filter(task -> task.description.toLowerCase().contains(keywordInLowerCase))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Saves the current tasks to disk if storage is configured.
     */
    public void save() {
        if (storage != null) {
            storage.save(tasks);
        }
    }

    private void recordUndo(UndoAction action) {
        assert action != null : "Undo action must not be null";

        if (undoHistory.size() == MAX_UNDO_HISTORY_SIZE) {
            undoHistory.removeLast();
        }
        undoHistory.addFirst(action);
    }

    private void setDone(Task task, boolean isDone) {
        if (isDone) {
            task.markAsDone();
        } else {
            task.markUndone();
        }
    }

    /** Reverses one recorded task-list change without recording another change. */
    @FunctionalInterface
    private interface UndoAction {
        /** Reverses the recorded change. */
        void undo();
    }
}
