package jump61;

import static org.junit.Assert.*;

import org.junit.Test;

public class MiscTest {

    @Test
    public void squareIndexing() {
        Board myBoard = new MutableBoard(5);
        int n = 13;
        int N = myBoard.size();
        int row = n / N;
        int col = n % N;
        assertEquals(row, 2);
        assertEquals(col, 3);
        n = 24;
        row = n / N;
        col = n % N;
        assertEquals(row, 4);
        assertEquals(col, 4);
        n = 0;
        row = n / N;
        col = n % N;
        assertEquals(row, 0);
        assertEquals(col, 0);
    }

}
