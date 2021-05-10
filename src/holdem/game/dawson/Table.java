package holdem.game.dawson;

import holdem.engine.dawson.*;
import holdem.entity.dawson.NPC;
import holdem.entity.dawson.Player;
import holdem.graphic.dawson.Drawable;
import holdem.graphic.dawson.EndingScreen;
import holdem.hud.dawson.HBoxContainer;
import holdem.hud.dawson.Text;
import holdem.input.dawson.InputHandler;
import holdem.input.dawson.Option;
import holdem.renderer.dawson.Pixel;
import holdem.renderer.dawson.RenderType;
import holdem.renderer.dawson.Renderer;
import holdem.utils.dawson.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

public class Table extends Drawable {
	public final static int HEIGHT = Card.HEIGHT + 4;
	public final static int WIDTH = (Card.WIDTH * 5) + 4;

	public boolean isOver = false;

	private Deck deck = new Deck();
	private Card[] cards = new Card[5];

	private int cardHorizontalGap = 2;
	private ArrayList<Player> players = new ArrayList<>();
	private Player[] orderedPlayers;

	private Turn turn = Turn.PREFLOP;
	private HBoxContainer hud;

	private Player lastRaisePlayer;

	// Turn related variables \\
	private double lastBet = Blind.BIG.price;
	private double pot = Blind.BIG.price + Blind.SMALL.price;
	//TODO change that.
	/** lastRaise[0] is the last orderedPlayer index to have raise and
	 *  lastRaise[1] is by how much. */
	private int lastToRaise = 0;
	private double betPrice = Blind.BIG.price;

	public Table() {
		super((Renderer.WIDTH / 2) - WIDTH / 2, (Renderer.HEIGHT / 2) - HEIGHT / 2);

	}
	public void start() {
		this.orderPlayersByScreenSection();
		this.initializeBlinds();
	}

	/**
	 * Updates the table according to the current turn.
	 */
	public void update() {
		// Creates the cards
		if (turn == Turn.PREFLOP) {
			for (int i = 0; i < 5; i++) {
				this.cards[i] = this.deck.drawRandomCard(this.cords[0] + i * Card.WIDTH + cardHorizontalGap, this.cords[1] + 2);
			}
			this.orderPlayersByBet();
		}
		// Flip the first three cards
		else if (turn == Turn.FLOP) {
			this.cards[0].flip();
			this.cards[1].flip();
			this.cards[2].flip();
		} else if (turn == Turn.TURN) {
		    this.cards[3].flip();
		} else if (turn == Turn.RIVER) {
			this.cards[4].flip();
		}
		Renderer.draw();
		InputHandler.waitForEnter(Optional.of("Press enter to continue."));
	}


	/**
	 * Prompts the Player OR the AI with the possible TurnOptions.
	 * If playing online, it will listen to the server response to get selected action instead
	 * of getting an AI movement for NPCs.
	 *
	 */
	public void getActions() {
	    System.out.println("New turn");
	    int i = 0;
	    this.lastRaisePlayer = Arrays.stream(this.orderedPlayers).
				filter(p -> p.getBlind() == Blind.BIG).
				findFirst().
				get();
		int stillPlayingAtTheBeginning = (int) Arrays.stream(this.orderedPlayers).filter(p -> p.isPlaying).count();
		while (true) {
	    	int stillPlaying = (int) Arrays.stream(this.orderedPlayers).filter(p -> p.isPlaying).count();
			int hasAllIn = (int) Arrays.stream(this.orderedPlayers).filter(p -> p.hasAllIn && p.isPlaying).count();

		    Player player = orderedPlayers[i];


		    if (player == lastRaisePlayer) {
		    	if (player.hasPlayed) {
		    		break;
				}
			}

			if (stillPlaying == hasAllIn + 1) {
			    if (betPrice == 0 || player.hasPlayed) {
			    	if (lastBet > betPrice) {
						break;
					}
				}
			}

			if (player.isPlaying && !player.hasAllIn) {
			    double before = player.getInfo().getOnTable();
				double answer = player.getInput(this.getTurnOptions(), betPrice);
				player.hasPlayed = true;
				this.lastBet = answer;

				if (answer != 0) {
					pot += lastBet - before;
				}

				if (this.lastBet > betPrice) {
					betPrice = this.lastBet;
					lastRaisePlayer = player;
				} else {
					if (lastRaisePlayer == player) {
						break;
					}
				}
				Renderer.draw();

				InputHandler.waitForEnter(Optional.of("Press enter to continue."));

				player.finishTurn();
				tryFinish();

				if (isOver) {
					finishRound();
					return;
				}

			} else {
				stillPlaying = (int) Arrays.stream(this.orderedPlayers).filter(p -> p.isPlaying).count();
				hasAllIn = (int) Arrays.stream(this.orderedPlayers).filter(p -> p.hasAllIn).count();

				if (stillPlaying == hasAllIn) {
					break;
				}

			}


			if (i < this.orderedPlayers.length - 1) {
				i++;
			} else {
				i = 0;
			}
		}
	}

	/**
	 * Gives you an array of options that the user can take in the current turn.
	 * @return
	 */
	private Option[] getTurnOptions() {

	    ArrayList<Option> options = new ArrayList<>();

	    options.add(Option.FOLD);
	    if (betPrice == 0) {
	        options.add(Option.CHECK);
	        options.add(Option.BET);
		} else {
	        options.add(Option.CALL);
	    	options.add(Option.RAISE);
		}
		options.add(Option.ALLIN);

		Option[] opArray = new Option[options.size()];
	    return options.toArray(opArray);
	}

	/**
	 * Change to the next turn and reset all turn related variables.
	 */
	public void nextTurn() {
		resetTurn();

		if(this.isOver) return;

		if ( this.turn == Turn.PREFLOP) this.turn = Turn.FLOP;
		else if ( this.turn == Turn.FLOP ) this.turn = Turn.TURN;
		else if ( this.turn == Turn.TURN ) this.turn = Turn.RIVER;
		else {
			this.isOver = true;
			finishRound();
		}

		// Reset turn variables
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

	/**
	 * Assigns the Small and Big blind for two random players.
	 */
	private void initializeBlinds() {
	    int randomIndex = new Random().nextInt(this.players.size());

	    Player smallBlindPlayer = this.orderedPlayers[randomIndex];
	    smallBlindPlayer.setBlind(Blind.SMALL);

	    Player leftOfSmallBlindPlayer = this.orderedPlayers[getClosestPlayerIndex(randomIndex)];
	    leftOfSmallBlindPlayer.setBlind(Blind.BIG);

		for (Player p : this.orderedPlayers) {
			p.applyBlind();
		}
	}

	private void orderPlayersByScreenSection() {
		this.orderedPlayers = new Player[players.size()];

		int added = 0;
		for (int i = 0; i < players.size(); i++) {
			int finalI = i;
			Player tPlayer = players.stream()
					.filter(player -> player.getScreenSection().ordinal() == finalI)
					.findAny()
					.orElse(null);
			this.orderedPlayers[added] = tPlayer;
			added++;
		}

	}

	private void orderPlayersByBet() {
		// Assuming that is was already created.
		Player[] tempOrdered = new Player[orderedPlayers.length];
		for (int i = 0; i < orderedPlayers.length; i++) {
			// TODO rewrite this as a loop.
			if (orderedPlayers[i].getBlind() == Blind.BIG)  {
			    int first = getClosestPlayerIndex(i);
			    tempOrdered[0] = orderedPlayers[first];
			    int second = getClosestPlayerIndex(first);
				tempOrdered[1] = orderedPlayers[second];
				int third = getClosestPlayerIndex(second);
				tempOrdered[2] = orderedPlayers[third];
				int fourth = getClosestPlayerIndex(third);
				tempOrdered[3] = orderedPlayers[fourth];
			}
		}
		this.orderedPlayers = tempOrdered;
	}

	/**
	 * Shifts the Big and Small blind to the right.
	 */
	private void moveBlinds() {
	    if (!moveBlind(Blind.BIG)) {
	    	moveBlind(Blind.SMALL);
		}
		for (Player p : this.orderedPlayers) {
			p.applyBlind();
		}
	}

	/**
	 * Moves a given blind to next player that didn't fold.
	 * If there is only two players and the blinds were switched, it will return true.
	 * Otherwise, it will return false.
	 * @param blind
	 * @return
	 */
	private boolean moveBlind(Blind blind) {
		int oldBigBlindIndex = -1;
		boolean switched = false; // When there are only two players and they switch blind.
		for (int i = 0; i < orderedPlayers.length; i++) {
			if (orderedPlayers[i].getBlind() == blind) {
				oldBigBlindIndex = i;
				orderedPlayers[i].setBlind(Blind.NONE);
			}
		}

		int nextPlayerIndex = getClosestPlayerIndex(oldBigBlindIndex);
		while (true) {
		    Player nextPlayer = orderedPlayers[nextPlayerIndex];
		    if (nextPlayer.isPlaying) {
		        if (nextPlayer.getBlind() != Blind.NONE) {
		            switch (blind) {
						case BIG:
							orderedPlayers[oldBigBlindIndex].setBlind(Blind.SMALL);
							switched = true;
							break;
						case SMALL:
							orderedPlayers[oldBigBlindIndex].setBlind(Blind.BIG);
							switched = true;
							break;
					}
				}
		    	nextPlayer.setBlind(blind);
		        break;
			}
		    nextPlayerIndex = getClosestPlayerIndex(nextPlayerIndex);
		}

		return switched;

	}

	/**
	 * Return the index of the player on the left on the given player.
	 * @param playerIndex
	 * @return
	 */
	private int getClosestPlayerIndex(int playerIndex) {
	    if (playerIndex + 1 >= orderedPlayers.length) {
	    	return 0;
		} else {
	    	return playerIndex + 1;
		}
	}

	private void finishRound() {

		PokerResponse pokerResponse = new PokerResponse(PokerHands.HIGH_CARD, 0); // Lowest possible hand.
		int playerIndex = -1; // Just so java won't complain about a possibly non initialized variable.


        if (this.turn == Turn.RIVER) {
			for (Player player : this.orderedPlayers) {
				if (player instanceof NPC && player.isPlaying) {
					player.flipCards();
				}
			}

			Renderer.draw();
			InputHandler.waitForEnter(Optional.of("Press enter to continue."));
		}

		for (int i = 0; i < this.orderedPlayers.length; i++) {
			if (this.orderedPlayers[i].isPlaying) {
				PokerResponse combination = PokerEngine.getCombo(
						this.getCards(),
						this.orderedPlayers[i].getHand());

				if (combination.combination.ordinal() > pokerResponse.combination.ordinal()) {
					pokerResponse = combination;
					playerIndex = i;
				} else if (combination.combination == pokerResponse.combination) {
					if (combination.points > pokerResponse.points) {
						pokerResponse = combination;
						playerIndex = i;
					} else if (combination.points == pokerResponse.points) {
					    if (orderedPlayers[i].getHandPoints() > orderedPlayers[playerIndex].getHandPoints()) {
							playerIndex = i;
						} else {
					    	System.out.println("possible draw.");
						}
					}
				}
			}
		}

		Player player = orderedPlayers[playerIndex];
		player.getInfo().incrementMoney(pot);

		int playingCount = (int) Arrays.stream(this.orderedPlayers).filter(p -> p.isPlaying).count();
		EndingScreen endingScreen;
		if (playingCount == 1) {
		    endingScreen = new EndingScreen(player.getInfo().getName());
		} else {
			endingScreen = new EndingScreen(
					player.getInfo().getName(),
					pokerResponse);
		}


		Renderer.addDrawable(endingScreen);
		Renderer.draw();
        Renderer.removeDrawable(endingScreen);
		isOver = true;
	}

	public void restart(){
		resetTurn();

		this.isOver = false;
		this.turn = Turn.PREFLOP;
		this.lastBet = Blind.BIG.price;
		this.orderPlayersByBet();
		this.lastToRaise = -1;
		this.pot = Blind.BIG.price + Blind.SMALL.price;
		this.betPrice = Blind.BIG.price;

		this.deck = new Deck();

		for (Player player : this.orderedPlayers ) {
			player.hasAllIn = false;
			if (player.getInfo().canSpend(Blind.BIG.price)) {
				player.isPlaying = true;
				player.setHand(deck.drawRandomCard(), deck.drawRandomCard());
			} else {
				System.out.println("here");
				player.isPlaying = false;
				if (player.getHand()[0].isUp()) {
					player.flipCards();
				}
			}
			player.finishTurn(); // Reset to white or red cards.
		}
		if (players.stream().filter(p -> !p.isPlaying).count() == this.players.size() -1 ) {
		    System.out.println("Thank you for playing! Everyone else lost. :) You are too good.");
		    System.exit(0);
		}

		this.moveBlinds();

	}

	private void resetTurn() {
		this.lastBet = 0;
		this.betPrice = 0;
		this.players.stream()
				.forEach(p ->{
					p.hasPlayed = false;
					p.getInfo().setOnTable(0);
				});
	}

	private void tryFinish() {
		int foldCount = 0;
		for (Player player : this.players) {
			if (!player.isPlaying) foldCount++;
		}

		if (foldCount == 3) {
			this.isOver = true;
		}
	}

	/**
	 * Add a new player or NPC to the table and gives them a new hand using the
	 * table deck.
	 * @param player
	 */
	public void addPlayer(Player player) {
		this.players.add(player);
		Renderer.addDrawable(player);
		player.setHand(new Card[] {this.deck.drawRandomCard(), this.deck.drawRandomCard()});
	}

	@Override
	public void draw(RenderType rt) {
	    if (rt != RenderType.NORMAL) return;
		Pixel[][] asciiArray = new Pixel[HEIGHT][WIDTH];
		Color color = Color.BLUE;

		asciiArray[0][0] = new Pixel('┏', color);
		asciiArray[HEIGHT - 1][0] = new Pixel('┗', color);

		for (int i = 1; i < WIDTH - 1; i++) {
			asciiArray[0][i] = new Pixel('━', color); // Top border
			asciiArray[HEIGHT - 1][i] = new Pixel('━', color); // Bottom border
		}
		
		asciiArray[0][WIDTH -1] = new Pixel('┓', color);
		asciiArray[HEIGHT - 1][WIDTH -1] = new Pixel('┛', color);
		
		for (int i = 1; i < HEIGHT - 1; i++) {
			asciiArray[i][0] = new Pixel('┃', color);
			asciiArray[i][WIDTH - 1] = new Pixel('┃', color);
		}

		for (int y = 1; y < HEIGHT - 1; y++) {
			for (int x = 1; x < WIDTH - 1; x++) {
				asciiArray[y][x] = new Pixel(' ', Color.BLUE);
			}
		}
		
		Renderer.Render(this.cords, asciiArray);

		hud = new HBoxContainer(
				this.cords[0] + 8,
				this.cords[1] + HEIGHT - 2,
				this.getWidth() - 2,
				1,
				new Text("Bet price: $" + betPrice, color),
				new Text("Prize: $" + pot, color)
		);



		this.hud.draw(rt);

	}

	@Override
	public int getWidth() {
	    return WIDTH;
	}

	@Override
	public int getHeight() {
	    return HEIGHT;
	}

	public String toString() {
		return this.displayCards();
	}

	public Card[] getCards() {
		return cards;
	}
}
