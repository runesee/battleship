package battleshipProject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BattleshipTest {
    private Battleship one, two, three, four, five, six;

    @BeforeEach
    public void setup() {
        one = new Battleship(1);
        two = new Battleship(2);
        three = new Battleship(3);
        four = new Battleship(4);
        five = new Battleship(5);
        six = new Battleship(6);
    }

    @Test
    @DisplayName("Sjekke at lengden på skipene er riktig")
    public void testConstructor() {
        assertEquals(two.getLength(), 2, "Lengden av skipet skulle vært 2");
        assertEquals(three.getLength(), 3, "Lengden av skipet skulle vært 3");
        assertEquals(four.getLength(), 4, "Lengden av skipet skulle vært 4");
        assertEquals(five.getLength(), 5, "Lengden av skipet skulle vært 5");
    }

    @Test
    @DisplayName("Sjekke at lengden på skipene er innenfor kravene")
    public void checkLengthWithinRestrictions() {
        assertEquals(one.isWithinLength(one.getLength()), false, "Lengden av skipet skulle vært mellom 2 og 5");
        assertEquals(two.isWithinLength(two.getLength()), true, "Lengden av skipet skulle vært mellom 2 og 5");
        assertEquals(four.isWithinLength(four.getLength()), true, "Lengden av skipet skulle vært mellom 2 og 5");
        assertEquals(six.isWithinLength(six.getLength()), false, "Lengden av skipet skulle vært mellom 2 og 5");
    }
}
