package holdem.engine.dawson;

import holdem.utils.dawson.Color;

public enum Action {

    /** Stop playing the current game */
    FOLD(Color.RED, 1),
    /** Don't do anything in the current turn.
     * Constraints: No money should be put in the table in that turn. */
    CHECK(Color.YELLOW, 2),  // Do nothing but keep playing IF
    /** Put money on the table if no one else has put.
     * Constraints: No one can have bet before in the round. */
    BET(Color.GREEN, 3, true),
    /** Matches the amount of the last bet or raise.
     * Constraints: Someone must have bet before in the round. */
    CALL(Color.PURPLE, 4),
    /** Raises the amount of the last bet.
     * Constraints: Someone must have bet before in the round. */
    RAISE(Color.WHITE, 5, true),
    /** Spends all the money in order to stay in the game
     * Constraints: Receives only a portion of the money */
    ALLIN(Color.WHITE, 6);

    public final int identifier;
    public final Color color;
    public final boolean hasArgs;
    Action(Color color, int identifier) {
        this(color, identifier, false);
    }

    Action(Color color, int identifier, boolean hasArgs) {
        this.identifier = identifier;
        this.color = color;
        this.hasArgs = hasArgs;
    }
}
