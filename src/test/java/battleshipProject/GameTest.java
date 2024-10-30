package battleshipProject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javafx.geometry.Point2D;

public class GameTest {
    private Game seven, ten;

    @BeforeEach
    public void setup() {
        seven = new Game(7);
        ten = new Game(10);
    }

    @Test
    @DisplayName("Teste konstruktøren med heltall")
    public void testConstructorInt() {
        assertEquals(seven.getSize(), 7);
        assertEquals(seven.getMap().getMapSize(), 7);
        assertEquals(ten.getSize(), 10);
        assertEquals(ten.getMap().getMapSize(), 10);
        assertEquals(seven.getMap().getBattleships().size(), 4, ("I et spill med størrelse 7 skal det være 4 skip"));
        assertEquals(ten.getMap().getBattleships().size(), 5, ("I et spill med størrelse 10 skal det være 5 skip"));
        assertEquals(seven.getEnemyMap().getBattleships().size(), 4,
                ("I et spill med størrelse 7 skal det være 4 skip"));
        assertEquals(ten.getEnemyMap().getBattleships().size(), 5,
                ("I et spill med størrelse 10 skal det være 5 skip"));
    }

    @Test
    @DisplayName("Teste fullføringen av oppsettet")
    public void testFinishSetup() {
        seven.placeShip(seven.getMap().getBattleships().get(0),
                new Point2D(0, 0),
                new Point2D(1, 0),
                seven.getMap());
        seven.placeShip(seven.getMap().getBattleships().get(1),
                new Point2D(0, 1),
                new Point2D(2, 1),
                seven.getMap());
        seven.placeShip(seven.getMap().getBattleships().get(2),
                new Point2D(0, 2),
                new Point2D(2, 2),
                seven.getMap());

        assertThrows(IllegalStateException.class, () -> {
            seven.finishSetup(seven.getMap());
        }, "Alle skip må plasseres før setup kan fullføres!");

        seven.placeShip(seven.getMap().getBattleships().get(3),
                new Point2D(0, 3),
                new Point2D(3, 3),
                seven.getMap());

        seven.finishSetup(seven.getMap());
        assertEquals(true, seven.getMap().isSetupComplete());

        for (int i = 0; i < seven.getSize(); i++) {
            for (int j = 0; j < seven.getSize(); j++) {
                assertEquals(' ', seven.getMap().getMap()[i][j]);
            }
        }
    }

    @Test
    @DisplayName("Teste funksjonen av shoot")
    public void testShoot() {
        seven.placeShip(seven.getMap().getBattleships().get(0),
                new Point2D(0, 0),
                new Point2D(1, 0),
                seven.getMap());
        seven.placeShip(seven.getMap().getBattleships().get(1),
                new Point2D(0, 1),
                new Point2D(2, 1),
                seven.getMap());
        seven.placeShip(seven.getMap().getBattleships().get(2),
                new Point2D(0, 2),
                new Point2D(2, 2),
                seven.getMap());
        seven.placeShip(seven.getMap().getBattleships().get(3),
                new Point2D(0, 3),
                new Point2D(3, 3),
                seven.getMap());
        seven.randomizedPlacement();
        seven.finishSetup(seven.getMap());
        seven.finishSetup(seven.getEnemyMap());
        seven.shoot(new Point2D(0, 0), seven.getMap());
        assertThrows(IllegalArgumentException.class, () -> {
            seven.shoot(new Point2D(0, 0), seven.getMap());
        },"Kan ikke skyte samme punkt to ganger!" );
        assertEquals('x', seven.getMap().getMap()[0][0]);
    }

    @Test
    @DisplayName("Teste å restarte oppsettet")
    public void testRestartSetup() {
        seven.placeShip(seven.getMap().getBattleships().get(0),
                new Point2D(0, 0),
                new Point2D(1, 0),
                seven.getMap());
        seven.placeShip(seven.getMap().getBattleships().get(1),
                new Point2D(0, 1),
                new Point2D(2, 1),
                seven.getMap());

        ten.placeShip(ten.getMap().getBattleships().get(0),
                new Point2D(0, 0),
                new Point2D(1, 0),
                ten.getMap());
        ten.placeShip(ten.getMap().getBattleships().get(1),
                new Point2D(0, 1),
                new Point2D(2, 1),
                ten.getMap());
        
        seven.restartSetup(seven.getMap());
        ten.restartSetup(ten.getMap());
        
        for (int i = 0; i < seven.getSize(); i++) {
            for (int j = 0; j < seven.getSize(); j++) {
                assertEquals(' ', seven.getMap().getMap()[i][j]);
            }
        }
        for (int i = 0; i < ten.getSize(); i++) {
            for (int j = 0; j < ten.getSize(); j++) {
                assertEquals(' ', ten.getMap().getMap()[i][j]);
            }
        }
        assertEquals(true, seven.getMap().getPlacedBattleships().isEmpty());
        assertEquals(true, ten.getMap().getPlacedBattleships().isEmpty());

        seven.randomizedPlacement();
        ten.randomizedPlacement();
        seven.restartSetup(seven.getEnemyMap());
        ten.restartSetup(ten.getEnemyMap());

        for (int i = 0; i < seven.getSize(); i++) {
            for (int j = 0; j < seven.getSize(); j++) {
                assertEquals(' ', seven.getEnemyMap().getMap()[i][j]);
            }
        }
        for (int i = 0; i < ten.getSize(); i++) {
            for (int j = 0; j < ten.getSize(); j++) {
                assertEquals(' ', ten.getEnemyMap().getMap()[i][j]);
            }
        }
        assertEquals(true, seven.getEnemyMap().getPlacedBattleships().isEmpty());
        assertEquals(true, ten.getEnemyMap().getPlacedBattleships().isEmpty());
    }

    @Test
    @DisplayName("Teste funskjonen for å finne indexen av punkter")
    public void testFindPointIndex() {
        seven.placeShip(seven.getMap().getBattleships().get(1),
                new Point2D(0, 0),
                new Point2D(2, 0),
                seven.getMap());
        assertThrows(IllegalArgumentException.class, () -> {
            seven.findPointIndex(new Point2D(2, 0), seven.getMap().getBattleships().get(0));
        }, "Må velge et punkt som eksisterer i shipArray!");

        assertEquals(0, seven.findPointIndex(new Point2D(0, 0), seven.getMap().getBattleships().get(1)));
        assertEquals(1, seven.findPointIndex(new Point2D(1, 0), seven.getMap().getBattleships().get(1)));
        assertEquals(2, seven.findPointIndex(new Point2D(2, 0), seven.getMap().getBattleships().get(1)));
    }

    @Test
    @DisplayName("Teste tilhørende battleship")
    public void testLocateAssociatedBattleship() {
        Battleship b = new Battleship(2);
        ten.placeShip(b, new Point2D(0, 0),
                new Point2D(0, 1),
                ten.getMap());
        assertEquals(b, ten.locateAssociatedBattleship(new Point2D(0, 0), ten.getMap()));
        assertEquals(b, ten.locateAssociatedBattleship(new Point2D(0, 1), ten.getMap()));
    }

    @Test
    @DisplayName("Teste funksjonen for å bestemme om spillet er over")
    public void testIsGameOver() {
        seven.placeShip(seven.getMap().getBattleships().get(0),
                new Point2D(0, 0),
                new Point2D(1, 0),
                seven.getMap());
        seven.placeShip(seven.getMap().getBattleships().get(1),
                new Point2D(0, 1),
                new Point2D(2, 1),
                seven.getMap());
        seven.placeShip(seven.getMap().getBattleships().get(2),
                new Point2D(0, 2),
                new Point2D(2, 2),
                seven.getMap());
        seven.placeShip(seven.getMap().getBattleships().get(3),
                new Point2D(0, 3),
                new Point2D(3, 3),
                seven.getMap());
        seven.randomizedPlacement();
        seven.finishSetup(seven.getMap());
        seven.finishSetup(seven.getEnemyMap());

        assertEquals(false, seven.isGameOver(seven.getMap()));
        for (int i = 0; i < seven.getSize(); i++) {
            for (int j = 0; j < seven.getSize(); j++) {
                Battleship battleship = seven.locateAssociatedBattleship(new Point2D(j, i), seven.getMap());
                if (battleship != null) {
                    if (battleship.getPositions().contains(new Point2D(j, i))) {
                        seven.shoot(new Point2D(j, i), seven.getMap());
                    }
                } 
            }
        }
        assertEquals(true, seven.isGameOver(seven.getMap()));
    }

    @Test
    @DisplayName("Teste om spill som åpnes fra ei fil er identisk til spillet som fila ble lagret fra")
    public void testNewGameEqualsSavedGame() {
        seven.placeShip(seven.getMap().getBattleships().get(0),
                new Point2D(0, 0),
                new Point2D(1, 0),
                seven.getMap());
        seven.placeShip(seven.getMap().getBattleships().get(1),
                new Point2D(0, 1),
                new Point2D(2, 1),
                seven.getMap());
        seven.placeShip(seven.getMap().getBattleships().get(2),
                new Point2D(0, 2),
                new Point2D(2, 2),
                seven.getMap());
        seven.placeShip(seven.getMap().getBattleships().get(3),
                new Point2D(0, 3),
                new Point2D(3, 3),
                seven.getMap());
        seven.randomizedPlacement();
        seven.finishSetup(seven.getMap());
        seven.finishSetup(seven.getEnemyMap());
        // Skyter noen ganger:
        seven.randomizedShoot(seven.getEnemyMap());
        seven.randomizedShoot(seven.getMap());
        seven.randomizedShoot(seven.getEnemyMap());
        seven.randomizedShoot(seven.getMap());
        seven.randomizedShoot(seven.getEnemyMap());
        seven.randomizedShoot(seven.getMap());

        seven.saveGame("GameTestSaveFile.txt");
        String path = new File("").getAbsolutePath();
        Game newGame = new Game(new File(path + "/src/main/java/battleshipProject/saves/GameTestSaveFile.txt"));
        
        // Sjekker om brettene er de samme:
        for (int i = 0; i < seven.getSize(); i++) {
            for (int j = 0; j < seven.getSize(); j++) {
                assertEquals(seven.getMap().getMap()[i][j], newGame.getMap().getMap()[i][j]);
                assertEquals(seven.getEnemyMap().getMap()[i][j], newGame.getEnemyMap().getMap()[i][j]);
            }
        }
        // Sjekker at alle battleships er like:
        for (int i = 0; i < seven.getMap().getPlacedBattleships().size(); i++) {
            for (int j = 0; j < seven.getMap().getPlacedBattleships().get(i).getPositions().size(); j++) {
                
                assertEquals(true, seven.getMap().getPlacedBattleships().get(i).getPositions().get(j)
                .equals(newGame.getMap().getPlacedBattleships().get(i).getPositions().get(j)));
                
                assertEquals(true, seven.getEnemyMap().getPlacedBattleships().get(i).getPositions().get(j)
                .equals(newGame.getEnemyMap().getPlacedBattleships().get(i).getPositions().get(j)));
            }
        }

        newGame.saveGame("GameTestCompareSaveFile.txt");
        File firstFile = new File(path + "/src/main/java/battleshipProject/saves/GameTestSaveFile.txt");
        File secondFile = new File(path + "/src/main/java/battleshipProject/saves/GameTestCompareSaveFile.txt");
        List<String> firstArray = new ArrayList<>();
        List<String> secondArray = new ArrayList<>();
        Scanner scanner;
        try {
            scanner = new Scanner(new FileReader(firstFile));
            while (scanner.hasNext()) {
                firstArray.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + firstFile.getName() + " could not be opened!");
        }
        try {
            scanner = new Scanner(new FileReader(secondFile));
            while (scanner.hasNext()) {
                secondArray.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + secondFile.getName() + " could not be opened!");
        }

        // Sjekker om filene har identisk innhold:
        for (int i = 0; i < firstArray.size(); i++) {
            assertEquals(firstArray.get(i), secondArray.get(i));
        }
        firstFile.delete();
        secondFile.delete();
    }
}