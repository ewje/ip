package duke.task;

import java.time.LocalDate;

/**
 * Represents a deadline task with a description and a due date.
 */
public class Deadline extends Task {
    /** Due date of the deadline. */
    private LocalDate deadline;
    
    /**
     * Creates a deadline task.
     *
     * @param description Description of the deadline.
     * @param date Due date of the deadline.
     */
    public Deadline(String description, LocalDate date) {
        super(description);
        this.deadline = date;
    }

    /**
     * Returns a user-facing representation of a deadline task.
     *
     * @return A display string prefixed with {@code [D]} and including the due date.
     */
    @Override
    public String toString() {
        return "[D] " + super.toString() + " (by: " + this.deadline + ")";
    }

    /**
     * Returns the storage format for a deadline task.
     *
     * @return A single-line data string containing type, done flag, description, and due date.
     */
    @Override
    public String toDataString() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + deadline;
    }
}
