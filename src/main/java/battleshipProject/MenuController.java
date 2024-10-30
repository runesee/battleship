package battleshipProject;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class MenuController {
    private final Stage currentStage;
    private File fileToGameController;
    @FXML
    private Button continueGame;
    @FXML
    private Button newGame;
    @FXML
    private Button exitButton;

    public MenuController() {
        currentStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
            loader.setController(this);
            currentStage.setScene(new Scene(loader.load()));
            currentStage.setTitle("Battleship - Main menu");
        } catch (IOException e) {}
        this.initialize();
    }

    public void showStage() {
        this.currentStage.showAndWait();
    }

    private void initialize() {
        this.continueGame.setOnAction(event -> openFileChooser());
        this.newGame.setOnAction(event -> openNewGameLayout());
        this.exitButton.setOnAction(event -> exitGame());
    }

    private void openNewGameLayout() {
        PopUpController popUpController = new PopUpController();
        popUpController.showStage();
        this.currentStage.close();
    }

    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
        String path = new File("").getAbsolutePath();
        try {
            fileChooser.setInitialDirectory(new File(path + "/src/main/java/battleshipProject/saves"));
        } catch (Exception e) {}
        try {
            File file = fileChooser.showOpenDialog(this.currentStage);
            Game game = new Game(file); // Oppretter et game for å sjekke om fila er gyldig, gir exception hvis ikke
            this.fileToGameController = file;
            this.openContinueGameLayout(game.getMap().getMapSize());
        } catch (Exception e) {}
    }

    private void openContinueGameLayout(int size) {
        GameController gameController = new GameController(this.getFileToGameController(), size);
        gameController.showStage();
        this.currentStage.close();
    }

    public File getFileToGameController() {
        return this.fileToGameController;
    }

    @FXML
    private void exitGame() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}