package jump61;

import java.io.Reader;
import java.io.Writer;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Random;

import static jump61.Color.*;

/**
 * Main logic for playing (a) game(s) of Jump61.
 * @author Jonathan King
 */
class Game {

    /** Name of resource containing help message. */
    private static final String HELP = "jump61/Help.txt";

    /** If true, program will execute. Once false, will terminate play(). */
    private boolean _session = true;

    /**
     * A new Game that takes command/move input from INPUT, prints normal output
     * on OUTPUT, prints prompts for input on PROMPTS, and prints error messages
     * on ERROROUTPUT. The Game now "owns" INPUT, PROMPTS, OUTPUT, and
     * ERROROUTPUT, and is responsible for closing them when its play method
     * returns.
     */
    Game(Reader input, Writer prompts, Writer output, Writer errorOutput) {
        _board = new MutableBoard(Defaults.BOARD_SIZE);
        _readonlyBoard = new ConstantBoard(_board);
        _prompter = new PrintWriter(prompts, true);
        _inp = new Scanner(input);
        _inp.useDelimiter("(?m)$|^|\\p{Blank}+");
        _out = new PrintWriter(output, true);
        _err = new PrintWriter(errorOutput, true);
    }

    /**
     * Returns a readonly view of the game board. This board remains valid
     * throughout the session.
     */
    Board getBoard() {
        return _board;
    }

    /**
     * Play a session of Jump61. This may include multiple games, and proceeds
     * until the user exits. Returns an exit code: 0 is normal; any positive
     * quantity indicates an error.
     */
    int play() {
        _out.print("Welcome to " + Defaults.VERSION + ", ");
        _out.println("where everything's made up and the points don't matter!");
        while (_session) {
            while (!_playing) {
                promptForNext();
                readExecuteCommand();
            }
            while (_board.whoseMove() == RED && _playing) {

                _red.makeMove();
                clearMove();
                checkForWin();
                if (_verbose && _playing) {
                    _out.println(_board.toDisplayString());
                }
            }
            if (!_playing) {
                continue;
            }
            while (_board.whoseMove() == BLUE && _playing) {
                _blue.makeMove();
                clearMove();
                checkForWin();
                if (_verbose && _playing) {
                    _out.println(_board.toDisplayString());
                }
            }
        }
        _out.flush();
        return 0;
    }

    /** Sets the items in _move to [0, 0]. */
    void clearMove() {
        _move[0] = _move[1] = 0;
    }

    /**
     * Get a move from my input and place its row and column in MOVE. Returns
     * true if this is successful, false if game stops or ends first.
     */
    boolean getMove() {
        while (_playing && _move[0] == 0 && promptForNext()) {

            readExecuteCommand();
        }
        return (_move[0] > 0);
    }

    /** Add a spot to R C, if legal to do so. */
    void makeMove(int r, int c) {
        Color who = _board.whoseMove();
        if (_board.isLegal(who, r, c)) {
            _board.addSpot(who, r, c);
            _board._moves++;
        } else {
            reportError("The move to <%d, %d> is not legal.", r, c);
        }

    }

    /** Add a spot to square #N, if legal to do so. */
    void makeMove(int n) {
        makeMove(_board.row(n), _board.col(n));
    }

    /**
     * Return a random integer in the range [0 .. N), uniformly distributed.
     * Requires N > 0.
     */
    int randInt(int n) {
        return _random.nextInt(n);
    }

    /**
     * Send a message to the user as determined by FORMAT and ARGS, which are
     * interpreted as for String.format or PrintWriter.printf.
     */
    void message(String format, Object... args) {
        _out.printf(format, args);
    }

    /**
     * Check whether we are playing and there is an unannounced winner. If so,
     * announce and stop play.
     */
    void checkForWin() {
        Color winner = getBoard().getWinner();
        if (_playing && (winner != null)) {
            announceWinner();
            _playing = false;

        }
    }

    /** Send announcement of winner to my user output. */
    private void announceWinner() {
        message("%s wins.\n", getBoard().getWinner().toCapitalizedString());
    }

    /** Make PLAYER an AI for subsequent moves. */
    private void setAuto(Color player) {
        if (player == Color.RED) {
            _red = new AI(this, Color.RED);
        } else if (player == Color.BLUE) {
            _blue = new AI(this, Color.BLUE);
        }
    }

    /** Make PLAYER take manual input from the user for subsequent moves. */
    private void setManual(Color player) {
        if (player == Color.RED) {
            _red = new HumanPlayer(this, Color.RED);
        } else if (player == Color.BLUE) {
            _blue = new HumanPlayer(this, Color.BLUE);
        }
    }

    /** Print the current board using standard board-dump format. */
    private void dump() {
        _out.println(_board);
    }

    /** Print a help message. */
    private void help() {
        Main.printHelpResource(HELP, _out);
    }

    /** Seed the random-number generator with SEED. */
    private void setSeed(long seed) {
        _random.setSeed(seed);
    }

    /**
     * Save move R C in _move. Error if R and C do not indicate an existing
     * square on the current board.
     */
    private void saveMove(int r, int c) {
        if (!_board.exists(r, c)) {
            reportError("move %d %d out of bounds", r, c);
            return;
        }
        _move[0] = r;
        _move[1] = c;
    }

    /**
     * Read and execute one command. Leave the input at the start of a line, if
     * there is more input.
     */
    private void readExecuteCommand() {
        String command = "";
        command = _inp.nextLine().trim();
        if (command.isEmpty() || command.matches("\\s+")) {
            return;
        }
        String[] commands = command.split("\\s+");
        if (commands.length == 2 && commands[0].matches("\\d+")
            && commands[1].matches("\\d+") && _playing) {
            int r = Integer.parseInt(commands[0]);
            int c = Integer.parseInt(commands[1]);
            saveMove(r, c);
            return;
        }
        String[] args = new String[commands.length - 1];
        java.lang.System.arraycopy(commands, 1, args, 0, args.length);
        executeCommand(commands[0], args);
    }

    /**
     * Place SPOTS spots on square R:C and color the square red or blue
     * depending on whether COLOR is "r" or "b". If SPOTS is 0, clears the
     * square, ignoring COLOR. SPOTS must be less than the number of neighbors
     * of square R, C.
     */
    private void setSpots(int r, int c, int spots, String color) {
        Color p = Color.WHITE;
        if (color.equals("r")) {
            p = Color.RED;
        } else if (color.equals("b")) {
            p = Color.BLUE;
        }
        playFalse();
        _board.set(r, c, spots, p);
    }

    /**
     * Stop any current game and set the board to an empty N x N board with
     * numMoves() == 0.
     */
    private void setSize(int n) {
        playFalse();
        if (n <= 1) {
            reportError("Error: %d is not a valid board size.", n);
            return;
        }
        _board.clear(n);
    }

    /** Starts the game and clears the board if a previous game has finished. */
    void startPlaying() {
        _playing = true;
        if (_board.getWinner() != null) {
            _board.clear(_board.size());
        }
    }

    /** Sets the number of moves to X. */
    void setNumMoves(int x) {
        playFalse();
        if (x <= 0) {
            reportError("%d must be larger than zero.", x);
            return;
        }
        _board.setMoves(x);
    }

    /**
     * Gather arguments ARGS and execute command CMND. Throws GameException on
     * errors.
     */
    private void executeCommand(String cmnd, String[] args) {
        try {
            switch (cmnd) {
            case "\n":
            case "\r\n":
                return;
            case "#":
                break;
            case "clear":
                playFalse();
                _board.clear(_board.size());
                break;
            case "start":
                startPlaying();
                break;
            case "quit":
                System.exit(0);
                break;
            case "auto":
                playFalse();
                setAuto(Color.parseColor(args[0]));
                break;
            case "manual":
                playFalse();
                setManual(Color.parseColor(args[0]));
                break;
            case "size":
                setSize(Integer.parseInt(args[0]));
                break;
            case "move":
                setNumMoves(Integer.parseInt(args[0]));
                break;
            case "set":
                setSpots(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                    Integer.parseInt(args[2]), args[3]);
                break;
            case "dump":
                dump();
                break;
            case "seed":
                setSeed(Long.parseLong(args[0]));
                break;
            case "verbose":
                _verbose = true;
                break;
            case "quiet":
                _verbose = false;
                break;
            case "help":
                help();
                break;
            default:
                defaultExecutionError(cmnd, args);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            reportError("%s", "Wrong number of arguments for command.");
        }
    }

    /**
     * Default behavior for the executeCommand function. Requires a COMMAND and
     * ARGS.
     */
    void defaultExecutionError(String command, String[] args) {
        if (_playing) {
            reportError("Syntax error in move command.");
        } else {
            reportError("bad command: '%s'", command);
        }
    }

    /**
     * Print a prompt and wait for input. Returns true iff there is another
     * token.
     */
    private boolean promptForNext() {

        String prompt = "> ";
        Color cur = _board.whoseMove();
        if ((_playing && cur == RED && !(_red instanceof AI))
            || (_playing && cur == BLUE && !(_blue instanceof AI))) {
            prompt = cur + "> ";
        }
        _prompter.print(prompt);
        _prompter.flush();
        return _inp.hasNext();

    }

    /** Changes the boolean value of _PLAYING to FALSE. */
    void playFalse() {
        _playing = false;
    }

    /**
     * Send an error message to the user formed from arguments FORMAT and ARGS,
     * whose meanings are as for printf.
     */
    void reportError(String format, Object... args) {
        _err.print("Error: ");
        _err.printf(format, args);
        _err.println();
    }

    /** Writer on which to print prompts for input. */
    private final PrintWriter _prompter;
    /**
     * Scanner from current game input. Initialized to return newlines as
     * tokens.
     */
    private final Scanner _inp;
    /** Outlet for responses to the user. */
    private final PrintWriter _out;
    /** Outlet for error responses to the user. */
    private final PrintWriter _err;

    /** The board on which I record all moves. */
    private final MutableBoard _board;
    /** A readonly view of _board. */
    private final Board _readonlyBoard;

    /** A pseudo-random number generator used by players as needed. */
    private final Random _random = new Random();

    /** True iff a game is currently in progress. */
    private boolean _playing;

    /** If true, display the board after each move. */
    private boolean _verbose = false;

    /** The player with the color RED. */
    private Player _red = new HumanPlayer(this, RED);
    /** The player with the color BLUE. */
    private Player _blue = new HumanPlayer(this, BLUE);

    /**
     * Used to return a move entered from the console. Allocated here to avoid
     * allocations.
     */
    private final int[] _move = new int[2];

    /** Returns the _MOVE holder. */
    int[] moveHolder() {
        return _move;
    }

    /**
     * Returns the value of _VERBOSE.
     */
    public boolean getVerbose() {
        return _verbose;
    }

}
