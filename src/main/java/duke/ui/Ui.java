package duke.ui;

import java.util.ArrayList;

import duke.task.Task;

public class Ui {
    private static final String LINE = "____________________________________________________________";

    public void showWelcome() {
        String banner = """
                 ==================================================================\s
                  ____                 \s
                 / ___| __ _ _ __ _   _\s
                | |  _ / _` | '__| | | |
                | |_| | (_| | |  | |_| |
                 \\____|\\__,_|_|   \\__, |
                                  |___/\s
                 ==================================================================\s
                """;
        String greeting = """
                HELLO! I'm GARY!
                How can I help you today?
                (Type bye to exit)
                """;
        System.out.println(banner + greeting);
    }

    public void showGoodbye() {
        System.out.println("Bye! Hope to see you again soon!\n");
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public void showEmptyInputError() {
        showError("Please enter a command.");
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showError(String message) {
        showLine();
        System.out.println(message);
        showLine();
    }

    public void showTaskAdded(Task task, int taskCount) {
        showLine();
        System.out.println("Got it! Here's the task you added:\n"
                + "  " + task
                + "\nNow you have " + taskCount + " tasks in your list!");
        showLine();
    }

    public void showTaskRemoved(Task task, int taskCount) {
        showLine();
        System.out.println("Noted. This task has been removed:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        showLine();
    }

    public void showMarkedTask(int taskNumber, boolean isDone) {
        showLine();
        System.out.println("Gary marked task " + taskNumber + (isDone ? " as done!" : " as undone!"));
        showLine();
    }

    public void showTaskList(ArrayList<Task> tasks) {
        showLine();
        System.out.println("These are the tasks you have in your list!");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
        showLine();
    }

    public void showMatchingTasks(ArrayList<Task> tasks) {
        showLine();

        if (tasks.isEmpty()) {
            System.out.println("No matching tasks found.");
            showLine();
            return;
        }

        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
        showLine();
    }
}
