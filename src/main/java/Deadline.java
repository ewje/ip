public class Deadline extends Task {
    private String deadline;
    public Deadline(String description, String date) {
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
