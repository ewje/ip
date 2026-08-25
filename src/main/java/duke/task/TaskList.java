package duke.task;

import java.util.ArrayList;

import duke.exception.GaryException;
import duke.storage.Storage;

public class TaskList {
    private final ArrayList<Task> tasks;
    private Storage storage;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
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
        save();
    }

    public void addDeadline(String description, java.time.LocalDate deadline) {
        tasks.add(new Deadline(description, deadline));
        save();
    }

    public void addEvent(String description, java.time.LocalDate start, java.time.LocalDate end) {
        tasks.add(new Event(description, start, end));
        save();
    }

    public Task remove(int index) {
        Task removedTask = tasks.remove(index);
        save();
        return removedTask;
    }

    public void mark(int index, boolean isDone) {
        if (isDone) {
            tasks.get(index).markAsDone();
        } else {
            tasks.get(index).markUndone();
        }
        save();
    }

    public void validateIndex(int index) {
        if (index < 0 || index >= tasks.size()) {
            throw new GaryException("I can't find a task with that number!");
        }
    }

    public ArrayList<Task> asList() {
        return tasks;
    }

    public void save() {
        if (storage != null) {
            storage.save(tasks);
        }
    }
}
