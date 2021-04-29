package holdem.game.dawson;

import java.util.ArrayList;
import java.util.Scanner;

import holdem.engine.dawson.Card;
import holdem.engine.dawson.Turn;
import holdem.entity.dawson.Player;
import holdem.graphic.dawson.Drawable;
import holdem.renderer.dawson.Renderer;

public class Table extends Drawable {
	public final int HEIGHT = Card.HEIGHT + 4;
	public final int WIDTH = (Card.WIDTH * 5) + 4;
	
	private ArrayList<Card> cards = new ArrayList<Card>();
	private Deck deck = new Deck();
	private int cardHorizontalGap = 2;
	private ArrayList<Player> players = new ArrayList<Player>(); 

	private Turn turn = Turn.FLOP;
	private Scanner input = new Scanner(System.in);
	
	
	public Table() {
		this.cords = new int[] {(Renderer.WIDTH / 2) - WIDTH / 2, (Renderer.HEIGHT / 2) - HEIGHT / 2};
	}
	
	
	public void update() {
		if (turn == Turn.FLOP) {
			this.cards.add(this.deck.drawRandomCard(this.cords[0] + this.cards.size() * Card.WIDTH + cardHorizontalGap, this.cords[1] + 2));			
			this.cards.add(this.deck.drawRandomCard(this.cords[0] + this.cards.size() * Card.WIDTH + cardHorizontalGap, this.cords[1] + 2));
			this.cards.add(this.deck.drawRandomCard(this.cords[0] + this.cards.size() * Card.WIDTH + cardHorizontalGap, this.cords[1] + 2));
			this.cards.add(this.deck.drawRandomCard(this.cords[0] + this.cards.size() * Card.WIDTH + cardHorizontalGap, this.cords[1] + 2));
			this.cards.add(this.deck.drawRandomCard(this.cords[0] + this.cards.size() * Card.WIDTH + cardHorizontalGap, this.cords[1] + 2));
			
			this.cards.get(0).flip();
			this.cards.get(1).flip();
			this.cards.get(2).flip();
			
			this.turn = Turn.TURN;
		} else if (turn == Turn.TURN) {
			this.cards.add(this.deck.drawTopCard());

			this.turn = Turn.RIVER;
		} else if (turn == Turn.RIVER) {
			this.cards.add(this.deck.drawTopCard());
			
//			Player winner = this.getWinner();
		}
	}
	
	public void getActions() {
		for (Player player : this.players) {
			if (player.isPlaying)
				if (!player.isNPC) {
					player.getInput(this.input);
				} else {
					System.out.println("State machine and stuff...");
			} 
		}
	}
	
	
	/**
	 * Return a display of all the cards on the table.
	 */
	private String displayCards() {
		String display = "";

		for (int i = 0; i < Card.HEIGHT; i++) {
			for (Card card : this.cards) {
				String cardString = card.toString();
				display += cardString.substring(i * (Card.WIDTH + 1), cardString.indexOf("\n", i * (Card.WIDTH + 1)));
				
				for (int j = 0; j < cardHorizontalGap; j++) {
					display += " ";
				}
				
			}
			display += "\n";
		}
		
		return display;
	}
	
	@Override
	public void draw() {
		char[][] asciiArray = new char[HEIGHT][WIDTH];
		
		asciiArray[0][0] ='┏';
		
		for (int i = 1; i < WIDTH - 1; i++) {
			asciiArray[0][i] = '━'; // Top border
			asciiArray[HEIGHT - 1][i] = '━'; // Bottom border
		}
		
		asciiArray[0][WIDTH -1] = '┓';
		asciiArray[HEIGHT - 1][WIDTH -1] = '┛';
		
		for (int i = 1; i < HEIGHT - 1; i++) {
			asciiArray[i][0] = '┃';	
			asciiArray[i][WIDTH - 1] = '┃';	
		}	
		
		Renderer.Render(this.cords, asciiArray);
	}
	
	public String toString() {
		return this.displayCards();
	}
	
	public void addCard(Card card) {
		this.cards.add(card);
	}
	
	public void addPlayer(Player player) {
		this.players.add(player);
		player.setHand(new Card[] {this.deck.drawRandomCard(), this.deck.drawRandomCard()});
	}
	
	public Turn getTurn() {
		return turn;
	}
}
