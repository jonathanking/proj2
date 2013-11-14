package jump61;

import static org.junit.Assert.*;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import jump61.Player.Move;
import jump61.AI.*;

import org.junit.Test;

public class AITest {

    @Test
    public void setUp() throws Exception {
        Writer output = new OutputStreamWriter(System.out);
        Game game =
            new Game(new InputStreamReader(System.in), output, output,
                new OutputStreamWriter(System.err));
        Board board = new MutableBoard(3);
        Color who = Color.BLUE;
        HumanPlayer red = new HumanPlayer(game, Color.RED);
        AI blue = new AI(game, who);
        board.addSpot(red.getColor(), 1, 1);
        Move test = blue.findBestMove(who, board, 1, 200);
        assertTrue(blue instanceof AI);
    }

    @Test
    public void staticEvalCorner() {
        Writer output = new OutputStreamWriter(System.out);
        Game game =
            new Game(new InputStreamReader(System.in), output, output,
                new OutputStreamWriter(System.err));
        Board board = new MutableBoard(3);
        Board oneSquare = new MutableBoard(3);
        oneSquare.set(1, 1, 2, Color.RED);
        AI b = new AI(game, Color.RED);
        int x = b.staticEval(Color.RED, board);
        assertTrue(b.staticEval(Color.RED, oneSquare) > b.staticEval(Color.RED,
            board));
        board.set(3, 3, 2, Color.RED);
        board.set(1, 3, 2, Color.RED);
        assertTrue(b.staticEval(Color.RED, board) > b.staticEval(Color.RED,
            oneSquare));
    }

    @Test
    public void legalMoves() {
        Writer output = new OutputStreamWriter(System.out);
        Game game =
            new Game(new InputStreamReader(System.in), output, output,
                new OutputStreamWriter(System.err));
        Board board = new MutableBoard(3);
        AI b = new AI(game, Color.RED);
        assertTrue(b.getLegalMoves(Color.BLUE, board).size() == 9);
    }

}
