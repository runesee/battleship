package battleshipProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.geometry.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileReader;
import java.util.Scanner;

public class Game {
    private BattleMap map;
    private int size;
    private BattleMap enemyMap;
    private int counter;
    private Point2D prevRandomPoint;
    private List<Point2D> prevRandomAdjacentPoints = new ArrayList<>();

    public Game(int size) {
        this.size = size;
        this.map = new BattleMap(size);
        this.enemyMap = new BattleMap(size);
        this.counter = 0;

        // Legger til en rekke battleships for størrelse 7 og 10:
        this.map.getBattleships().add(new Battleship(2));
        this.enemyMap.getBattleships().add(new Battleship(2));
        this.map.getBattleships().add(new Battleship(3));
        this.enemyMap.getBattleships().add(new Battleship(3));
        this.map.getBattleships().add(new Battleship(3));
        this.enemyMap.getBattleships().add(new Battleship(3));
        this.map.getBattleships().add(new Battleship(4));
        this.enemyMap.getBattleships().add(new Battleship(4));
        // Legger til et femte skip for størrelse 10:
        if (size == 10) {
            this.map.getBattleships().add(new Battleship(5));
            this.enemyMap.getBattleships().add(new Battleship(5));
        }
    }

    public Game(File saveFile) {
        List<String> outputArray = new ArrayList<>();
        Scanner scanner;

        try {
            scanner = new Scanner(new FileReader(saveFile));
            while (scanner.hasNext()) {
                outputArray.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + saveFile.getName() + " could not be opened!");
            System.exit(1);
        }
        try {
            this.map = new BattleMap(Integer.parseInt(outputArray.get(0)));
            this.enemyMap = new BattleMap(Integer.parseInt(outputArray.get(0)));
            saveFileHelperFunction(this.map, outputArray);
            saveFileHelperFunction(this.enemyMap, outputArray);
            this.counter = Integer.parseInt(outputArray.get(outputArray.size() - 1));
            this.map.completeSetup();
            this.enemyMap.completeSetup();
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Given file is not of supported format!");
        }
    }

    public void randomizedPlacement() {
        for (Battleship battleship : this.enemyMap.getBattleships()) {
            while (this.enemyMap.isSetupComplete() == false) {
                int timeToLive = 10;
                Random rand = new Random();
                int randInt = rand.nextInt(4) + 1;
                Point2D firstPos = null;
                Point2D secondPos = null;

                // Prøver å plassere skip opptil 10 ganger, kjører hele metoden på nytt hvis det ikke går
                while (timeToLive > 0) {
                    try {
                        timeToLive--;
                        firstPos = placementHelperFunction(battleship, randInt);
                        if (randInt == 1) {
                            secondPos = new Point2D(((int) firstPos.getX()) + battleship.getLength() - 1,
                                    (int) firstPos.getY());
                        } else if (randInt == 2) {
                            secondPos = new Point2D(((int) firstPos.getX()) - battleship.getLength() + 1,
                                    (int) firstPos.getY());
                        } else if (randInt == 3) {
                            secondPos = new Point2D(((int) firstPos.getX()),
                                    (int) firstPos.getY() + battleship.getLength() - 1);
                        } else {
                            secondPos = new Point2D(((int) firstPos.getX()),
                                    (int) firstPos.getY() - battleship.getLength() + 1);
                        }
                        this.placeShip(battleship, firstPos, secondPos, this.enemyMap);
                        break;
                    } catch (IllegalArgumentException e) {}
                }
                if (timeToLive == 0) {
                    // Plassering tar for mange forsøk, starter metoden på nytt
                    this.restartSetup(this.enemyMap);
                    this.randomizedPlacement();
                }
                break;
            }
        }
        finishSetup(this.enemyMap);
    }

    private Point2D placementHelperFunction(Battleship battleship, int directionDecider) {
        Random random = new Random();
        int randX = 0;
        int randY = 0;

        if (directionDecider == 1) {
            // Bevegelse til høyre
            // Må være minst Battleship.length() ifra høyre kant
            // Tilfeldig int mellom 0 til ytre høyre grense:
            randX = random.nextInt((this.enemyMap.getMapSize() - battleship.getLength() + 1));
            // Tilfeldig int for y-koordinat mellom 0 og mapSize:
            randY = random.nextInt(this.enemyMap.getMapSize());
        } else if (directionDecider == 2) {
            // Bevegelse til venstre
            randX = random.nextInt(this.enemyMap.getMapSize() - battleship.getLength() + 1) + battleship.getLength()
                    - 1;
            randY = random.nextInt(this.enemyMap.getMapSize());
        } else if (directionDecider == 3) {
            // Bevegelse nedover
            randY = random.nextInt((this.enemyMap.getMapSize() - battleship.getLength() + 1));
            randX = random.nextInt(this.enemyMap.getMapSize());
        } else {
            // Bevegelse oppover
            randY = random.nextInt(this.enemyMap.getMapSize() - battleship.getLength() + 1) + battleship.getLength()
                    - 1;
            randX = random.nextInt(this.enemyMap.getMapSize());
        }
        return new Point2D(randX, randY);
    }

    private void saveFileHelperFunction(BattleMap map, List<String> output) {
        int positionsSize = Integer.parseInt(output.get(this.map.getMapSize() + 1));
        if (map == this.map) {
            for (int i = 0; i < map.getMapSize(); i++) {
                for (int j = 0; j < map.getMapSize(); j++) {
                    map.getMap()[i][j] = output.get(i + 1).charAt(j);
                }
            }
        } else {
            if (map.getMapSize() == 7) {
                for (int i = 0; i < map.getMapSize(); i++) {
                    for (int j = 0; j < map.getMapSize(); j++) {
                        map.getMap()[i][j] = output.get(i + 14).charAt(j);
                    }
                }
            } else {
                for (int i = 0; i < map.getMapSize(); i++) {
                    for (int j = 0; j < map.getMapSize(); j++) {
                        map.getMap()[i][j] = output.get(i + 18).charAt(j);
                    }
                }
            }
        }
        for (int i = 0; i < positionsSize; i++) {
            String[] strArray = null;
            if (map == this.map) {
                strArray = output.get(map.getMapSize() + 2 + i).split(",");
            } else {
                if (map.getMapSize() == 7) {
                    strArray = output.get(22 + i).split(",");
                } else {
                    strArray = output.get(29 + i).split(",");
                }
            }

            Battleship battleship;
            int length = 0;
            ArrayList<Point2D> tempPositions = new ArrayList<>();
            for (String string : strArray) {
                length += 1;
                int temp1 = Integer.parseInt(Character.toString(string.charAt(2)));
                int temp2 = Integer.parseInt(Character.toString(string.charAt(4)));
                tempPositions.add(new Point2D(temp1, temp2));
            }
            battleship = new Battleship(length);
            for (Point2D point : tempPositions) {
                battleship.getPositions().add(point);
            }
            int counter = 0;
            for (Point2D point : battleship.getPositions()) {
                if (map.getMap()[(int) point.getY()][(int) point.getX()] == 'x') {
                    battleship.getShipArray()[counter] = map.getMap()[(int) point.getY()][(int) point.getX()];
                }
                counter += 1;
            }
            map.getBattleships().add(battleship);
            map.getPlacedBattleships().add(battleship);
        }
    }

    public void placeShip(Battleship battleship, Point2D start, Point2D end, BattleMap map) {
        if (!map.isSetupComplete()) {
            if (!map.getPlacedBattleships().contains(battleship)) {
                try {
                    map.placeShip(start, end, battleship);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Ulovlig plassering av skip!");
                }
            }
        }
    }

    public void shoot(Point2D point, BattleMap map) {
        if (!map.isSetupComplete()) {
            throw new IllegalStateException("Setup must be completed before game can begin!");
        }
        if (isGameOver(this.map) || isGameOver(this.enemyMap)) {
            throw new IllegalStateException("Game is finished, start a new game to continue playing!");
        }
        if (isPointAlreadyShot(point, map)) {
            throw new IllegalArgumentException("Point has already been shot!");
        }
        Battleship tempShip = locateAssociatedBattleship(point, map);
        if (tempShip != null) {
            // Har truffet et skip
            map.getMap()[(int) point.getY()][(int) point.getX()] = 'x';
            int index = 0;
            index = findPointIndex(point, tempShip);
            tempShip.getShipArray()[index] = 'x';
        } else {
            map.getMap()[(int) point.getY()][(int) point.getX()] = 'x';
        }
        this.counter += 1;
    }

    public void randomizedShoot(BattleMap map) {
        if (!this.isGameOver(map)) {
            if (!(this.prevRandomAdjacentPoints.isEmpty())) {
                Battleship battleship = locateAssociatedBattleship(this.prevRandomPoint, map);
                if (battleship != null) {
                    // Forrige skudd fra lista traff et skip - slutter å traversere den gamle lista
                    // Vil nå opprette en ny liste for det nye punktet
                    // Gjør dette ved å tømme forrige array, samt å kjøre koden utenfor denne bolken
                    this.prevRandomAdjacentPoints.clear();
                } else {
                    // Forrige forsøk traff ikke et skip, fortsetter å prøve de andre punktene:
                    Point2D point = this.prevRandomAdjacentPoints.get(0);
                    shoot(point, map);
                    this.prevRandomPoint = point;
                    this.prevRandomAdjacentPoints.remove(point);
                    return;
                }
            }
            if (this.prevRandomPoint != null) {
                Battleship battleship = locateAssociatedBattleship(this.prevRandomPoint, map);
                if (battleship != null) {
                    // -> Forrige skudd traff et skip
                    // I stedet for å generere et tilfeldig punkt, forsøkes det å skyte de
                    // nærliggende punktene
                    try {
                        // Legger til de nærliggende punktene til forrige treff i en array hvis de er
                        // skytbare:
                        addAdjacentPoints(this.prevRandomAdjacentPoints, this.prevRandomPoint, map);

                        if (!(this.prevRandomAdjacentPoints.isEmpty())) {
                            // Finnes minst ett nærliggende punkt som kan være del av skipet
                            Point2D point = this.prevRandomAdjacentPoints.get(0);
                            shoot(point, map);
                            this.prevRandomPoint = point;
                            this.prevRandomAdjacentPoints.remove(point);
                            return;
                        } else {
                            // Hvis det ikke finnes noen nærliggende punkt, velges et tilfeldig punkt i stedet
                            // (som altså gjøres i koden nedenfor)
                        }
                    } catch (Exception e) {}
                }
            }
            // Velger tilfeldig Point2D siden forrige skudd ikke finnes:
            List<Point2D> unusedPoints = new ArrayList<>();
            List<Point2D> pointsToRemove = new ArrayList<>();
            for (int i = 0; i < map.getMapSize(); i++) {
                for (int j = 0; j < map.getMapSize(); j++) {
                    unusedPoints.add(new Point2D(j, i));
                }
            }
            for (int i = 0; i < map.getMapSize(); i++) {
                for (int j = 0; j < map.getMapSize(); j++) {
                    if (map.getMap()[i][j] == 'x') {
                        Point2D newPoint = new Point2D(j, i);
                        pointsToRemove.add(newPoint);
                    }
                }
            }
            for (Point2D point : pointsToRemove) {
                unusedPoints.remove(point);
            }

            Random random = new Random();
            int randint = random.nextInt(unusedPoints.size());
            Point2D randPoint = unusedPoints.get(randint);
            this.prevRandomPoint = randPoint;
            shoot(randPoint, map);
        }
    }

    private void addAdjacentPoints(List<Point2D> pointList, Point2D point, BattleMap map) {
        Point2D newPoint = null;
        if (point.getX() > 0) {
            newPoint = new Point2D(point.getX() - 1, point.getY());
            if (!(isPointAlreadyShot(newPoint, map))) {
                pointList.add(newPoint);
            }
        }
        if (point.getX() < map.getMapSize() - 1) {
            newPoint = new Point2D(point.getX() + 1, point.getY());
            if (!(isPointAlreadyShot(newPoint, map))) {
                pointList.add(newPoint);
            }
        }
        if (point.getY() > 0) {
            newPoint = new Point2D(point.getX(), point.getY() - 1);
            if (!(isPointAlreadyShot(newPoint, map))) {
                pointList.add(newPoint);
            }
        }
        if (point.getY() < map.getMapSize() - 1) {
            newPoint = new Point2D(point.getX(), point.getY() + 1);
            if (!(isPointAlreadyShot(newPoint, map))) {
                pointList.add(newPoint);
            }
        }
    }

    public void finishSetup(BattleMap map) {
        // Setter hele kartet til å være "tomt" før spillet settes i gang
        if ((map.getPlacedBattleships().size() == map.getBattleships().size())) {
            clearBoard(map);
            map.completeSetup();
        } else {
            throw new IllegalStateException("All ships must be placed before setup can be completed!");
        }
    }

    private void clearBoard(BattleMap map) {
        // Hjelpemetode for å fjerne alle tegn fra spillebrettet
        for (int i = 0; i < map.getMapSize(); i++) {
            for (int j = 0; j < map.getMapSize(); j++) {
                map.getMap()[i][j] = ' ';
            }
        }
    }

    public void restartSetup(BattleMap map) {
        // Fjerner alt fra brettet, inkludert de plasserte skipene
        clearBoard(map);
        map.getPlacedBattleships().clear();
    }

    public int findPointIndex(Point2D point, Battleship battleship) {
        for (Point2D comparePoint : battleship.getPositions()) {
            if (comparePoint.equals(point)) {
                return battleship.getPositions().indexOf(comparePoint);
            }
        }
        throw new IllegalArgumentException("Must choose a point that exists within battleship to locate index!");
    }

    public Battleship locateAssociatedBattleship(Point2D point, BattleMap map) {
        for (Battleship battleship : map.getPlacedBattleships()) {
            for (Point2D comparePoint : battleship.getPositions()) {
                if (comparePoint.equals(point)) {
                    return battleship;
                }
            }
        }
        return null;
    }

    public boolean isGameOver(BattleMap map) {
        // Sjekker om alle battleships har blitt sunket:
        for (Battleship battleship : map.getBattleships()) {
            for (Point2D point : battleship.getPositions()) {
                if (map.getMap()[(int) point.getY()][(int) point.getX()] != 'x') {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isPointAlreadyShot(Point2D point, BattleMap map) {
        if (map.getMap()[(int) point.getY()][(int) point.getX()] == 'x') {
            return true;
        }
        return false;
    }

    public BattleMap getMap() {
        return this.map;
    }

    public int getSize() {
        return this.size;
    }

    public BattleMap getEnemyMap() {
        return this.enemyMap;
    }

    private void writeMap(PrintWriter file, BattleMap map) {
        StringBuilder s1;
        StringBuilder s2;

        // Skriver først ut kartets størrelse:
        file.println(map.getMapSize());

        // Legger så til kartets tilstand:
        for (int i = 0; i < map.getMapSize(); i++) {
            s1 = new StringBuilder();
            for (int j = 0; j < map.getMapSize(); j++) {
                s1.append(map.getMap()[i][j]);
            }
            file.println(s1);
        }

        // Legger til en oversikt over skipenes posisjoner:
        file.println(map.getPlacedBattleships().size());
        for (Battleship battleship : map.getPlacedBattleships()) {
            s2 = new StringBuilder();
            s2.append("[");
            for (Point2D point : battleship.getPositions()) {
                s2.append("[" + (int) point.getX() + "|" + (int) point.getY() + "], ");
            }
            s2.deleteCharAt(s2.length() - 1);
            s2.deleteCharAt(s2.length() - 1);
            s2.append("]");
            file.println(s2);
        }
    }

    public void saveGame(String fileName) {
        if (this.map.isSetupComplete() && this.enemyMap.isSetupComplete()) {
            try {
                if (!(fileName.endsWith(".txt"))) {
                    fileName += ".txt";
                }
                String path = new File("").getAbsolutePath();
                PrintWriter file = new PrintWriter(path + "/src/main/java/battleshipProject/saves/" + fileName);
                writeMap(file, this.map);
                writeMap(file, this.enemyMap);
                // Legger til sist til counter på bunnen av fila:
                file.println(this.counter);
                file.close();
            } catch (FileNotFoundException e) {
                System.out.println("File " + fileName + " could not be opened!");
                System.exit(1);
            }
        } else {
            throw new IllegalStateException("Game can only be saved once setup has been completed!");
        }
    }

    public int getCounter() {
        return this.counter;
    }
}