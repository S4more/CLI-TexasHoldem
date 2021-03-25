package gui.game.dawson;

import java.util.Scanner;

public class Player extends Drawable {

	public boolean isPlaying = true;
	public final boolean isNPC;

	private Card hand[] = new Card[2];
	private Blind blind;
	
	public Player(boolean isNPC, int[] cords) {
		this.cords = cords;
		this.isNPC = isNPC;
	}
	
	public Blind getBlind() {
		return this.blind;
	}
	
	public Card[] getHand() {
		return this.hand;
	}
	
	public void setBlind(Blind blind) {
		this.blind = blind;
	}
	
	public void setHand(Card[] hand) {
		this.hand = hand;	

		for (int i = 0; i < hand.length; i++) {
			hand[i].setCords(new int[] {Renderer.WIDTH / 2 - Card.WIDTH * hand.length + Card.WIDTH * (i + 1), this.cords[1]});
			hand[i].flip();
			Renderer.addDrawable(hand[i]);
		}
		
	}
	
	public void getInput(Scanner input) {
		System.out.println("Do you want to fold? [y/N]");
		String ans = input.nextLine();
		if (ans.toLowerCase().contains("y")) {
			this.isPlaying = false;
		}
		
	}

	@Override
	public void draw() {
		char[][] asciiArray = new char[Renderer.HEIGHT / 5][Renderer.WIDTH];
		
		for (int y = 0; y < asciiArray.length; y++) {
			for (int x = 0; x < asciiArray[y].length; x++) {
				asciiArray[y][x] = 'p';
			}			
		}
		
		//Renderer.Render(this.cords, asciiArray);
		
	}
	
}
