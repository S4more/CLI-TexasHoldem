package holdem.engine.dawson;

public class PokerResponse {
    public final PokerHands combination;
    /** The value of the highest card of a combination */
    public final int points;

    public PokerResponse(PokerHands combination, int points) {
        this.combination = combination;
        this.points = points;
    }

}
