package duke.task;

/**
 * Represents a generic task with a description and completion status.
 *
 * <p>Specific task types (e.g., {@link ToDo}, {@link Deadline}, {@link Event}) extend this class and may
 * override methods such as {@link #toString()} and {@link #toDataString()} to include extra details.</p>
 */
public class Task {
    /** Short description of the task, shown to the user and stored on disk. */
    protected String description;
    /** Whether the task is marked as completed. */
    protected boolean isDone;

    /**
     * Creates a new task with the given description. New tasks are initially not done.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon used in task display strings.
     *
     * @return {@code "X"} if done, otherwise a single space {@code " "}.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /** Marks this task as done. */
    public void markAsDone() {
        this.isDone = true;
    }

    /** Marks this task as not done. */
    public void markUndone() {
        this.isDone = false;
    }

    /**
     * Returns a compact string representation used for saving this task to disk.
     *
     * <p>Subclasses should override this if they need to save additional fields.</p>
     *
     * @return A single-line data string in the storage format.
     */
    public String toDataString() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Returns a user-facing representation of the task.
     *
     * @return A display string including completion status and description.
     */
    @Override
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + this.description;
    }
}
