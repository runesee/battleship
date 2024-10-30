package battleshipProject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javafx.geometry.Point2D;

public class BattlemapTest {
    private BattleMap mapSeven, mapTen;

    @BeforeEach
    public void setup() {
        mapSeven = new BattleMap(7);
        mapTen = new BattleMap(10);
    }

    @Test
    @DisplayName("Test av konstruktør")
    public void testConstructor() {
        assertEquals(mapSeven.getMapSize(), 7, "Størrelsen på rumapTenettet må være enmapTen 7 eller 10");
        assertEquals(mapTen.getMapSize(), 10, "Størrelsen på rumapTenettet må være enmapTen 7 eller 10");
    }

    @Test
    @DisplayName("Teste om skipene ligger innenfor rutenettet i spill med størrelse 10")
    public void testWrongIndexPlaceShipShip10() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            mapTen.placeShip(new Point2D(-1, 0),
                    mapTen.getBattleships().get(0).getEndPos(),
                    mapTen.getBattleships().get(0));

        }, "Kan ikke plassere skip utenenfor brettet");

        assertThrows(IndexOutOfBoundsException.class, () -> {
            mapTen.placeShip(new Point2D(0, mapTen.getMapSize() + 1),
                    mapTen.getBattleships().get(0).getEndPos(),
                    mapTen.getBattleships().get(0));

        }, "Kan ikke plassere skip utenfor brettet");

        assertThrows(IndexOutOfBoundsException.class, () -> {
            mapTen.placeShip(mapTen.getBattleships().get(0).getStartPos(),
                    new Point2D(0, -1),
                    mapTen.getBattleships().get(0));
        }, "Kan ikke plassere skip utenfor brettet");

        assertThrows(IndexOutOfBoundsException.class, () -> {
            mapTen.placeShip(mapTen.getBattleships().get(0).getStartPos(),
                    new Point2D(0, mapTen.getMapSize() + 1),
                    mapTen.getBattleships().get(0));
        }, "Kan ikke plassere skip utenfor brettet");
    }

    @Test
    @DisplayName("Teste om skipene ligger innenfor rutenettet i spill med størrelse 7")
    public void testWrongIndexPlaceShip7() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            mapSeven.placeShip(new Point2D(-1, 0),
                    mapSeven.getBattleships().get(0).getEndPos(),
                    mapSeven.getBattleships().get(0));
        }, "Kan ikke plassere skip utenfor brettet");

        assertThrows(IndexOutOfBoundsException.class, () -> {
            mapSeven.placeShip(new Point2D(0, mapSeven.getMapSize() + 1),
                    mapSeven.getBattleships().get(0).getEndPos(),
                    mapSeven.getBattleships().get(0));
        }, "Kan ikke plassere skip utenfor brettet");

        assertThrows(IndexOutOfBoundsException.class, () -> {
            mapSeven.placeShip(mapSeven.getBattleships().get(0).getStartPos(),
                    new Point2D(0, -1),
                    mapSeven.getBattleships().get(0));
        }, "Kan ikke plassere skip utenfor brettet");

        assertThrows(IndexOutOfBoundsException.class, () -> {
            mapSeven.placeShip(mapSeven.getBattleships().get(0).getStartPos(),
                    new Point2D(0, mapSeven.getMapSize() + 1),
                    mapSeven.getBattleships().get(0));
        }, "Kan ikke plassere skip utenfor brettet");
    }

    @Test
    @DisplayName("Teste at man ikke kan plassere skip oppå hverandre, størrelse 10")
    public void testduplicateShipPlacment10() {
        assertThrows(IllegalArgumentException.class, () -> {
            mapTen.placeShip(new Point2D(0, 0),
                    new Point2D(0, 1),
                    new Battleship(2));

            mapTen.placeShip(new Point2D(0, 0),
                    new Point2D(0, 2),
                    new Battleship(3));
        }, "Kan ikke plassere oppå hverandre");
    }

    @Test
    @DisplayName("Teste at man ikke kan plassere skip oppå hverandre, størrelse 7")
    public void testduplicateShipPlacment7() {
        assertThrows(IllegalArgumentException.class, () -> {
            mapSeven.placeShip(new Point2D(0, 0),
                    new Point2D(1, 0),
                    new Battleship(2));

            mapSeven.placeShip(new Point2D(0, 0),
                    new Point2D(0, 2),
                    new Battleship(3));
        }, "Kan ikke plassere skip oppå hverandre");
    }
}