package battleshipProject;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class PopUpController {
    private Stage currentStage;
    private int sizeToSetupController;

    @FXML
    RadioButton r1;

    @FXML
    Button startButton;

    public PopUpController() {
        this.currentStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PopUp.fxml"));
            loader.setController(this);
            currentStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {}
        currentStage.setTitle("Battleship - Create new game");
        startButton.setOnAction(event -> startNewGame());
        showStage();
    }

    private void startNewGame() {
        if (r1.isSelected()) {
            this.sizeToSetupController = 7;
        } else {
            this.sizeToSetupController = 10;
        }
        SetupController setupController = new SetupController(this.sizeToSetupController);
        setupController.showStage();
        this.currentStage.close();
    }

    public void showStage() {
        this.currentStage.show();
    }
}