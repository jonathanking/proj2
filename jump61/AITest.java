package jump61;

import static org.junit.Assert.*;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import jump61.Player.Move;

import org.junit.Test;

public class AITest {

    @Test
    public void setUp() throws Exception {
        Writer output = new OutputStreamWriter(System.out);
        Game game = new Game(new InputStreamReader(System.in),
                             output, output,
                             new OutputStreamWriter(System.err));
        Board board = new MutableBoard(3);
        Color who = Color.BLUE;
        HumanPlayer red = new HumanPlayer(game, Color.RED);
        AI blue = new AI(game, who);
        board.addSpot(red.getColor(), 1, 1);
        Move test = blue.findBestMove(who, board, 1, 200);
        assertTrue(blue instanceof AI);
    }

}
