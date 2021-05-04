package holdem.engine.dawson;

import holdem.graphic.dawson.Drawable;
import holdem.renderer.dawson.Pixel;
import holdem.renderer.dawson.Renderer;
import holdem.utils.dawson.Color;

public class Card extends Drawable {
	
	public static enum Suit {
		Clubs('♠'),
		Diamonds('♦'),
		Hearts('♥'),
		Spades('♣');
		
		public final char symbol;
		
		private Suit(char symbol) {
			this.symbol = symbol;
		}
	}
	
	/** It is an awesome idea to keep these numbers as odd, since there is no float
	 * points.  */
	public static final int HEIGHT = 7;
	public static final int WIDTH = HEIGHT + 2;	

	private final Suit suit; 
	private int value;
	private String rank;
	private boolean isUp = false;
	

	public Card(Suit suit, String rank) {
		
		this.suit = suit;
		this.rank = rank;
		
		switch (rank) {
			case "A":
				this.value = 14;
				break;
			case "J":
				this.value = 11;
				break;
			case "Q":
				this.value = 12;
				break;
			case "K":
				this.value = 13;
				break;
		}
	}
	
	public Suit getSuit() {
		return suit;
	}
	
	public int getValue() {
		return this.value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (getClass() != obj.getClass()) return false;
		Card card = (Card) obj;
		return this.value == card.getValue();
	}

	@Override
	public void draw() {
		Pixel[][] asciiArray = new Pixel[HEIGHT][WIDTH];

		final Color color = Color.WHITE;
		
		asciiArray[0][0] = new Pixel('┌', color);
		asciiArray[0][WIDTH - 1] = new Pixel('┐', color);
		asciiArray[HEIGHT - 1][0] = new Pixel('└', color);
		asciiArray[HEIGHT - 1][WIDTH -1] = new Pixel('┘', color);
		
		if (this.isUp) {	
			for (int i = 1; i < HEIGHT - 1; i++) {
				asciiArray[i][0] = new Pixel('│', color);
				asciiArray[i][WIDTH - 1] = new Pixel('│', color);
				for (int x = 1; x < WIDTH -1; x++) {
					asciiArray[i][x] = new Pixel(' ', Color.GREEN);
				}
			}
			
			asciiArray[HEIGHT / 2][WIDTH / 2] = new Pixel(this.suit.symbol, Color.RED);
			
			if (this.rank.contains("10")) {
				asciiArray[1][1] = new Pixel('1', color);
				asciiArray[1][2] = new Pixel('0', color);
				
				asciiArray[HEIGHT - 2][WIDTH - 2] = new Pixel('0', color);
				asciiArray[HEIGHT - 2][WIDTH - 3] = new Pixel('1', color);
				
			} else {	
				asciiArray[1][1] = new Pixel(this.rank.charAt(0), color);
				asciiArray[HEIGHT - 2][WIDTH - 2] = new Pixel(this.rank.charAt(0), color);
			}			
			
		}	
		
		else {
			for (int i = 1; i < HEIGHT - 1; i++) {
				asciiArray[i][0] = new Pixel('│', color);
				for (int x = 1; x < WIDTH - 1 ; x++) {
					asciiArray[i][x] = new Pixel('▒', color);
				}
				asciiArray[i][WIDTH - 1] = new Pixel('│', color);
			}				

		}

		for (int i = 1; i < WIDTH - 1; i++) {
			asciiArray[0][i] = new Pixel('─', color); // Top border
			asciiArray[HEIGHT - 1][i] = new Pixel('─', color); // Bottom border
		}
		
		Renderer.Render(this.cords, asciiArray);
		
	}
	
	public void setCords(int[] cords) {
		this.cords = cords;
	}
	
	public void flip() {
		this.isUp = !this.isUp;
	}

}
