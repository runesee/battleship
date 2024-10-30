package battleshipProject;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class BattleshipProjectApp extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        MenuController menuController = new MenuController();
        menuController.showStage();
    }
}