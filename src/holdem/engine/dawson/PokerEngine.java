package holdem.engine.dawson;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import holdem.engine.dawson.Card.Suit;

public class PokerEngine {

	public static PokerResponse getCombo(Card[] table, Card[] hand) {
		Card[] a = new Card[table.length + hand.length];
		for (int i = 0; i < table.length; i++) {
			a[i] = table[i];
		}

		for (int i = 0; i < hand.length; i++) {
			a[i + table.length] = hand[i];
		}

		return getCombo(a);

    }

	public static PokerResponse getCombo(Card ... cards) {

		Optional<PokerResponse> pair = checkPair(cards);
		Optional<PokerResponse> flush = checkFlush(cards);
		Optional<PokerResponse> straight = checkStraight(cards);

		// maybe comparing the poker hands will be better. Using enums, I mean
		
		// Pair and straight are opposites. If there is one pair, it is safe to assume that the best possible combination
		// is a flush or one of the parity combo.
		
		if (pair.isPresent() && flush.isEmpty()) return pair.get();

		// With a flush, we can get a royal, a straight flush or a flush.
		
		if (flush.isPresent()) {
			
			if (straight.isPresent()) {

				Optional<PokerHands> royal = checkRoyal(cards);
				if (royal.isPresent()) {
					return new PokerResponse(PokerHands.ROYAL_FLUSH, 14);
				}
			
				return straight.get();
			}	
			
			return flush.get();
			
		}
		
		// TODO optimize here. I'm checking for straight twice.
		if (straight.isPresent()) {
			return straight.get();
		}


		return highCard(Arrays.copyOfRange(cards, 5, 7));

	}

	private static Optional<PokerHands> checkRoyal(Card[] cards) {
		if (Arrays.asList(cards).stream().anyMatch(e -> e.getValue() == 14)) {
			return Optional.of(PokerHands.ROYAL_FLUSH);
		}
	
		return Optional.empty();
		
		
	}

	private static Optional<PokerResponse> checkFlush(Card[] cards) {
		for (Suit suit : Suit.values()) {
			Supplier<Stream<Card>> cardStream = () -> Arrays.asList(cards).stream();

			Stream<Card> matches = cardStream.get().filter(e -> e.getSuit() == suit);

			if (matches.count() >= 5) {
				return Optional.of(
						new PokerResponse(
								PokerHands.FLUSH,
                                // Get the value of the highest card of the flush.
                                cardStream.get().max(Comparator.comparing(Card::getValue)).get().getValue())
						);
			}

		}

		return Optional.empty();
				
	}
	
	private static Optional<PokerResponse> checkStraight(Card[] cards) {

		List<Integer> sortedList = Arrays.asList(cards).stream()
			.map(e -> e.getValue())
			.sorted()
			.collect(Collectors.toList());
		
		for (int i = 1; i < sortedList.size(); i++) {
			System.out.println(sortedList.get(i - 1) + ", " + (sortedList.get(i) -1));
			if (sortedList.get(i - 1) != sortedList.get(i) -1){
				return Optional.empty();
			}
		}
		

		return Optional.of(new PokerResponse(PokerHands.STRAIGHT, sortedList.get(sortedList.size())));
		
	}

	private static PokerResponse highCard(Card[] hand) {
		Card highestCard = Arrays.stream(hand).max(Comparator.comparing(Card::getValue)).get();
		return new PokerResponse(PokerHands.HIGH_CARD, highestCard.getValue());
	}
	
	/**
	 * Check for parity between an array of poker cards.
	 * 
	 * @param cards An array of cards that will be compared.
	 * @return Returns an the Poker Hand if it exists and is pair based, otherwise returns
	 * an empty Optional.
	 */
	private static Optional<PokerResponse> checkPair(Card[] cards) {
		
		
		HashMap<Integer, Integer> occurrences = new HashMap<Integer, Integer>();
		
		for (int pivot = 0; pivot < cards.length; pivot++) {
			// Check if the card was counted already.
			if (occurrences.containsKey(cards[pivot].getValue())) {
				continue;
			}
			for (int i = 0; i < cards.length; i++) {
				if (cards[pivot].equals(cards[i])) { // If they have the same value,	
					occurrences.computeIfPresent(cards[pivot].getValue(), (key, value) -> value + 1);
					occurrences.putIfAbsent(cards[pivot].getValue(), 1);
				}
			}
		}

		System.out.println(occurrences.toString());
			
		Set<Entry<Integer, Integer>> occurrenceSet= occurrences.entrySet();

		// Checks for four of a kind
		Optional<Entry<Integer, Integer>> fourOfAKind = occurrenceSet.stream().filter(e -> e.getValue() == 4).findFirst();
		if (fourOfAKind.isPresent()) {
			return Optional.of(new PokerResponse(PokerHands.FOUR_OF_A_KIND, fourOfAKind.get().getKey()));
		}

		int differentPairs = (int) occurrenceSet.stream().filter(e -> e.getValue() > 1).count();

		if (differentPairs == 2) {
			Optional<Entry<Integer, Integer>> matches = occurrenceSet.stream().filter(e -> e.getValue() == 3).findFirst();
			if (matches.isPresent()) return Optional.of(new PokerResponse(PokerHands.FULL_HOUSE, matches.get().getKey()));
			return Optional.of( new PokerResponse(
					PokerHands.TWO_PAIR,
					occurrenceSet.stream().filter(e -> e.getValue() == 2).findFirst().get().getKey()
					)
			);
		}
		
		Optional<Entry<Integer, Integer>> optionalThree = occurrenceSet.stream().filter(e -> e.getValue() == 3).findFirst();
		if (optionalThree.isPresent()) {
			return Optional.of(new PokerResponse(PokerHands.THREE_OF_A_KIND, optionalThree.get().getKey()));
		}



		Optional<Entry<Integer, Integer>> optionalOne = occurrenceSet.stream().filter(e -> e.getValue() == 2).findFirst();
		if (optionalOne.isPresent()) return Optional.of(new PokerResponse(PokerHands.ONE_PAIR, optionalOne.get().getKey()));
			
		return Optional.empty();
		
		
		
	}
}
