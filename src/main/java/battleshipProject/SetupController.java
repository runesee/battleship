package battleshipProject;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;

public class SetupController {
    private Stage currentStage;
    private int gameSize;
    private List<Button> buttonArray = new ArrayList<>();
    private List<Button> shipButtons = new ArrayList<>();
    private List<Button> disabledButtons = new ArrayList<>();
    private List<Button> possibleEndpoins = new ArrayList<>();
    private Game game;
    private Game clonedGame;
    private Map<Button, Point2D> pointOfButton = new HashMap<>();
    private Map<Point2D, Button> buttonOfPoint = new HashMap<>();
    private Button lastClickedShip;
    private Button lastClicked;
    private int counter = 0;

    @FXML
    private Button resetButton;

    @FXML
    private Button startGame;

    public SetupController(int sizeToSetupController) {
        this.gameSize = sizeToSetupController;
        this.currentStage = new Stage();
        this.game = new Game(sizeToSetupController);
        this.clonedGame = new Game(sizeToSetupController);

        try {
            FXMLLoader loader;
            if (sizeToSetupController == 7) {
                loader = new FXMLLoader(getClass().getResource("Grid7xSetup.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("Grid10xSetup.fxml"));
            }
            loader.setController(this);
            this.currentStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {}
        this.currentStage.setTitle("Battleship - Setup phase");
        initialize();
    }

    private void initialize() {
        for (int i = 0; i < this.gameSize; i++) {
            for (int j = 0; j < this.gameSize; j++) {
                Button button = (Button) this.currentStage.getScene().lookup("#a" + String.valueOf(i) + String.valueOf(j));
                Point2D tempPoint = new Point2D(j, i);
                this.buttonArray.add(button);
                this.pointOfButton.put(button, tempPoint);
                this.buttonOfPoint.put(tempPoint, button);
                button.setOnAction(event -> indexClicked(event));
                button.setDisable(true);
            }
        }
        int tempSize = 0;
        if (this.gameSize == 7) {
            tempSize = 4;
        } else {
            tempSize = 5;
        }
        for (int i = 0; i < tempSize; i++) {
            Button shipButton = (Button) this.currentStage.getScene().lookup("#ship" + String.valueOf(i));
            this.shipButtons.add(shipButton);
            shipButton.setOnAction(event -> shipClicked(event));
        }
        this.resetButton.setOnAction(event -> resetBoard(event));
        this.startGame.setOnAction(event -> startNewGame());
    }

    @FXML
    private void shipClicked(ActionEvent e) {
        for (Button button : this.buttonArray) {
            if (!(this.disabledButtons.contains(button))) {
                button.setDisable(false);
            }
        }
        for (Button shipButton : this.shipButtons) {
            shipButton.setDisable(true);
        }
        this.lastClickedShip = (Button) e.getSource();
    }

    @FXML
    private void startNewGame() {
        File file = new File("");
        String path = file.getAbsolutePath();
        // Benytter defaultsave.txt for oppretting av nytt game
        GameController gameController = new GameController(
                new File(path + "/src/main/java/battleshipProject/saves/defaultsave.txt"), this.gameSize);
        gameController.showStage();
        this.currentStage.close();
    }

    public void showStage() {
        this.currentStage.show();
    }

    @FXML
    private void indexClicked(ActionEvent e) {
        updateMap();
        Button currentButton = (Button) e.getSource();
        int shipSize = Integer.parseInt(this.lastClickedShip.getText()) - 1;
        if (this.counter % 2 == 0) {
            // Hvis du velger startpunkt:
            this.disabledButtons.add(currentButton);
            currentButton.setStyle("-fx-background-radius: 0; -fx-background-color: yellow;");
            currentButton.setDisable(true);
            Point2D point = this.pointOfButton.get(currentButton);
            Point2D newPoint = null;
            boolean tempBool;

            if (point.getX() > shipSize - 1) {
                tempBool = false;
                Battleship tempShip = new Battleship(shipSize + 1);
                newPoint = new Point2D(point.getX() - shipSize, point.getY());
                Button newButton = this.buttonOfPoint.get(newPoint);
                this.placeIfValid(tempShip, point, newPoint, this.clonedGame, newButton, tempBool);
            }
            if (point.getX() < gameSize - shipSize) {
                tempBool = false;
                Battleship tempShip = new Battleship(shipSize + 1);
                newPoint = new Point2D(point.getX() + shipSize, point.getY());
                Button newButton = this.buttonOfPoint.get(newPoint);
                this.placeIfValid(tempShip, point, newPoint, this.clonedGame, newButton, tempBool);
            }
            if (point.getY() > shipSize - 1) {
                tempBool = false;
                newPoint = new Point2D(point.getX(), point.getY() - shipSize);
                Button newButton = this.buttonOfPoint.get(newPoint);
                Battleship tempShip = new Battleship(shipSize + 1);
                this.placeIfValid(tempShip, point, newPoint, this.clonedGame, newButton, tempBool);
            }
            if (point.getY() < gameSize - shipSize) {
                tempBool = false;
                newPoint = new Point2D(point.getX(), point.getY() + shipSize);
                Button newButton = this.buttonOfPoint.get(newPoint);
                Battleship tempShip = new Battleship(shipSize + 1);
                this.placeIfValid(tempShip, point, newPoint, this.clonedGame, newButton, tempBool);
            }
            for (Button button : this.buttonArray) {
                button.setDisable(true);
            }
            for (Button button : possibleEndpoins) {
                button.setDisable(false);
                button.setStyle("-fx-background-radius: 0; -fx-background-color: red;");
            }
        } else {
            // Hvis du velger sluttpunkt:
            Point2D startPoint = this.pointOfButton.get(lastClicked);
            Point2D endPoint = this.pointOfButton.get(currentButton);
            this.game.placeShip(new Battleship(shipSize + 1), startPoint, endPoint, this.game.getMap());
            Battleship b = new Battleship(shipSize + 1);
            this.clonedGame.placeShip(b, startPoint, endPoint, this.clonedGame.getMap());
            for (Point2D point : b.getPositions()) {
                this.disabledButtons.add(this.buttonOfPoint.get(point));
            }
            for (Button button : this.possibleEndpoins) {
                button.setStyle("-fx-background-radius: 0;");
                button.setDisable(true);
            }
            this.possibleEndpoins.clear();
            updateMap();
            currentButton.setDisable(true);
            this.shipButtons.remove(this.lastClickedShip);
            this.lastClicked = currentButton;
            for (Button button : this.shipButtons) {
                button.setDisable(false);
            }
        }
        this.lastClicked = currentButton;
        this.counter++;
        // Hvis alle skipene er plassert:
        if (this.shipButtons.isEmpty()) {
            this.game.finishSetup(this.game.getMap());
            this.game.randomizedPlacement();
            this.game.saveGame("defaultsave");
            this.startGame.setDisable(false);
        }
    }

    private void placeIfValid(Battleship tempShip, Point2D point, Point2D newPoint, Game clonedGame2, Button newButton,
            boolean tempBool) {
        try {
            this.clonedGame.placeShip(tempShip, point, newPoint, this.clonedGame.getMap());
        } catch (IllegalArgumentException exp) {
            tempBool = true;
        }
        if (tempBool == false) {
            this.possibleEndpoins.add(newButton);
            // Fjerner forrige plassering fra clonedGame:
            List<Battleship> tempList = new ArrayList<>();
            for (Point2D point2d : tempShip.getPositions()) {
                this.clonedGame.getMap().getMap()[(int) point2d.getY()][(int) point2d.getX()] = '\u0000';
            }
            for (Battleship battleship : this.clonedGame.getMap().getBattleships()) {
                if (battleship == tempShip) {
                    tempList.add(battleship);
                }
            }
            for (Battleship b : tempList) {
                this.clonedGame.getMap().getBattleships().remove(b);
            }
        }
    }

    @FXML
    private void resetBoard(ActionEvent e) {
        SetupController setupController = new SetupController(this.gameSize);
        setupController.showStage();
        this.currentStage.close();
    }

    private void updateMap() {
        for (Battleship battleship : this.game.getMap().getPlacedBattleships()) {
            for (Point2D point : battleship.getPositions()) {
                Button button = this.buttonOfPoint.get(point);
                button.setStyle("-fx-background-radius: 0; -fx-background-color: green;");
                button.setOnAction(event -> emptyFunction());
                button.setDisable(true);
            }
        }
    }

    @FXML
    private void emptyFunction() {
    }
}