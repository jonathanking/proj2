package jump61;

import static jump61.Color.*;

import java.util.ArrayList;

/**
 * A Jump61 board state.
 * @author Jonathan King
 */

class MutableBoard extends Board {
    // /** The game that is using the Board. */
    // private Game _game;

    /** Holds the information of the board as a 2D Array of Squares. */
    private Square[][] _board;

    /** Returns the Square[][] representation of the board. */
    public Square[][] getBoard() {
        return _board;
    }

    /** Sets the representation of the current board. */
    public void setBoard(Square[][] _board) {
        int n = _board.length;
        Square[][] copy = new Square[n][n];
        java.lang.System.arraycopy(_board, 0, copy, 0, _board.length);
        this._board = copy;
    }

    /** An N x N board in initial configuration. */
    MutableBoard(int N) {
        setBoard(cleanBoard(N));
        _N = N;
        _moves = 0;
    }

    /** An N x N board in initial configuration with size set to DEFAULT. */
    MutableBoard() {
        setBoard(cleanBoard(Defaults.BOARD_SIZE));
        _N = Defaults.BOARD_SIZE;
        _moves = 0;
    }

    /**
     * A board whose initial contents are copied from BOARD0. Clears the undo
     * history.
     */
    MutableBoard(Board board0) {
        copy(board0);
        clearUndoHistory();
    }

    @Override
    void clear(int N) {
        setBoard(cleanBoard(N));
        _N = N;
        _moves = 0;
    }

    @Override
    void copy(Board board) {
        int N = board.size();
        Square[][] newBoard = new Square[N][N];
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                Square orig = board.getBoard()[r][c];
                Square s = new Square(orig.getSpots(), orig.getColor());
                newBoard[r][c] = s;
            }
        }
        setBoard(newBoard);
        _N = N;
        _moves = board.numMoves();
        // FIXME copy other characteristics?
    }

    @Override
    int size() {
        return _N;
    }

    @Override
    int spots(int r, int c) {
        r -= 1;
        c -= 1;
        return getBoard()[r][c].getSpots();
    }

    @Override
    int spots(int n) {
        return spots(row(n), col(n));
    }

    @Override
    Color color(int r, int c) {
        r -= 1;
        c -= 1;
        return getBoard()[r][c].getColor();
    }

    @Override
    Color color(int n) {
        return color(row(n), col(n));
    }

    @Override
    int numMoves() {
        return _moves;
    }

    @Override
    int numOfColor(Color color) {
        int total = 0;
        for (int r = 0; r < _N; r++) {
            for (int c = 0; c < _N; c++) {
                if (getBoard()[r][c].getColor() == color) {
                    total++;
                }
            }
        }
        return total;
    }

    @Override
    void addSpot(Color player, int r, int c) {
        r -= 1;
        c -= 1;
        getBoard()[r][c].addSpot(player);
        this.jump(r, c);
        updateHistory();
    }

    @Override
    void addSpot(Color player, int n) {
        addSpot(player, row(n), col(n));
    }

    @Override
    void set(int r, int c, int num, Color player) {
        r -= 1;
        c -= 1;
        if (num >= 0) {
            getBoard()[r][c].setSpots(num);
        }
        if (num > 0) {
            getBoard()[r][c].setColor(player);
        } else {
            getBoard()[r][c].setColor(WHITE);
        }
        clearUndoHistory();
    }

    @Override
    void set(int n, int num, Color player) {
        set(row(n), col(n), num, player);
    }

    @Override
    void setMoves(int num) {
        assert num > 0;
        _moves = num;
        clearUndoHistory();
    }

    /** Increase the number of moves by one. */
    void increaseMoves() {
        _moves++;
    }

    @Override
    void undo() {
        latestBoard();
        Board recent = latestBoard();
        copy(recent);
        prevBoards.add(recent);
    }

    /** Removes and returns the most recent board in the list of PREVBOARDS. */
    Board latestBoard() {
        return prevBoards.remove(prevBoards.size() - 1);
    }

    /** Adds the current board to this list of PREVBOARDS for use in undo(). */
    void updateHistory() {
        MutableBoard b = new MutableBoard(this);
        prevBoards.add(b);
    }

    /**
     * Clears the ArrayList<Board> PREVBOARDS that holds the history of the
     * session.
     */
    void clearUndoHistory() {
        prevBoards.clear();
    }

    /**
     * Do all jumping on this board, assuming that initially, S is the only
     * square that might be over-full. When adding spots, there is +1 added to
     * each addSpot() and exists() call due to my Square[][] representation of
     * the board.
     */
    private void jump(int r, int c) {
        // _game.checkForWin();
        if (this.getWinner() != null) {
            return;
        }
        Color color = getBoard()[r][c].getColor();
        int neighbors = this.neighbors(r, c);
        int curSpots = getBoard()[r][c].getSpots();
        if (curSpots > neighbors) {
            getBoard()[r][c].setSpots(curSpots - neighbors);
            if (this.exists(r + 2, c + 1)) {
                this.addSpot(color, r + 2, c + 1);
                latestBoard();
                this.jump(r + 1, c);
            }
            if (this.exists(r, c + 1)) {
                this.addSpot(color, r, c + 1);
                latestBoard();
                this.jump(r - 1, c);
            }
            if (this.exists(r + 1, c + 2)) {
                this.addSpot(color, r + 1, c + 2);
                latestBoard();
                this.jump(r, c + 1);
            }
            if (this.exists(r + 1, c)) {
                this.addSpot(color, r + 1, c);
                latestBoard();
                this.jump(r, c - 1);
            }
        }
    }

    /** Total combined number of moves by both sides. */
    protected int _moves;
    /** Convenience variable: size of board (squares along one edge). */
    private int _N;
    /**
     * Holds a list of boards for use in the undo() method. Allows user to
     * revert to a previous board state.
     */
    private ArrayList<Board> prevBoards = new ArrayList<Board>();

}
