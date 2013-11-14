package jump61;

import ucb.junit.textui;

/**
 * The suite of all JUnit tests for the Jump61 game.
 * @author Jonathan King
 */
public class UnitTest {

    /** Run the JUnit tests in the jump61 package. */
    public static void main(String[] ignored) {
        textui.runClasses(jump61.BoardTest.class, jump61.SquareTest.class,
            jump61.GameTest.class, jump61.MiscTest.class,
            jump61.AITest.class, jump61.MutableBoardTest.class);

    }

}
