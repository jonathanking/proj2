package jump61;

import static org.junit.Assert.*;

import org.junit.Test;

public class SquareTest {

    @Test
    public void createSquares() {
        Square a = new Square();
        Square b = new Square();
        Square c = new Square(3, Color.RED);
        assertTrue(a.getColor() == Color.WHITE);
        assertTrue(b.getSpots() == 0);
        b.setColor(Color.BLUE);
        b.setSpots(3);
        assertTrue(b.getColor() == Color.BLUE && b.getSpots() == 3);
    }
    
    @Test
    public void createSquares2() {
        Square a = new Square();
        Square b = new Square();
        Square c = new Square(3, Color.RED);
        assertTrue(a.getColor() == Color.WHITE);
        assertTrue(b.getSpots() == 0);
        b.setColor(Color.BLUE);
        b.setSpots(3);
        assertTrue(b.getColor() == Color.BLUE && b.getSpots() == 3);
    }

}
