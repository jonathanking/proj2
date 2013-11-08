package jump61;


import static jump61.Color.*;

/** A Jump61 board state.
 *  @author Jonathan King
 */

class MutableBoard extends Board {

    /** Holds the information of the board as a 2D Array of Squares. */
    private Square[][] _board;

    /** Returns the Square[][] representation of the board. */
    public Square[][] getBoard() {
        return _board;
    }

    /** Sets the representation of the current board. */
    public void setBoard(Square[][] _board) {
        this._board = _board;
    }

    /** An N x N board in initial configuration. */
    MutableBoard(int N) {
        setBoard(cleanBoard(N));
        _N = N;
        _moves = 0;
    }

    /**
     * A board whose initial contents are copied from BOARD0. Clears the undo
     * history.
     */
    MutableBoard(Board board0) {
        
        copy(board0);

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
                newBoard[r][c] = board.getBoard()[r][c];
            }
        }
        setBoard(newBoard);
        _N = N;
        _moves = board.numMoves();
      //FIXME copy other characteristics?
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
//        int row = n/_N;
//        int col = n%_N;
        return spots(row(n), col(n));
    }

    @Override
    Color color(int r, int c) {
        r -= 1;
        c -= 1;
//        if(r < 0 || c < 0) {
//            return null;
//        }
        return getBoard()[r][c].getColor();
        // FIXME
    }

    @Override
    Color color(int n) {
//        int row = n/_N;
//        int col = n%_N;
        int r = row(n);
        int c = col(n);

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
                if (getBoard()[r][c].getColor() == color){
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
        //FIXME is this where to jump?
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
        //FIXME clear undo history ??
    }

    @Override
    void set(int n, int num, Color player) {
        set(row(n), col(n), num, player);
    }

    @Override
    void setMoves(int num) {
        assert num > 0;
        _moves = num;
        //FIXME clear undo history ??
    }

    @Override
    void undo() {
        // FIXME
    }

    /**
     * Do all jumping on this board, assuming that initially, S is the only
     * square that might be over-full.
     */
    private void jump(int r, int c) {

        Color color = getBoard()[r][c].getColor();
        int neighbors = this.neighbors(r, c);
        int curSpots = getBoard()[r][c].getSpots();
        if (curSpots > neighbors) {
            getBoard()[r][c].setSpots(curSpots - neighbors);
            if (this.exists(r + 1, c)) {
                this.addSpot(color, r + 2, c + 1);
                this.jump(r + 1, c);
            }
            if (this.exists(r - 1, c)) {
                this.addSpot(color, r, c + 1);
                this.jump(r - 1, c);
            }
            if (this.exists(r, c + 1)) {
                this.addSpot(color, r + 1, c + 2);
                this.jump(r, c + 1);
            }
            if (this.exists(r, c - 1)) {
                this.addSpot(color, r + 1, c);
                this.jump(r, c - 1);
            }
        }
        // FIXME jump() on each neighbor ???
        // exists does not seem to work very well.
        // UPDATE: Fixed inputs to exists, should work
    }

    /** Total combined number of moves by both sides. */
    protected int _moves;
    /** Convenience variable: size of board (squares along one edge). */
    private int _N;
    // FIXME

}
