package holdem.game.dawson;

import java.util.ArrayList;
import java.util.Random;

import holdem.engine.dawson.Card;
import holdem.engine.dawson.Card.Suit;
import holdem.renderer.dawson.Renderer;
import holdem.utils.dawson.Color;

public class Deck {
	private ArrayList<Card> cards = new ArrayList<Card>();
	private Random rand = new Random();
	
	public Deck() {
		for (int i = 0; i < 52; i++) {
			Suit suit;
			int value;
			if (i < 13) {
				suit = Card.Suit.Clubs;
				value = i;
			} else if (i < 26) {
				suit = Card.Suit.Diamonds;
				value = i - 13;
			} else if (i < 39) {
				suit = Card.Suit.Spades;
				value = i - 26;
			} else {
				suit = Card.Suit.Hearts;
				value = i - 39;
			}
				
			cards.add(new Card(0, 0, suit, Integer.toString(value + 1)));
		}
	}
	
	public Card drawRandomCard() {
		int index = rand.nextInt(this.cards.size());
		Card card = this.cards.get(index);
		this.cards.remove(index);
		return card;
	}

	public void putInDeck(Card ... cards) {
		for (Card card : cards) {
			if (card.isUp()) {
				card.flip();
			}
			card.setColor(Color.WHITE);
			this.cards.add(card);
		}
	}
	
	public Card drawRandomCard(int x, int y) {		
		int index = rand.nextInt(this.cards.size());
		Card card = this.cards.get(index);
		card.setPos(x, y);
		Renderer.addDrawable(card);
		this.cards.remove(index);
		return card;
	}
	
}
