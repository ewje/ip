import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public int size() {
        return tasks.size();
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public Task getLast() {
        return tasks.getLast();
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public void addTodo(String description) {
        tasks.add(new ToDo(description));
    }

    public void addDeadline(String description, java.time.LocalDate deadline) {
        tasks.add(new Deadline(description, deadline));
    }

    public void addEvent(String description, java.time.LocalDate start, java.time.LocalDate end) {
        tasks.add(new Event(description, start, end));
    }

    public Task remove(int index) {
        return tasks.remove(index);
    }

    public void mark(int index, boolean isDone) {
        if (isDone) {
            tasks.get(index).markAsDone();
        } else {
            tasks.get(index).markUndone();
        }
    }

    public void validateIndex(int index) {
        if (index < 0 || index >= tasks.size()) {
            throw new GaryException("I can't find a task with that number!");
        }
    }

    public ArrayList<Task> asList() {
        return tasks;
    }
}
