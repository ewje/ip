package duke.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * A minimal JavaFX application used to verify that the project is set up correctly for a GUI.
 */
public class GuiApp extends Application {

    @Override
    public void start(Stage stage) {
        Label label = new Label("Hello, GARY (JavaFX)!");
        StackPane root = new StackPane(label);

        Scene scene = new Scene(root, 480, 240);
        stage.setTitle("GARY");
        stage.setScene(scene);
        stage.show();
    }
}

