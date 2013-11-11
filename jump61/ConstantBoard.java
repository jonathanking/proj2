package jump61;

/**
 * A ConstantBoard is a view of an existing Board that does not allow
 * modifications. Changes made to the underlying Board are reflected in
 * ConstantBoards formed from it.
 * @author P. N. Hilfinger
 */
class ConstantBoard extends Board {

    /** Holds the information of the board as a 2D Array of Squares. */
    private Square[][] _board;

    /** A new ConstantBoard that allows a read-only view of BOARD. */
    ConstantBoard(Board board) {
        _boardOrigin = board;
        _board = board.getBoard();
    }

    @Override
    int size() {
        return _boardOrigin.size();
    }

    @Override
    int spots(int r, int c) {
        return _boardOrigin.spots(r, c);
    }

    @Override
    int spots(int n) {
        return _boardOrigin.spots(n);
    }

    @Override
    Color color(int r, int c) {
        return _boardOrigin.color(r, c);
    }

    @Override
    Color color(int n) {
        return _boardOrigin.color(n);
    }

    @Override
    int numMoves() {
        return _boardOrigin.numMoves();
    }

    @Override
    Color whoseMove() {
        return _boardOrigin.whoseMove();
    }

    @Override
    boolean isLegal(Color player, int r, int c) {
        return _boardOrigin.isLegal(player, r, c);
    }

    @Override
    boolean isLegal(Color player) {
        return _boardOrigin.isLegal(player);
    }

    @Override
    int numOfColor(Color color) {
        return _boardOrigin.numOfColor(color);
    }

    @Override
    public boolean equals(Object obj) {
        return _boardOrigin.equals(obj);
    }

    @Override
    public int hashCode() {
        return _boardOrigin.hashCode();
    }

    /** Board to which all operations delegated. */
    private Board _boardOrigin;

    @Override
    /** Returns the board in Square[][] form. */
    Square[][] getBoard() {
        return _board;
    }

    @Override
    /** Sets the board. */
    void setBoard(Square[][] _board) {
        return;
    }

}
