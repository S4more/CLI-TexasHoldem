package gui.game.dawson;

import gui.game.dawson.Table.Turn;

public class Main {
	static Player localPlayer;
	static Table table;
	// TODO -> WHY STRINGS! NUMBERS!
	// TODO -> There is no need to regenerate the whole stuff every time.
	
	

	public static void main(String[] args) {
		Card one = new Card(Card.Suit.Hearts ,"A");
		Card two = new Card(Card.Suit.Hearts ,"2");
		Card three = new Card(Card.Suit.Hearts ,"3");
		Card four = new Card(Card.Suit.Hearts ,"4");
		Card five = new Card(Card.Suit.Hearts ,"5");
		Card six = new Card(Card.Suit.Hearts ,"6");
		Card seven = new Card(Card.Suit.Hearts ,"7");
		Card eight = new Card(Card.Suit.Hearts ,"8");
		Card nine = new Card(Card.Suit.Hearts ,"9");
		Card ten = new Card(Card.Suit.Hearts ,"10");
		Card j = new Card(Card.Suit.Hearts ,"Q");
		Card q = new Card(Card.Suit.Hearts ,"J");
		Card k = new Card(Card.Suit.Hearts ,"K");

			
		table = new Table();
		localPlayer = new Player(false, new int[] {0, 4 * Renderer.HEIGHT / 5});
		Player npc = new Player(true, new int[] {0, 0});
		
		table.addPlayer(localPlayer);
		
		Renderer.addDrawable(table);
		Renderer.addDrawable(localPlayer);
		Renderer.addDrawable(npc);
	
		//System.out.println(PokerEngine.getCombo(new Card[]{ten, j, q, k, one}));
//		PokerEngine.getPoints();

		update();
		draw();
		
		update();
		draw();

		update();
		draw();	
	}
	
	public static void update() {
		if (table.getTurn() != Turn.FLOP) {
			table.getActions();
		}
		
		table.update();
	}
	
	public static void draw() {
		Renderer.draw();
	}

}
