package holdem.engine.dawson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import holdem.engine.dawson.Card.Suit;

public class PokerEngine {

	public static PokerHands getCombo(Card[] hand) {

		Optional<PokerHands> pair = checkPair(hand);
		Optional<PokerHands> flush = checkFlush(hand);
		Optional<PokerHands> straight = checkStraight(hand);

		// maybe comparing the poker hands will be better. Using enums, I mean
		
		// Pair and straight are opposites. If there is one pair, it is safe to assume that the best possible combination
		// is a flush or one of the parity combo.
		
		if (pair.isPresent() && flush.isEmpty()) return pair.get();

		// With a flush, we can get a royal, a straight flush or a flush.
		
		if (flush.isPresent()) {
			
			if (straight.isPresent()) {

				Optional<PokerHands> royal = checkRoyal(hand);
				if (royal.isPresent()) {
					return PokerHands.ROYAL_FLUSH;
				}
			
				return PokerHands.STRAIGHT_FLUSH;	
			}	
			
			return PokerHands.FLUSH;
			
		}
		
		// TODO optimize here. I'm checking for straight twice.
		if (straight.isPresent()) {
			return PokerHands.STRAIGHT;
		}
		
		
		return PokerHands.HIGH_CARD;
		
	}

	private static Optional<PokerHands> checkRoyal(Card[] cards) {
		if (Arrays.asList(cards).stream().anyMatch(e -> e.getValue() == 14)) {
			return Optional.of(PokerHands.ROYAL_FLUSH);
		}
	
		return Optional.empty();
		
		
	}

	private static Optional<PokerHands> checkFlush(Card[] cards) {
		Suit suit = cards[0].getSuit();
		
		if (Arrays.asList(cards).stream()
			.allMatch(e -> e.getSuit() == suit)) {
			return Optional.of(PokerHands.FLUSH);
		}
		
		return Optional.empty();
				
	}
	
	private static Optional<PokerHands> checkStraight(Card[] cards) {

		List<Integer> sortedList = Arrays.asList(cards).stream()
			.map(e -> e.getValue())
			.sorted()
			.collect(Collectors.toList());
		
		for (int i = 1; i < sortedList.size(); i++) {
			if (sortedList.get(i - 1) != sortedList.get(i) -1) return Optional.empty();
		}
		

		return Optional.of(PokerHands.STRAIGHT);
		
	}
	
	
	// TODO set it back to private
	/**
	 * 
	 * Check for parity between an array of poker cards.
	 * 
	 * @param cards An array of cards that will be compared.
	 * @return Returns an the Poker Hand if it exists and is pair based, otherwise returns
	 * an empty Optional.
	 */
	private static Optional<PokerHands> checkPair(Card[] cards) {
		
		
		HashMap<Integer, Integer> occurences = new HashMap<Integer, Integer>();
		
		for (int pivot = 0; pivot < cards.length; pivot++) {
			// Check if the card was counted already.
			if (occurences.containsKey(cards[pivot].getValue())) {
				continue;
			}
			for (int i = 0; i < cards.length; i++) {
				if (cards[pivot].equals(cards[i])) { // If they have the same value,	
					occurences.computeIfPresent(cards[pivot].getValue(), (key, value) -> value + 1);
					occurences.putIfAbsent(cards[pivot].getValue(), 1);
				}
			}
		}	
			
		Set<Entry<Integer, Integer>> occurencySet= occurences.entrySet();
		
		//occurences.forEach((k, v) -> System.out.println("key: " + k + " value: " +  v));
		
		
		// Checks for four of a kind
		if (occurencySet.stream().anyMatch(e -> e.getValue() == 4)) return Optional.of(PokerHands.FOUR_OF_A_KIND);
		
		int differentPairs = (int) occurencySet.stream().filter(e -> e.getValue() > 1).count();

		if (differentPairs == 2) {
			if (occurencySet.stream().anyMatch(e -> e.getValue() == 3)) return Optional.of(PokerHands.FULL_HOUSE);
			return Optional.of(PokerHands.TWO_PAIR);
		}
		
		if (occurencySet.stream().anyMatch(e -> e.getValue() == 3)) return Optional.of(PokerHands.THREE_OF_A_KIND);
		if (differentPairs == 1) return Optional.of(PokerHands.ONE_PAIR);
			
		return Optional.empty();
		
		
		
	}
		
}
