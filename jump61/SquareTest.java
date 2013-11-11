package jump61;

import static org.junit.Assert.*;

import org.junit.Test;

public class SquareTest {

    @Test
    public void createSquares() {
        Board x = new MutableBoard(6);
        Square a = new Square(0, 0, x);
        Square b = new Square(0, 1 ,x);
        Square c = new Square(3, Color.RED, 1, 0, x);
        assertTrue(a.getColor() == Color.WHITE);
        assertTrue(b.getSpots() == 0);
        b.setColor(Color.BLUE);
        b.setSpots(3);
        assertTrue(b.getColor() == Color.BLUE && b.getSpots() == 3);
    }
    
    @Test
    public void createSquares2() {
        Board x = new MutableBoard(6);
        Square a = new Square(0, 0, x);
        Square b = new Square(0, 1 ,x);
        Square c = new Square(3, Color.RED, 1, 0, x);
        assertTrue(a.getColor() == Color.WHITE);
        assertTrue(b.getSpots() == 0);
        b.setColor(Color.BLUE);
        b.setSpots(3);
        assertTrue(b.getColor() == Color.BLUE && b.getSpots() == 3);
    }

}
