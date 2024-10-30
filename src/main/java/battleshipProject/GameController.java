package battleshipProject;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;

public class GameController {
    private Stage currentStage;
    private List<Button> enemyButtonArray = new ArrayList<>();
    private List<Button> playerButtonArray = new ArrayList<>();
    private Map<BattleMap, List<Button>> mapToButtonHashmap = new HashMap<>();
    private Collection<Button> disabledButtons = new ArrayList<>();
    private Map<Button, Point2D> pointOfButton = new HashMap<>();
    private File gameFile;
    private Game game;
    private Button lastClicked;

    @FXML
    private Button shootButton;

    @FXML
    private Text winner;

    @FXML
    private Text rounds;

    @FXML
    private Button saveButton;

    public GameController(File file, int size) {
        this.gameFile = file;
        this.currentStage = new Stage();
        this.game = new Game(this.gameFile);
        try {
            FXMLLoader loader;
            if (size == 7) {
                loader = new FXMLLoader(getClass().getResource("Grid7xGame.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("Grid10xGame.fxml"));
            }
            loader.setController(this);
            this.currentStage.setScene(new Scene(loader.load()));
            this.currentStage.setTitle("Battleship");
            this.initialize(size);
        } catch (IOException e) {}
    }

    private void initialize(int size) {
        try {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    Button enemyButton = (Button) this.currentStage.getScene().lookup("#b" + String.valueOf(i) + String.valueOf(j));
                    Button playerButton = (Button) this.currentStage.getScene().lookup("#c" + String.valueOf(i) + String.valueOf(j));
                    Point2D tempPoint = new Point2D(j, i);
                    this.enemyButtonArray.add(enemyButton);
                    this.playerButtonArray.add(playerButton);
                    this.pointOfButton.put(enemyButton, tempPoint);
                    if (this.game.getEnemyMap().getMap()[i][j] == 'x') {
                        this.disabledButtons.add(enemyButton);
                    }
                }
            }
            this.mapToButtonHashmap.put(this.game.getEnemyMap(), this.enemyButtonArray);
            this.mapToButtonHashmap.put(this.game.getMap(), this.playerButtonArray);
            updateMap(this.game.getEnemyMap());
            updateMap(this.game.getMap());
            this.shootButton.setDisable(true);
            this.disableButtons();
        } catch (Exception e) {
            throw new IllegalArgumentException("Given size must be either 7 or 10!");
        }
        this.shootButton.setOnAction(event -> shoot());
        this.saveButton.setOnAction(event -> saveGame());
        for (Button button : this.enemyButtonArray) {
            button.setOnAction(event -> indexClicked(event));
        }
        this.winner.setText("Winner: " + determineWinner());
    }

    private void saveGame() {
        TextInputDialog textInputDialog = new TextInputDialog("defaultsave.txt");
        textInputDialog.setHeaderText("Please choose a filename ending in .txt:");
        textInputDialog.showAndWait();
        String result = textInputDialog.getResult();
        if (result != null) {
            this.game.saveGame(result);
        }
    }

    public void showStage() {
        this.currentStage.show();
    }

    @FXML
    private void indexClicked(ActionEvent e) {
        if (!isGameFinished()) {
            if (this.lastClicked != null) {
                if (!((this.lastClicked.getStyle().equals("-fx-background-radius: 0; -fx-background-color: red;"))
                        || (this.lastClicked.getStyle()
                                .equals("-fx-background-radius: 0; -fx-background-color: green;")))) {
                    this.lastClicked.setStyle("-fx-background-radius: 0;");
                }
            }
            this.lastClicked = (Button) e.getSource();
            this.lastClicked.setStyle("-fx-background-radius: 0; -fx-background-color: darkgray;");
            if (this.shootButton.isDisabled()) {
                this.shootButton.setDisable(false);
            }
        }
    }

    @FXML
    private void shoot() {
        if (!isGameFinished()) {
            if (this.game.getCounter() % 2 == 0) {
                this.disabledButtons.add(this.lastClicked);
                Point2D point = this.pointOfButton.get(this.lastClicked);
                this.game.shoot(point, this.game.getEnemyMap());
                this.updateText();
                this.updateMap(this.game.getEnemyMap());
                if (this.game.isGameOver(this.game.getEnemyMap())) {
                    this.disableButtons();
                    return;
                }
            }
            this.randomizedShootFunction();
            this.updateText();
            this.updateMap(this.game.getMap());
            this.shootButton.setDisable(true);
            this.disableButtons();
        }
        // Hvis spillet er fullført:
        else {
            for (Button button : enemyButtonArray) {
                if (!button.isDisabled()) {
                    button.setDisable(true);
                }
            }
            if (!this.shootButton.isDisabled()) {
                this.shootButton.setDisable(true);
            }
        }
    }

    private void disableButtons() {
        for (Button button : this.enemyButtonArray) {
            button.setDisable(false);
            if (this.disabledButtons.contains(button)) {
                button.setDisable(true);
            }
        }
    }

    private boolean isGameFinished() {
        return (this.game.isGameOver(this.game.getMap()) || this.game.isGameOver(this.game.getEnemyMap()));
    }

    private String determineWinner() {
        if (this.game.isGameOver(this.game.getEnemyMap())) {
            return "Player";
        } else if (this.game.isGameOver(this.game.getMap())) {
            return "Enemy (PC)";
        } else {
            return "";
        }
    }

    private void randomizedShootFunction() {
        this.game.randomizedShoot(this.game.getMap());
    }

    private void updateMap(BattleMap map) {
        List<Button> tempButtonArray = this.mapToButtonHashmap.get(map);
        for (int i = 0; i < map.getMapSize(); i++) {
            for (int j = 0; j < map.getMapSize(); j++) {
                Button enemyButton = (Button) this.currentStage.getScene().lookup("#b" + String.valueOf(i) + String.valueOf(j));
                Button playerButton = (Button) this.currentStage.getScene().lookup("#c" + String.valueOf(i) + String.valueOf(j));
                if (map.getMap()[i][j] == 'x') {
                    if (tempButtonArray.contains(enemyButton)) {
                        Battleship battleship = game.locateAssociatedBattleship(new Point2D(j, i), map);
                        if (battleship != null) {
                            enemyButton.setStyle("-fx-background-radius: 0; -fx-background-color: green;");
                        } else {
                            enemyButton.setStyle("-fx-background-radius: 0; -fx-background-color: red;");
                        }
                    } else if (tempButtonArray.contains(playerButton)) {
                        Battleship battleship = game.locateAssociatedBattleship(new Point2D(j, i), map);
                        if (battleship != null) {
                            playerButton.setStyle("-fx-background-radius: 0; -fx-background-color: green;");
                        } else {
                            playerButton.setStyle("-fx-background-radius: 0; -fx-background-color: red;");
                        }
                    }
                }
            }
        }
    }

    private void updateText() {
        this.rounds.setText("Total shots: " + this.game.getCounter());
        if ((this.game.isGameOver(this.game.getMap()) == true)) {
            this.winner.setText("Winner: Enemy (PC)");
        } else if ((this.game.isGameOver(this.game.getEnemyMap()) == true)) {
            this.winner.setText("Winner: Player");
        }
    }
}
