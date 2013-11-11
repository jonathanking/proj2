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

        assertEquals(game.play(), 0);
    }

}
