package duke.task;

import java.time.LocalDate;

public class Deadline extends Task {
    private LocalDate deadline;
    
    public Deadline(String description, LocalDate date) {
        super(description);
        this.deadline = date;
    }

    @Override
    public String toString() {
        return "[D] " + super.toString() + " (by: " + this.deadline + ")";
    }

    @Override
    public String toDataString() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + deadline;
    }
}
