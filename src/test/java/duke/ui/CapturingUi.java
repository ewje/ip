package duke.ui;

import java.util.ArrayList;

import duke.task.Task;

/**
 * A test double for {@link Ui} that captures method calls for assertions.
 * Keeps test output clean by not printing to stdout.
 */
public class CapturingUi extends Ui {
    public String lastErrorMessage;
    public String lastMessage;

    public boolean goodbyeShown;

    public Task lastAddedTask;
    public Integer lastAddedTaskCount;

    public Task lastRemovedTask;
    public Integer lastRemovedTaskCount;

    public Integer lastMarkedTaskNumber;
    public Boolean lastMarkedIsDone;

    public ArrayList<Task> lastShownTaskList;
    public ArrayList<Task> lastShownMatchingTasks;

    @Override
    public void showError(String message) {
        lastErrorMessage = message;
    }

    @Override
    public void showMessage(String message) {
        lastMessage = message;
    }

    @Override
    public void showGoodbye() {
        goodbyeShown = true;
    }

    @Override
    public void showTaskAdded(Task task, int taskCount) {
        lastAddedTask = task;
        lastAddedTaskCount = taskCount;
    }

    @Override
    public void showTaskRemoved(Task task, int taskCount) {
        lastRemovedTask = task;
        lastRemovedTaskCount = taskCount;
    }

    @Override
    public void showMarkedTask(int taskNumber, boolean isDone) {
        lastMarkedTaskNumber = taskNumber;
        lastMarkedIsDone = isDone;
    }

    @Override
    public void showTaskList(ArrayList<Task> tasks) {
        lastShownTaskList = tasks;
    }

    @Override
    public void showMatchingTasks(ArrayList<Task> tasks) {
        lastShownMatchingTasks = tasks;
    }

    @Override
    public void showLine() {
        // No-op: keep test output clean.
    }

    @Override
    public void showWelcome() {
        // No-op.
    }

    @Override
    public void showEmptyInputError() {
        // No-op.
    }
}
