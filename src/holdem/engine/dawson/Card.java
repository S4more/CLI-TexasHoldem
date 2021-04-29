package holdem.engine.dawson;

import holdem.graphic.dawson.Drawable;
import holdem.renderer.dawson.Renderer;

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
		char[][] asciiArray = new char[HEIGHT][WIDTH];
		
		asciiArray[0][0] = '┌';    
		asciiArray[0][WIDTH - 1] = '┐';
		asciiArray[HEIGHT - 1][0] = '└';
		asciiArray[HEIGHT - 1][WIDTH -1] = '┘';
		
		if (this.isUp) {	
			for (int i = 1; i < HEIGHT - 1; i++) {
				asciiArray[i][0] = '┃';	
				asciiArray[i][WIDTH - 1] = '┃';	
				for (int x = 1; x < WIDTH -1; x++) {
					asciiArray[i][x] = ' ';
				}
			}
			
			asciiArray[HEIGHT / 2][WIDTH / 2] = this.suit.symbol;
			
			if (this.rank.contains("10")) {
				asciiArray[1][1] = '1';
				asciiArray[1][2] = '0';	
				
				asciiArray[HEIGHT - 2][WIDTH - 2] = '0';
				asciiArray[HEIGHT - 2][WIDTH - 3] = '1';
				
			} else {	
				asciiArray[1][1] = this.rank.charAt(0);
				asciiArray[HEIGHT - 2][WIDTH - 2] = this.rank.charAt(0);
			}			
			
		}	
		
		else {
			for (int i = 1; i < HEIGHT - 1; i++) {
				asciiArray[i][0] = '┃';	
				for (int x = 1; x < WIDTH - 1 ; x++) {
					asciiArray[i][x] = '▒';
				}
				asciiArray[i][WIDTH - 1] = '┃';	
			}				

		}

		for (int i = 1; i < WIDTH - 1; i++) {
			asciiArray[0][i] = '━'; // Top border
			asciiArray[HEIGHT - 1][i] = '━'; // Bottom border
		}
		
		Renderer.Render(this.cords, asciiArray);;
		
	}
	
	public void setCords(int[] cords) {
		this.cords = cords;
	}
	
	public void flip() {
		this.isUp = !this.isUp;
	}

}
