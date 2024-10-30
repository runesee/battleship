
package battleshipProject;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;

public class BattleMap {
    private char[][] map;
    private List<Battleship> placed = new ArrayList<>();
    private List<Battleship> battleships = new ArrayList<>();
    private int mapSize;
    private boolean isSetupComplete = false;

    public BattleMap(int size) {
        if (isCorrectSize(size)) {
            this.mapSize = size;
            this.map = new char[size][size];
        } else {
            throw new IllegalArgumentException("Map must be of size 7x7 or 10x10!");
        }
    }

    public List<Battleship> getPlacedBattleships() {
        return this.placed;
    }

    public void placeShip(Point2D start, Point2D end, Battleship battleship) {
        String direction;
        Battleship battleshipClone = new Battleship(battleship.getLength());

        // Sjekker at point-verdiene ikke er utenfor tuplene i char[][], og beregner direction:
        try {
            direction = determineDirection(start, end);
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("One or more of the given points are out of bounds!");
        }

        if (isCorrectLength(start, end, direction, battleship)) {
            // Lagrer posisjonene til skipet i skipets egen posisjons-Array:
            if (direction == "right") {
                for (int i = (int) start.getX(); i <= (int) end.getX(); i++) {
                    battleshipClone.getPositions().add(new Point2D(i, (int) start.getY()));
                }
            } else if (direction == "left") {
                ArrayList<Point2D> tempList = new ArrayList<Point2D>();
                for (int i = (int) end.getX(); i <= (int) start.getX(); i++) {
                    tempList.add(new Point2D(i, (int) start.getY()));
                }
                for (int i = tempList.size() - 1; i >= 0; i--) {
                    battleshipClone.getPositions().add(tempList.get(i));
                }
            } else if (direction == "down") {
                for (int i = (int) start.getY(); i <= (int) end.getY(); i++) {
                    battleshipClone.getPositions().add(new Point2D((int) start.getX(), i));
                }
            } else if (direction == "up") {
                ArrayList<Point2D> tempList = new ArrayList<Point2D>();
                for (int i = (int) end.getY(); i <= (int) start.getY(); i++) {
                    tempList.add(new Point2D((int) start.getX(), i));
                }
                for (int i = tempList.size() - 1; i >= 0; i--) {
                    battleshipClone.getPositions().add(tempList.get(i));
                }
            }
            if (isValidPlacement(battleshipClone)) {
                battleship.setDireciton(direction);
                for (Point2D point : battleshipClone.getPositions()) {
                    battleship.getPositions().add(point);
                }
                this.placed.add(battleship);
                for (int i = 0; i < battleship.getPositions().size(); i++) {
                    this.map[(int) battleship.getPositions().get(i).getY()][(int) battleship.getPositions().get(i)
                            .getX()] = 'o';
                }
            } else {
                battleship.getPositions().clear();
                throw new IllegalArgumentException("Invalid ship placement: placement overlaps with previously placed ships!");
            }
        } else {
            throw new IllegalArgumentException("Distance between points do not match ship length!");
        }
    }

    public char[][] getMap() {
        return this.map;
    }

    public List<Battleship> getBattleships() {
        return this.battleships;
    }

    public int getMapSize() {
        return this.mapSize;
    }

    public boolean isSetupComplete() {
        return this.isSetupComplete;
    }

    public void completeSetup() {
        this.isSetupComplete = true;
    }

    private boolean isCorrectSize(int size) {
        return (size == 7 || size == 10);
    }

    private boolean isCorrectLength(Point2D start, Point2D end, String direction, Battleship battleship) {
        if (direction.equals("right") || direction.equals("left")) {
            if (Math.abs((int) start.getX() - end.getX()) != battleship.getLength() - 1) {
                return false;
            }
        } else if (direction.equals("down") || direction.equals("up")) {
            if (Math.abs((int) start.getY() - end.getY()) != battleship.getLength() - 1) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean isValidPlacement(Battleship battleship) {
        // Sjekker om noen av posisjonene i Positions allerede er opptatt:
        for (int i = 0; i < battleship.getPositions().size(); i++) {
            if (this.getMap()[(int) battleship.getPositions().get(i).getY()][(int) battleship.getPositions().get(i)
                    .getX()] != '\u0000' /* Unicode for tom char */) {
                return false;
            }
        }
        return true;
    }

    private String determineDirection(Point2D start, Point2D end) {
        // Returnerer "right", "left", "up" eller "down" hvis to forskjellige, gyldige punkter velges
        // Hvis retningen er diagonal eller begge punktene er like, returneres en tom streng
        if (start.equals(end)) {
            return "";
        }
        // Hvis det er endring i både y- og x-retningen samtidig (diagonal):
        else if ((Math.abs((int) start.getX() - end.getX()) != 0) && (Math.abs((int) start.getY() - end.getY()) != 0)) {
            return "";
        }
        if (Math.abs((int) start.getX() - end.getX()) != 0) {
            // Bevegelse horisontalt (i x-retning)
            if (end.getX() > start.getX()) {
                // Bevegelse fra venstre til høyre
                return "right";
            } else if (end.getX() < start.getX()) {
                // Bevegelse fra høyre til venstre
                return "left";
            }
        } else if (Math.abs((int) start.getY() - end.getY()) != 0) {
            // Bevegelse vertikalt (i y-retning)
            if (end.getY() > start.getY()) {
                // Bevegelse nedover
                return "down";
            } else if (end.getY() < start.getY()) {
                // Bevegelse oppover
                return "up";
            }
        }
        return "";
    }
}