public class Event extends Task {
    private String start;
    private String end;

    public Event(String description, String start, String end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "[E] " + super.toString() + " (from: " + this.start + " to: " + this.end + ")";
     }

    @Override
    public String toDataString() {
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " + start + " | " + end;
    }
}
