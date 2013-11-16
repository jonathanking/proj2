package jump61;

import java.util.ArrayList;

/**
 * An automated Player.
 * @author Jonathan
 */
class AI extends Player {

    /**
     * A new player of GAME initially playing COLOR that chooses moves
     * automatically.
     */
    AI(Game game, Color color) {
        super(game, color);

    }

    @Override
    void makeMove() {
        Move m = findBestMove(getColor(), getBoard(), 4, (hundred * 3) / 2);
        getGame().makeMove(m.getR(), m.getC());
        getGame().message("%s moves %d %d.", getColor().toCapitalizedString(),
            m.getR(), m.getC());
        getGame().message("%s", "\n");
    }

    /**
     * Return the minimum of CUTOFF and the minmax value of board B (which must
     * be mutable) for player P to a search depth of D (where D == 0 denotes
     * evaluating just the next move). If MOVES is not null and CUTOFF is not
     * exceeded, set MOVES to a list of all highest-scoring moves for P; clear
     * it if non-null and CUTOFF is exceeded. the contents of B are invariant
     * over this call.
     */
    @SuppressWarnings({ "unused", "unused" })
    private int minmax(Color p, Board b, int d, int cutoff,
        ArrayList<Integer> moves) {
        return 0;

    }

    /**
     * Returns legal move for WHO that either has an estimated value >= CUTOFF
     * or that has the best estimated value for player WHO, starting from
     * position START, and looking up to DEPTH moves ahead.
     */
    public Move findBestMove(Color who, Board start, int depth, double cutoff) {
        Board empty = new MutableBoard(start.size());
        if (start.getBoard() == empty.getBoard()) {
            return new Move(0, 0);
        }

        if (start.getWinner() == who) {
            return _wonGame;
        } else if (start.getWinner() == who.opposite()) {
            return _lostGame;
        } else if (depth == 0) {
            return guessBestMove(who, start, cutoff);
        }

        Move bestSoFar = reallyBadMove(who, start);
        for (Move M : getLegalMoves(who, start)) {
            Board next = new MutableBoard(start);
            makeMove(M, next, who);
            Move response =
                findBestMove(who.opposite(), next, depth - 1,
                    -bestSoFar.value());

            if (-response.value() > bestSoFar.value()) {
                M.setValue(-response.value());
                bestSoFar = M;
                if (M.value() >= cutoff) {
                    break;
                }
            }
        }
        return bestSoFar;
    }

    /**
     * Returns an ArrayList<Move> that is a list of every legal move for WHO on
     * START.
     */
    ArrayList<Move> getLegalMoves(Color who, Board start) {
        int N = start.size();
        ArrayList<Move> moves = new ArrayList<Move>();

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                Color cur = start.getBoard()[r][c].getColor();
                if (cur == who || cur == Color.WHITE) {
                    moves.add(new Move(r, c));
                }
            }
        }
        return moves;
    }

    /** Returns a guess of the best move for WHO on START with CUTOFF. */
    Move guessBestMove(Color who, Board start, double cutoff) {
        Move bestSoFar;
        bestSoFar = reallyBadMove(who, start);
        for (Move M : getLegalMoves(who, start)) {
            Board next = new MutableBoard(start);
            makeMove(M, next, who);
            M.setValue(staticEval(who, next));
            if (M.value() > bestSoFar.value()) {
                bestSoFar = M;
                if (M.value() >= cutoff) {
                    break;
                }
            }
        }
        return bestSoFar;

    }

    /**
     * Returns heuristic value of board B for player P. Higher is better for P.
     */
    public int staticEval(Color p, Board b) {
        if (b.getWinner() == p) {
            return _wonGame.value();
        } else if (b.getWinner() == p.opposite()) {
            return _lostGame.value();
        }
        int size = b.size();
        int mySquares = b.numOfColor(p);
        int oppSquares = b.numOfColor(p.opposite());
        int val = 0;
        val = (mySquares - oppSquares) * hundred;
        return val;
    }

    /**
     * Returns the *value* of high-weight squares with a lot of spots and a lot
     * of neighbors. Increases value if square is ready to be flipped and it's
     * neighbors are as well. True for player P on board B.
     */
    int highWeightSquares(Color p, Board b) {
        int x = 0;
        int N = b.size();
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                Square orig = b.getBoard()[r][c];
                if (orig.getSpots() == orig.getLocationType()) {
                    x += hundred / 2;
                    Square[] neigh = orig.neighboringSquares();
                    for (Square s : neigh) {
                        if (s.getLocationType() == 2 && s.getSpots() == 2) {
                            x += 5;
                        } else if (s.getLocationType() == 3
                            && s.getSpots() == 3) {
                            x += 8;
                        } else if ((s.getLocationType() == 4
                            && s.getSpots() == 4)) {
                            x += 10 * 3;
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
        Square topR = b.getBoard()[0][b.size() - 1];
        Square bottomL = b.getBoard()[b.size() - 1][0];
        Square bottomR = b.getBoard()[b.size() - 1][b.size() - 1];
        Square[] corners = { topL, topR, bottomL, bottomR };
        for (Square s : corners) {
            if (s.getColor() == p) {
                x += 2;
                if (s.getSpots() == 2) {
                    x += 5;
                }
            }
        }
        return x;
    }

    /**
     * Returns *value* of corner squares on B that are waiting to be claimed by
     * P.
     */
    int cornersNotOwnedByPlayer(Color p, Board b) {
        int x = 0;
        Square topL = b.getBoard()[0][0];
        Square topR = b.getBoard()[0][b.size() - 1];
        Square bottomL = b.getBoard()[b.size() - 1][0];
        Square bottomR = b.getBoard()[b.size() - 1][b.size() - 1];
        Square[] corners = { topL, topR, bottomL, bottomR };
        for (Square s : corners) {
            if (s.getColor() == p) {
                x += hundred;
            }
        }
        return x;
    }

    /** Infinity. */
    private final int _infinity = Integer.MAX_VALUE;

    /** A Move with a value that represents a forced win. */
    private final Move _wonGame = new Move(_infinity);

    /** A 7-digit number that represents the value of a game. */
    private final Move _lostGame = new Move(-_infinity);

    /** Hundred. */
    private final int hundred = 100;

    /** Returns a REALLY BAD MOVE with a low value. Takes C and B. */
    Move reallyBadMove(Color c, Board b) {
        Move m = getLegalMoves(c, b).get(0);
        m.setValue(-hundred);
        return m;
    }
}
