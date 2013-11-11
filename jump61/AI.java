package jump61;

import java.util.ArrayList;

/** An automated Player.
 *  @author Jonathan
 */
class AI extends Player {

    /** A new player of GAME initially playing COLOR that chooses
     *  moves automatically.
     */
    AI(Game game, Color color) {
        super(game, color);
        // FIXME
    }

    @Override
    void makeMove() {
        Move m = findBestMove(getColor(), getBoard(), 4, 350);
        getGame().makeMove(m.getR(), m.getC());
        getGame().message("%s moves %d %d.", getColor().toCapitalizedString(), m.getR(), m.getC());
        getGame().message("%s", "\n");
    }

    /** Return the minimum of CUTOFF and the minmax value of board B
     *  (which must be mutable) for player P to a search depth of D
     *  (where D == 0 denotes evaluating just the next move).
     *  If MOVES is not null and CUTOFF is not exceeded, set MOVES to
     *  a list of all highest-scoring moves for P; clear it if
     *  non-null and CUTOFF is exceeded. the contents of B are
     *  invariant over this call. */
    @SuppressWarnings({ "unused", "unused" })
    private int minmax(Color p, Board b, int d, int cutoff, ArrayList<Integer> moves) {
        return 0;
        // FIXME
    }

    
    /**
     * A legal move for WHO that either has an estimated value >= CUTOFF or that
     * has the best estimated value for player WHO, starting from position
     * START, and looking up to DEPTH moves ahead.
     */
    public Move findBestMove(Color who, Board start, int depth, double cutoff) {

        if (start.getWinner() == who)
            return WON_GAME;
        else if (start.getWinner() == who.opposite())
            return LOST_GAME;
        else if (depth == 0)
            return guessBestMove(who, start, cutoff);

        Move bestSoFar = reallyBadMove(who, start);
        for (Move M : getLegalMoves(who, start)) {
            Board next = new MutableBoard(start);//start.makeMove(M);
            makeMove(M, next, who);
            Move response =
                findBestMove(who.opposite(), next, depth - 1,
                    -bestSoFar.value());

            if (-response.value() > bestSoFar.value()) {
                M.setValue(-response.value()); // value for who = - value for
                                               // opponent
                bestSoFar = M;
                if (M.value() >= cutoff)
                    break;
            }
        }
        return bestSoFar;
    }

    private ArrayList<Move> getLegalMoves(Color who, Board start) {
        int N = start.size();
        ArrayList<Move> moves = new ArrayList<Move>();
    
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                Color cur = start.getBoard()[r][c].getColor();
                if(cur == who || cur == Color.WHITE) {
                    moves.add(new Move(r, c));
                }
            }
        }
        return moves;
    }

    private Move guessBestMove(Color who, Board start, double cutoff) {

        Move bestSoFar;
        bestSoFar = reallyBadMove(who, start);
        for (Move M : getLegalMoves(who, start)) {
//            Board next = start.makeMove(M);
            Board next = new MutableBoard(start);
            makeMove(M, next, who);
            M.setValue(staticEval(who, next));
            if (M.value() > bestSoFar.value()) {
                bestSoFar = M;
                if (M.value() >= cutoff)
                    break;
            }
        }
        return bestSoFar;

    }

    /** Returns heuristic value of board B for player P.
     *  Higher is better for P. */
    private int staticEval(Color p, Board b) {
        if (b.getWinner() == p) {
            return WON_GAME.value();
        }
        int size = b.size();
        int mySquares = b.numOfColor(p);
        int oppSquares = b.numOfColor(p.opposite());
        int val = 0;
        val = mySquares - oppSquares;
        val += cornersNotOwnedByPlayer(p, b) + cornersOwnedByPlayer(p, b) + highWeightSquares(p,b);
        return val;
    }

    /**
     * Returns the *value* of high-weight squares with a lot of spots and a lot
     * of neighbors. Increases value if square is ready to be flipped and it's
     * neighbors are as well.
     */
    int highWeightSquares(Color p, Board b) {
        int x = 0;
        int N = b.size();
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                Square orig = b.getBoard()[r][c];
                if (orig.getSpots() == orig.getLocationType()) {
                    x += 50;
                    Square[] neigh = orig.neighboringSquares();
                    for (Square s : neigh) {
                        if (s.getLocationType() == 2 && s.getSpots() == 2) {
                            x += 5;
                        } else if (s.getLocationType() == 3
                            && s.getSpots() == 3) {
                            x += 8;
                        } else if ((s.getLocationType() == 4 && s.getSpots() == 4)) {
                            x += 10;
                        }
                    }
                }
            }
        }
        return x;

    }
    
    /** Returns *value* of corner squares on B that belong to P. */
    int cornersOwnedByPlayer(Color p, Board b) {
        int x = 0;
        Square topL = b.getBoard()[0][0];
        Square topR = b.getBoard()[0][b.size()-1];
        Square bottomL = b.getBoard()[b.size()-1][0];
        Square bottomR = b.getBoard()[b.size()-1][b.size()-1];
        Square[] corners = {topL, topR, bottomL, bottomR};
        for (Square s : corners) {
            if (s.getColor() == p) {
                x += 2;
                if (s.getSpots() == 2){
                    x += 5;
                }
            }
        }
        return x;
    }
    
    /** Returns *value* of corner squares on B that are waiting to be claimed. */
    int cornersNotOwnedByPlayer(Color p, Board b) {
        int x = 0;
        Square topL = b.getBoard()[0][0];
        Square topR = b.getBoard()[0][b.size()-1];
        Square bottomL = b.getBoard()[b.size()-1][0];
        Square bottomR = b.getBoard()[b.size()-1][b.size()-1];
        Square[] corners = {topL, topR, bottomL, bottomR};
        for (Square s : corners) {
            if (s.getColor() == p) {
                x += 100;
            }
        }
        return x;
    }
    
    /** Infinity. */
    private final int INFINITY = 7777777;
    
    /** A Move with a value that represents a forced win. */
    private final Move WON_GAME = new Move(INFINITY);

    /** A 7-digit number that represents the value of a game */
    private final Move LOST_GAME = new Move(-INFINITY);
    
    /** A Move with a value of -100. */
    private final Move REALLY_BAD_MOVE = new Move(-100);

    /** Returns a REALL BAD MOVE with a low value. */
    Move reallyBadMove(Color c, Board b) {
        Move m = getLegalMoves(c, b).get(0);
        m.setValue(-100);
        return m;
    }
}


