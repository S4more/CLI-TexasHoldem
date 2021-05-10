package holdem.engine.dawson;

public enum Blind {
	SMALL(5),
	BIG(10),
	NONE(0);

	public final double price;

	Blind(double price) {
		this.price = price;
	}

}
