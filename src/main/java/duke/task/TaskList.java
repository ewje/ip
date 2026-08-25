package duke.task;

import java.util.ArrayList;

import duke.exception.GaryException;
import duke.storage.Storage;

/**
 * Represents the in-memory list of tasks for the application.
 *
 * <p>This class provides operations to add, remove, and update tasks. If a {@link Storage} is configured,
 * mutating operations will automatically trigger {@link #save()}.</p>
 */
public class TaskList {
    /** Underlying list of tasks. Indices are zero-based. */
    private final ArrayList<Task> tasks;
    /** Storage used for persistence; may be {@code null} if persistence is not configured. */
    private Storage storage;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list backed by the given existing list.
     *
     * @param tasks Existing tasks to use as the initial contents.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Sets the storage used by this task list for persistence.
     *
     * @param storage Storage instance to use.
     */
    public void setStorage(Storage storage) {
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
        tasks.add(task);
    }

    /**
     * Adds a todo task and saves the updated list (if storage is configured).
     *
     * @param description Description of the todo.
     */
    public void addTodo(String description) {
        tasks.add(new ToDo(description));
        save();
    }

    /**
     * Adds a deadline task and saves the updated list (if storage is configured).
     *
     * @param description Description of the deadline.
     * @param deadline Due date.
     */
    public void addDeadline(String description, java.time.LocalDate deadline) {
        tasks.add(new Deadline(description, deadline));
        save();
    }

    /**
     * Adds an event task and saves the updated list (if storage is configured).
     *
     * @param description Description of the event.
     * @param start Start date.
     * @param end End date.
     */
    public void addEvent(String description, java.time.LocalDate start, java.time.LocalDate end) {
        tasks.add(new Event(description, start, end));
        save();
    }

    /**
     * Removes and returns the task at the given index, then saves the updated list (if storage is configured).
     *
     * @param index Zero-based index.
     * @return The removed task.
     */
    public Task remove(int index) {
        Task removedTask = tasks.remove(index);
        save();
        return removedTask;
    }

    /**
     * Marks the task at the given index as done/undone, then saves the updated list (if storage is configured).
     *
     * @param index Zero-based index.
     * @param isDone {@code true} to mark as done, {@code false} to mark as not done.
     */
    public void mark(int index, boolean isDone) {
        if (isDone) {
            tasks.get(index).markAsDone();
        } else {
            tasks.get(index).markUndone();
        }
        save();
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
     * Saves the current tasks to disk if storage is configured.
     */
    public void save() {
        if (storage != null) {
            storage.save(tasks);
        }
    }
}
