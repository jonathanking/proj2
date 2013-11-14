package jump61;

import static org.junit.Assert.*;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.junit.Test;

public class GameTest {

    @Test
    public void setUp() throws Exception {
        Writer output = new OutputStreamWriter(System.out);
        Game game =
            new Game(new InputStreamReader(System.in), output, output,
                new OutputStreamWriter(System.err));

        assertEquals(game.getBoard().size(), 6);
        game.setNumMoves("6");
        assertTrue(game.getBoard().numMoves() == 6);
        game.makeMove(1, 1);
        assertTrue(game.getBoard().getBoard()[0][0].getSpots() == 1);
        assertTrue(game.getBoard().getWinner() == null);
        game.makeMove(1, 1);
        assertTrue(game.getBoard().getBoard()[0][0].getSpots() == 1);
    }

}
