package duke;

import java.util.Scanner;

import duke.command.Command;
import duke.exception.GaryException;
import duke.parser.Parser;
import duke.storage.Storage;
import duke.task.TaskList;
import duke.ui.Ui;

public class Duke {
    private final TaskList tasks;
    private final Ui ui;
    private final Parser parser;

    public Duke(String filePath) {
        this.ui = new Ui();
        Storage storage = new Storage(filePath);
        this.parser = new Parser();
        this.tasks = new TaskList(storage.load());
        this.tasks.setStorage(storage);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        ui.showWelcome();

        boolean isExit = false;
        while (!isExit) {
            try {
                if (!scanner.hasNextLine()) {
                    tasks.save();
                    break;
                }

                String fullCommand = scanner.nextLine().trim();
                if (fullCommand.isEmpty()) {
                    ui.showEmptyInputError();
                    continue;
                }

                Command command = parser.parse(fullCommand);
                command.execute(tasks, ui, null);
                isExit = command.isExit();
            } catch (GaryException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
        scanner.close();
    }

    public static void main(String[] args) {
        new Duke("data/duke.txt").run();
    }
}
