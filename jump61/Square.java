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

    /** Initializes a new square that is white and has no spots. */
    Square() {
        setSpots(0);
        setColor(Color.WHITE);
    }

    /** Creates a square SPOTS spots and a COLOR. */
    Square(int spots, Color color) {
        setSpots(spots);
        setColor(color);
    }

    /** Changes the color of the current square. */
    public void flipColor() {
        this.setColor(this.getColor().opposite());
    }

    /** Adds one spot to the current square's spots. */
    public void addSpot(Color player) {
        this.setColor(player);
        setSpots(getSpots() + 1);
        // check if necessary to flip squares
        // actually, i think the Board.jump() method will take care of this

    }

    /** Gets the number of spots on the current Square. */
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

    /** Returns a String for the Square. i.e. "red 2" or "blue 3". */
    public String toString() {
        if (getColor() == Color.WHITE && getSpots() == 0) {
            return "--";
        } else {
            return "" + getColor().toString().substring(0, 1) + getSpots();
        }
    }

}
