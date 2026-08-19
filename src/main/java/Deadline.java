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
}
