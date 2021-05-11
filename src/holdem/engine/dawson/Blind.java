package holdem.engine.dawson;

/**
 * An enum for all the possible Blinds in a poker game.
 */
public enum Blind {
	SMALL(5),
	BIG(10),
	NONE(0);

	public final double price;

	Blind(double price) {
		this.price = price;
	}

}
