package jump61;

/**
 * Represents one square on a game board for jump61. Has both a color and a
 * number of spots.
 * @author jonathanking
 */
public class Square {
    /** The number of spots on a square. */
    private int _spots;
    /** The current color of a square. */
    private Color _color;
    /** The position of the square. */
    private int[] _position = { 0, 0 };
    /** The board that the square is on. */
    private Board _sqBoard;
    /**
     * Designates square as a corner, edge, or center type piece. Corner = 2,
     * edge = 3, center = 4.
     */
    private int locationType;

    /**
     * Initializes a new square that is white and has 0 spots. At position R C.
     */
    Square(int r, int c) {
        setSpots(0);
        setColor(Color.WHITE);
        _position[0] = r;
        _position[1] = c;
    }

    /**
     * Initializes a new square that is white and has no spots. At position R C
     * on Board B.
     */
    Square(int r, int c, Board b) {
        setSpots(0);
        setColor(Color.WHITE);
        _position[0] = r;
        _position[1] = c;
        _sqBoard = b;
        if (b != null) {
            locationType = this._sqBoard.neighbors(_position[0], _position[1]);
        }
    }

    /** Creates a square with SPOTS and a COLOR. At position R C on Board B. */
    Square(int spots, Color color, int r, int c, Board b) {
        setSpots(spots);
        setColor(color);
        _position[0] = r;
        _position[1] = c;
        _sqBoard = b;
        if (b != null) {
            locationType = this._sqBoard.neighbors(_position[0], _position[1]);
        }
    }

    /** Changes the color of the current square. */
    public void flipColor() {
        this.setColor(this.getColor().opposite());
    }

    /** Adds one spot of PLAYER 's color to the current square's spots. */
    public void addSpot(Color player) {
        this.setColor(player);
        setSpots(getSpots() + 1);
    }

    /** Returns the number of spots on the current Square. */
    public int getSpots() {
        return _spots;
    }

    /** Sets the number of spots on the current Square to SPOTS. */
    public void setSpots(int spots) {
        this._spots = spots;
    }

    /**
     * Returns the current COLOR of the Square.
     */
    public Color getColor() {
        return _color;
    }

    /**
     * Sets the current COLOR of the Square.
     */
    public void setColor(Color color) {
        this._color = color;
    }

    /** Returns a Square[] that holds the neighbors of this square. */
    Square[] neighboringSquares() {
        int r = _position[0];
        int c = _position[1];
        int pos = 0;
        Square[] list =
            new Square[_sqBoard.neighbors(_position[0], _position[1])];
        if (_sqBoard.exists(r + 2, c + 1)) {
            list[pos] = _sqBoard.getBoard()[r + 1][c];
            pos++;
        }
        if (_sqBoard.exists(r, c + 1)) {
            list[pos] = _sqBoard.getBoard()[r - 1][c];
            pos++;
        }
        if (_sqBoard.exists(r + 1, c + 2)) {
            list[pos] = _sqBoard.getBoard()[r][c + 1];
            pos++;
        }
        if (_sqBoard.exists(r + 1, c)) {
            list[pos] = _sqBoard.getBoard()[r][c - 1];
            pos++;
        }
        return list;
    }

    /** Returns a String for the Square. i.e. "red 2" or "blue 3". */
    public String toString() {
        if (getColor() == Color.WHITE && getSpots() == 0) {
            return "--";
        } else {
            return "" + getColor().toString().substring(0, 1) + getSpots();
        }
    }

    /**
     * Returns the value of locationType. Corner = 2, edge = 3, center = 4.
     */
    public int getLocationType() {
        return locationType;
    }

}
