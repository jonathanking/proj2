package jump61;

import java.util.ArrayList;
import java.util.Formatter;

import static jump61.Color.*;

/**
 * Represents the state of a Jump61 game. Squares are indexed either by row and
 * column (between 1 and size()), or by square number, numbering squares by
 * rows, with squares in row 1 numbered 0 - size()-1, in row 2 numbered size() -
 * 2*size() - 1, etc.
 * @author Jonathan King
 */
abstract class Board {

    /** Returns the String[][] representation of the board. */
    abstract Square[][] getBoard();

    /** The game associated with this board. */
    private Game _game;

    /** Sets the representation of the current board. */
    abstract void setBoard(Square[][] _board);

    /**
     * Returns a 2D array of squares (size N x N) all initialized as new
     * Squares.
     */
    Square[][] cleanBoard(int N) {
        Square[][] newBoard = new Square[N][N];
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                newBoard[r][c] = new Square(r, c, this);
            }
        }
        return newBoard;
    }

    /**
     * (Re)initialize me to a cleared board with N squares on a side. Clears the
     * undo history and sets the number of moves to 0.
     */
    void clear(int N) {
        unsupported("clear");
    }

    /** Copy the contents of BOARD into me. */
    void copy(Board board) {
        unsupported("copy");
    }

    /** Return the number of rows and of columns of THIS. */
    abstract int size();

    /**
     * Returns the number of spots in the square at row R, column C, 1 <= R, C
     * <= size ().
     */
    abstract int spots(int r, int c);

    /** Returns the number of spots in square #N. */
    abstract int spots(int n);

    /**
     * Returns the color of square #N, numbering squares by rows, with squares
     * in row 1 number 0 - size()-1, in row 2 numbered size() - 2*size() - 1,
     * etc.
     */
    abstract Color color(int n);

    /**
     * Returns the color of the square at row R, column C, 1 <= R, C <= size().
     */
    abstract Color color(int r, int c);

    /**
     * Returns the total number of moves made (red makes the odd moves, blue the
     * even ones).
     */
    abstract int numMoves();

    /**
     * Returns the Color of the player who would be next to move. If the game is
     * won, this will return the loser (assuming legal position).
     */
    Color whoseMove() {
        if (numMoves() % 2 != 0) {
            return BLUE;
        } else if (numMoves() % 2 == 0) {
            return RED;
        }
        return null;

    }

    /** Return true iff row R and column C denotes a valid square. */
    final boolean exists(int r, int c) {
        return 1 <= r && r <= size() && 1 <= c && c <= size();
    }

    /** Return true iff S is a valid square number. */
    final boolean exists(int s) {
        int N = size();
        return 0 <= s && s < N * N;
    }

    /** Return the row number for square #N. */
    final int row(int n) {
        return (int) Math.ceil((double) n / this.size());
    }

    /** Return the column number for square #N. */
    final int col(int n) {
        int c = n % this.size();
        if (c == 0) {
            return this.size();
        } else {
            return c;
        }
    }

    /** Return the square number of row R, column C. */
    final int sqNum(int r, int c) {
        return 1;
    }

    /**
     * Returns true iff it would currently be legal for PLAYER to add a spot to
     * square at row R, column C.
     */
    boolean isLegal(Color player, int r, int c) {
        Color square = getBoard()[r - 1][c - 1].getColor();
        if (!(this.whoseMove() == player)) {
            return false;
        }
        if (square == player || square == Color.WHITE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true iff it would currently be legal for PLAYER to add a spot to
     * square #N.
     */
    boolean isLegal(Color player, int n) {
        return isLegal(player, row(n), col(n));
    }

    /** Returns true iff PLAYER is allowed to move at this point. */
    boolean isLegal(Color player) {
        if (this.whoseMove() == player && this.getWinner() == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the winner of the current position, if the game is over, and
     * otherwise null.
     */
    final Color getWinner() {
        if (numOfColor(Color.RED) == this.size() * this.size()) {
            return Color.RED;
        } else if (numOfColor(Color.BLUE) == this.size() * this.size()) {
            return Color.BLUE;
        } else {
            return null;
        }
    }

    /** Return the number of squares of given COLOR. */
    abstract int numOfColor(Color color);

    /**
     * Add a spot from PLAYER at row R, column C. Assumes isLegal(PLAYER, R, C).
     */
    void addSpot(Color player, int r, int c) {
        unsupported("addSpot");
    }

    /** Add a spot from PLAYER at square #N. Assumes isLegal(PLAYER, N). */
    void addSpot(Color player, int n) {
        unsupported("addSpot");
    }

    /**
     * Set the square at row R, column C to NUM spots (0 <= NUM), and give it
     * color PLAYER if NUM > 0 (otherwise, white). Clear the undo history.
     */
    void set(int r, int c, int num, Color player) {
        unsupported("set");
    }

    /**
     * Set the square #N to NUM spots (0 <= NUM), and give it color PLAYER if
     * NUM > 0 (otherwise, white). Clear the undo history.
     */
    void set(int n, int num, Color player) {
        unsupported("set");
    }

    /** Set the current number of moves to N. Clear the undo history. */
    void setMoves(int n) {
        unsupported("setMoves");
    }

    /**
     * Undo the effects one move (that is, one addSpot command). One can only
     * undo back to the last point at which the undo history was cleared, or the
     * construction of this Board.
     */
    void undo() {
        unsupported("undo");
    }

    /** Returns my dumped representation. */
    @Override
    public String toString() {
        String out = "";
        out += "===\n";
        for (Square[] row : this.getBoard()) {
            out += "    ";
            for (Square s : row) {
                out += s + " ";
            }
            out = out.substring(0, out.length() - 1) + "\n";
        }
        out += "===\n";
        return out;
    }

    /**
     * Returns an external rendition of me, suitable for human-readable textual
     * display. This is distinct from the dumped representation (returned by
     * toString).
     */
    public String toDisplayString() {
        StringBuilder out = new StringBuilder();
        String numLine = "     ";
        int size = this.size();
        for (int r = 0; r < size; r++) {
            out.append(" ");
            out.append(r + 1 + "  ");
            for (Square s : this.getBoard()[r]) {
                out.append(s + " ");
            }
            out.append("\n");
            numLine += r + 1 + "  ";
        }
        out.append(numLine);

        return out.toString();
    }

    /** Returns the number of neighbors of the square at row R, column C. */
    int neighbors(int r, int c) {
        int total = 4;
        if (r == 0 || r == this.size() - 1) {
            total -= 1;
        }
        if (c == 0 || c == this.size() - 1) {
            total -= 1;
        }
        return total;
    }

    /** Returns the number of neighbors of square #N. */
    int neighbors(int n) {
        return neighbors(row(n), col(n));
    }

    /** Indicate fatal error: OP is unsupported operation. */
    private void unsupported(String op) {
        String msg = String.format("'%s' operation not supported", op);
        throw new UnsupportedOperationException(msg);
    }

    /** The length of an end of line on this system. */
    private static final int NL_LENGTH = System.getProperty("line.separator")
        .length();

}
