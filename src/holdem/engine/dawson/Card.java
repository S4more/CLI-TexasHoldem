package holdem.engine.dawson;

import holdem.graphic.dawson.Drawable;
import holdem.renderer.dawson.Pixel;
import holdem.renderer.dawson.RenderType;
import holdem.renderer.dawson.Renderer;
import holdem.utils.dawson.Color;

public class Card extends Drawable {
	
	public enum Suit {
		Clubs('♠'),
		Diamonds('♦'),
		Hearts('♥'),
		Spades('♣');
		
		public final char symbol;
		
		Suit(char symbol) {
			this.symbol = symbol;
		}
	}
	
	/** It is an awesome idea to keep these numbers as odd, since there is no float
	 * points.  */
	public static final int HEIGHT = 7;
	public static final int WIDTH = HEIGHT + 2;	

	private final Suit suit;
	/** The int representation of the card's rank */
	private int value;
	private String rank;
	private boolean isUp = false;
	private Color color = Color.WHITE;

	public Card(int x, int y, Suit suit, String rank) {
		super(x, y);
		this.suit = suit;
		this.rank = rank;

		switch (rank) {
			case "1":
				this.value = 14;
				this.rank = "A";
				break;
			case "11":
				this.value = 11;
				this.rank = "J";
				break;
			case "12":
				this.value = 12;
				this.rank = "Q";
				break;
			case "13":
				this.value = 13;
				this.rank = "K";
				break;
			default:
				this.value = Integer.parseInt(rank);
				break;
		}

	}

	/**
	 * Compare two cards based on their rank
	 * @param obj object to compare
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (getClass() != obj.getClass()) return false;
		Card card = (Card) obj;
		return this.value == card.getValue();
	}

	/**
	 * Writes the card visual representation on an ascii array and
	 * registers it to the Renderer.bufferArray.
	 * @param rt RenderType.
	 */
	@Override
	public void draw(RenderType rt) {
		if (rt != RenderType.NORMAL) return;
		Pixel[][] asciiArray = new Pixel[HEIGHT][WIDTH];

		Color borderColor = this.color;
		// Corner section
		asciiArray[0][0] = new Pixel('┌', borderColor);
		asciiArray[0][WIDTH - 1] = new Pixel('┐', borderColor);
		asciiArray[HEIGHT - 1][0] = new Pixel('└', borderColor);
		asciiArray[HEIGHT - 1][WIDTH -1] = new Pixel('┘', borderColor);
		
		if (this.isUp) {
			writeCardFaceToArray(asciiArray, borderColor);
		} else {
			writeCardBackToArray(asciiArray, borderColor);
		}

		// Generates the horizontal borders of the card.
		for (int i = 1; i < WIDTH - 1; i++) {
			asciiArray[0][i] = new Pixel('─', borderColor); // Top border
			asciiArray[HEIGHT - 1][i] = new Pixel('─', borderColor); // Bottom border
		}
		
		Renderer.Render(this.cords, asciiArray);
	}

	/**
	 * Write the vertical borders and the back of the card to a given ascii array.
	 * @param asciiArray A two dimensional pixel array representing the card visuals.
	 * @param borderColor The color of the to-be generated borders.
	 */
	private void writeCardBackToArray(Pixel asciiArray[][], Color borderColor) {
		for (int i = 1; i < HEIGHT - 1; i++) {
			asciiArray[i][0] = new Pixel('│', borderColor);
			for (int x = 1; x < WIDTH - 1 ; x++) {
				asciiArray[i][x] = new Pixel('▒', Color.WHITE);
			}
			asciiArray[i][WIDTH - 1] = new Pixel('│', borderColor);
		}


	}

	/**
	 * Write the vertical borders and number of the card to a given asciiArray
	 * @param asciiArray A two dimensional Pixel Array representing the card visuals.
	 * @param borderColor The color of the to-be generated borders.
	 */
	private void writeCardFaceToArray(Pixel asciiArray[][], Color borderColor) {
		//Generates the sides of the border
		for (int i = 1; i < HEIGHT - 1; i++) {
			asciiArray[i][0] = new Pixel('│', borderColor);
			asciiArray[i][WIDTH - 1] = new Pixel('│', borderColor);
			// The empty space in the middle of the card.
			for (int x = 1; x < WIDTH -1; x++) {
				asciiArray[i][x] = new Pixel(' ', Color.WHITE);
			}
		}

		// Insert the symbol in the middle of the card.
		asciiArray[HEIGHT / 2][WIDTH / 2] = new Pixel(this.suit.symbol, Color.RED);

		// When the rank is 10, we need to reserve two Pixels to write it.
		if (this.rank.contains("10")) {
			asciiArray[1][1] = new Pixel('1', Color.WHITE);
			asciiArray[1][2] = new Pixel('0', Color.WHITE);

			asciiArray[HEIGHT - 2][WIDTH - 2] = new Pixel('0', Color.WHITE);
			asciiArray[HEIGHT - 2][WIDTH - 3] = new Pixel('1', Color.WHITE);
		} else {
			asciiArray[1][1] = new Pixel(this.rank.charAt(0), Color.WHITE);
			asciiArray[HEIGHT - 2][WIDTH - 2] = new Pixel(this.rank.charAt(0), Color.WHITE);
		}
	}

	@Override
	public int getWidth() {
		return WIDTH;
	}

	@Override
	public int getHeight() {
		return HEIGHT;
	}

	public boolean isUp() {
		return isUp;
	}

	public Suit getSuit() {
		return suit;
	}

	public int getValue() {
		return this.value;
	}

	public void setCords(int[] cords) {
		this.cords = cords;
	}
	
	public void flip() {
		this.isUp = !this.isUp;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
