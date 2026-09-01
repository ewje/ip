package duke.gui;

import javafx.application.Application;

/**
 * Launches the JavaFX GUI.
 * <p>
 * This separate launcher avoids some JavaFX runtime edge cases when launching directly from an {@link Application}
 * subclass in certain build/run setups.
 */
public class GuiLauncher {

    /**
     * Launches {@link GuiApp}.
     *
     * @param args CLI arguments (unused for now).
     */
    public static void main(String[] args) {
        Application.launch(GuiApp.class, args);
    }
}

