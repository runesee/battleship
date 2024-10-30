package battleshipProject;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;

public class Battleship {
    private int length;
    private char[] shipArray;
    private List<Point2D> positions = new ArrayList<>();
    private String direciton;

    public Battleship(int length) {
        this.length = length;
        this.shipArray = new char[length];
        for (int i = 0; i < length; i++) {
            this.shipArray[i] = 'o';
        }
    }

    public boolean isWithinLength(int length) {
        return length >= 2 && length <= 5;
    }

    public int getLength() {
        return this.length;
    }

    public char[] getShipArray() {
        return this.shipArray;
    }

    public Point2D getStartPos() {
        return this.positions.get(0);
    }

    public Point2D getEndPos() {
        return this.positions.get(this.positions.size() - 1);
    }

    public String getDireciton() {
        return this.direciton;
    }

    public void setDireciton(String direciton) {
        this.direciton = direciton;
    }

    public List<Point2D> getPositions() {
        return this.positions;
    }
}