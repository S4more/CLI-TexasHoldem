package holdem.engine.dawson;

/**
 * Stores points and poker combinations so that the PokerEngine and the Game Engine can communicate
 * more easily.
 */
public class PokerResponse {
    public final PokerHands combination;
    /** The value of the highest card of a combination */
    public final int points;

    public PokerResponse(PokerHands combination, int points) {
        this.combination = combination;
        this.points = points;
    }
}