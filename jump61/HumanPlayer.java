package jump61;

import static jump61.GameException.error;

/** A Player that gets its moves from manual input.
 *  @author
 */
class HumanPlayer extends Player {

    /** A new player initially playing COLOR taking manual input of
     *  moves from GAME's input source. */
    HumanPlayer(Game game, Color color) {
        super(game, color);
        // FIXME
    }

    @Override
    void makeMove() {
        // readExecuteCommand();
        Game game = getGame();
        game.getMove();
        if (game.moveHolder()[0] != 0) {
            int[] move = game.moveHolder();
            game.makeMove(move[0], move[1]);
        }
        // FIXME
    }

}
