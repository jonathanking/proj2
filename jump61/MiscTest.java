package jump61;

import static org.junit.Assert.*;

import org.junit.Test;

public class MiscTest {

    @Test
    public void SquareIndexing() {
        Board myBoard = new MutableBoard(5);
        int n = 13;
        int _N = myBoard.size();
        int row = n / _N;
        int col = n % _N;
        assertEquals(row, 2);
        assertEquals(col, 3);
        n = 24;
        row = n / _N;
        col = n % _N;
        assertEquals(row, 4);
        assertEquals(col, 4);
        n = 0;
        row = n / _N;
        col = n % _N;
        assertEquals(row, 0);
        assertEquals(col, 0);
    }

}
