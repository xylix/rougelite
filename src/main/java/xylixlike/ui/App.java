package xylixlike.ui;

import javafx.application.Application;

import javafx.stage.Stage;
import xylixlike.engine.Level;

import static xylixlike.utils.FileOperations.loadLevel;

public class App extends Application {
    public static int SCALE = 24;

    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) {
        Level l = loadLevel("4");
        primaryStage.setScene(l.getScene());
        primaryStage.show();
    }
}
