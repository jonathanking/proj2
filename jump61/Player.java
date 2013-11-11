package jump61;

import static jump61.Color.*;

/**
 * Represents one player in a game of Jump61. At any given time, each Player is
 * attached to a Game and has a Color. Each call of makeMove() returns a valid
 * move.
 * @author Jonathan King
 */
abstract class Player {

    /** A Player in GAME, initially playing COLOR. */
    Player(Game game, Color color) {
        _game = game;
        _color = color;
    }

    /** Return the color I am currently playing. */
    final Color getColor() {
        return _color;
    }

    /** Set getColor() to COLOR, which must be RED or BLUE. */
    void setColor(Color color) {
        assert color == RED || color == BLUE;
        _color = color;
    }

    /** Return the Game I am currently playing in. */
    final Game getGame() {
        return _game;
    }

    /**
     * Return the Board containing the current position (read-only).
     */
    final Board getBoard() {
        return _game.getBoard();
    }

    /**
     * Ask my game to make my next move. Assumes that I am of the proper color
     * and that the game is not yet won.
     */
    abstract void makeMove();

    /** My current color. */
    private Color _color;
    /** The game I'm in. */
    private final Game _game;

    /** Make MOVE on BOARD for COLOR. */
    void makeMove(Move move, Board board, Color color) {
        board.addSpot(color, move.getR(), move.getC());

    }

    /** A Move in the game of jump61 that has a position and value. */
    class Move {
        /** The value of this move. More positive, better move. */
        private int _value = 0;

        /**
         * The [row, col] representation of the move. Stored in non-zero
         * indexing.
         */
        private int[] rc = new int[2];

        /** A move that can be made by a player on row R col C. */
        Move(int r, int c) {
            rc[0] = r + 1;
            rc[1] = c + 1;

        }

        /** A move that can be made by a player with value X. */
        Move(int x) {
            _value = x;
        }

        /** Returns the move's int VALUE. */
        int value() {
            return _value;
        }

        /** Sets the VALUE of this move to N. */
        void setValue(int n) {
            _value = n;
        }
        /** Returns the row. */
        int getR() {
            return rc[0];
        }
        /** Returns the col. */
        int getC() {
            return rc[1];
        }

        /** Makes the move. */
        void makeMove() {
            getBoard().addSpot(getColor(), rc[0], rc[1]);
        }

        @Override
        public String toString() {
            String s =
                String.format("(%d, %d) val: %d", getR(), getC(), value());
            return s;
        }

    }

}
