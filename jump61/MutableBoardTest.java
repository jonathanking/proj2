package jump61;

import static org.junit.Assert.*;

import org.junit.Test;

public class MutableBoardTest {

    @Test
    public void colorTest() {
        Board B = new MutableBoard(5);
        assertEquals(B.numOfColor(Color.WHITE), 25);
        B.set(1, 1, 2, Color.RED);
        assertTrue(B.color(1, 1) == Color.RED);
    }


}
