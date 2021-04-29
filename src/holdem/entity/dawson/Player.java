package holdem.entity.dawson;

import java.util.Arrays;
import java.util.Scanner;

import holdem.engine.dawson.Blind;
import holdem.engine.dawson.Card;
import holdem.graphic.dawson.Drawable;
import holdem.hud.dawson.Align;
import holdem.hud.dawson.VBoxContainer;
import holdem.renderer.dawson.Renderer;
import holdem.renderer.dawson.ScreenSection;

public class Player extends Drawable {

	public boolean isPlaying = true;
	public final boolean isNPC;

	private Card hand[] = new Card[2];
	private Blind blind;
	
	private PlayerInfo info;
	
	private ScreenSection screenSection;

	// GRAPHICAL 
	private VBoxContainer hud;
		// Anchors are the x and y of the top left card.
	private int yAnchor = 0;
	private int xAnchor = this.cords[0] + 16;
	
	public Player(boolean isNPC, ScreenSection screenSection) {
		this.info = new PlayerInfo("Player", 20.00);
		this.isNPC = isNPC;
		this.screenSection = screenSection;

		switch(screenSection) {
			case TOPLEFT:
				this.cords = new int[]{0, 0};
				break;
			case TOPRIGHT:
				this.cords = new int[]{Renderer.WIDTH / 2, 0};
				break;
			case BOTTOMLEFT:
				this.cords = new int[]{0, Renderer.HEIGHT - Card.HEIGHT - 1};
				break;
			case BOTTOMRIGHT:
				this.cords = new int[]{Renderer.WIDTH / 2, Renderer.HEIGHT - Card.HEIGHT - 1};
				break;
		}
		this.setCardAnchors();
		
		int deltaX;
		int hudCardSeparator = 1;
		
		// Creates the HUD.
		if (screenSection == ScreenSection.BOTTOMLEFT || screenSection == ScreenSection.TOPLEFT) {
			deltaX = this.xAnchor - this.cords[0];
			this.hud = new VBoxContainer(
					this.cords[0],
					this.yAnchor + 1,
					deltaX,
					5,
					Align.alignRight(info.asStringArray(), deltaX - hudCardSeparator, ".")
					);
		} else {
			deltaX = this.xAnchor + (Card.WIDTH -4) * (this.hand.length - 1) + Card.WIDTH;
			this.hud = new VBoxContainer(
					deltaX + hudCardSeparator,
					this.yAnchor + 1,
					Renderer.WIDTH - deltaX,
					5,
					info.asStringArray()
					);
		}
	}
	
	private void setCardAnchors() {		
		if (this.screenSection == ScreenSection.BOTTOMLEFT) {
			this.yAnchor = this.cords[1] + 1;
		} else if (this.screenSection == ScreenSection.BOTTOMRIGHT) {
			this.yAnchor = this.cords[1] + 1;
			this.xAnchor = Renderer.WIDTH - Card.WIDTH -(Card.WIDTH - 4) * (hand.length - 1) - 16;
		} else if (this.screenSection == ScreenSection.TOPRIGHT) {			
			this.xAnchor = Renderer.WIDTH - Card.WIDTH -(Card.WIDTH - 4) * (hand.length - 1) - 16;
		}
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
			hand[i].setCords(new int[] {this.xAnchor + (Card.WIDTH - 4) * i, this.yAnchor});
			if (!this.isNPC) {
				hand[i].flip();
			}
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
		// +1 is a magic number?
		char[][] asciiArray = new char[Card.HEIGHT + 1][Renderer.WIDTH / 2];
		char identifier = (this.isNPC) ? 'n' : 'p';
		
		for (int y = 0; y < asciiArray.length; y++) {
			for (int x = 0; x < asciiArray[y].length; x++) {
				asciiArray[y][x] = identifier;
			}			
		}
		
		Arrays.asList(this.hand).forEach(c -> c.draw());
		this.hud.draw();
		//Renderer.Render(this.cords, asciiArray);
	}
}
