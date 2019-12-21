package xylixlike.utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;


public class FXUtils {
    private FXUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Button createButton(String name, EventHandler<ActionEvent> action) {
        Button button = new Button(name);
        button.setOnAction(action);
        button.setId(name);
        return button;
    }
}
