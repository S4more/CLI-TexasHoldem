package holdem.entity.dawson;

import java.util.Arrays;

import holdem.engine.dawson.Blind;
import holdem.engine.dawson.Card;
import holdem.graphic.dawson.Drawable;
import holdem.hud.dawson.Align;
import holdem.hud.dawson.Dialogue;
import holdem.hud.dawson.Text;
import holdem.hud.dawson.VBoxContainer;
import holdem.input.dawson.Option;
import holdem.renderer.dawson.RenderType;
import holdem.renderer.dawson.Renderer;
import holdem.renderer.dawson.ScreenSection;
import holdem.utils.dawson.Color;

public abstract class Player extends Drawable {

	public boolean isPlaying = true;
	public boolean hasAllIn = false;
	public boolean hasPlayed = false;

	protected boolean isTurn = false;
	protected static int HEIGHT = Card.HEIGHT + 1;
	protected static int WIDTH = Renderer.WIDTH / 2;

	protected Card hand[] = new Card[2];

	protected PlayerInfo info;
	protected Dialogue dialogue;
	protected String message = "...";

	protected ScreenSection screenSection;

	// GRAPHICAL
	protected VBoxContainer hud;
		// Anchors are the x and y of the top left card.
	protected int yAnchor = 0;
	protected int xAnchor = this.cords[0] + 16;

	public Player(ScreenSection screenSection, String name) {
		super(0, 0); // TODO this is non sense.
		this.info = new PlayerInfo(name, 200.00);
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
		this.updateHUD();
		this.dialogue = new Dialogue(this.cords[0], this.cords[1], 5, 3, new Text("Test", Color.RED));

	}

	/**
	 * Updates the HUD with the newest info from the player.
	 */
	private void updateHUD() {
		int deltaX;
		int hudCardSeparator = 1;
		// Creates the HUD.
		if (screenSection == ScreenSection.BOTTOMLEFT || screenSection == ScreenSection.TOPLEFT) {
			deltaX = this.xAnchor - this.cords[0];

			Text stats[] = new Text[info.asStringArray().length];
			String formattedInfo[] = Align.alignRight(info.asStringArray(), deltaX - hudCardSeparator, Text.SPECIAL_SEPARATOR);
			for (int i = 0; i < formattedInfo.length; i++) {
				stats[i] = new Text(formattedInfo[i], Color.WHITE);
			}

			this.hud = new VBoxContainer(
					this.cords[0],
					this.yAnchor + 1,
					deltaX,
					5,
                    info.asTextArray(Color.WHITE)
			);

			deltaX = this.xAnchor + (Card.WIDTH - 4) * (this.hand.length - 1) + Card.WIDTH;
			this.dialogue = new Dialogue(deltaX, yAnchor, message.length() + 2, 3, new Text(message, Color.RED));
		} else {
			deltaX = this.xAnchor + (Card.WIDTH -4) * (this.hand.length - 1) + Card.WIDTH;
			this.hud = new VBoxContainer(
					deltaX + hudCardSeparator,
					this.yAnchor + 1,
					Renderer.WIDTH - deltaX,
					5,
					info.asTextArray(Color.WHITE)
			);
			this.dialogue = new Dialogue(this.xAnchor - message.length() - 2, this.yAnchor, message.length() + 2, 3, new Text(message, Color.RED));

		}

		if (this.isTurn) {
			this.dialogue.activate();
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

	@Override
	public void draw(RenderType rt) {
		this.updateHUD();
		if (rt != RenderType.NORMAL) return;
		// +1 is a magic number?
		char[][] asciiArray = new char[Card.HEIGHT + 1][Renderer.WIDTH / 2];
		char identifier = '.';

		for (int y = 0; y < asciiArray.length; y++) {
			for (int x = 0; x < asciiArray[y].length; x++) {
				asciiArray[y][x] = identifier;
			}
		}

		Arrays.asList(this.hand).forEach(c -> c.draw(rt));
		this.hud.draw(rt);
		this.dialogue.draw(rt);
		//Renderer.Render(this.cords, asciiArray);
	}

	@Override
	public int getWidth() {
	    return WIDTH;
	}

	@Override
	public int getHeight() {
	    return HEIGHT;
	}

	/**
	 * Shortcut for info.setBlind(blind);
	 * @param blind
	 */
	public void setBlind(Blind blind) {
		this.info.setBlind(blind);

		System.out.println("" + this.info.getBlind() + " for " + this.info.getName());

		// If the player can't spend the money, zero his balance.
	}

	public void applyBlind() {
	    System.out.println("called.");
		if ( this.info.canSpend(this.getBlind().price) ) {
			this.info.incrementMoney(-this.getBlind().price);
			this.info.incrementOnTable(this.getBlind().price);
		} else {
			this.info.incrementMoney(-this.info.getMoney());
			this.info.setOnTable(0);
		}

	}

	/**
	 * Shorcut for ingo.getBlind();
	 * @return
	 */
	public Blind getBlind(){
		return this.info.getBlind();
	}

	public PlayerInfo getInfo() {
		return this.info;
	}

	public ScreenSection getScreenSection() {
		return screenSection;
	}

	public void finishTurn() {
		this.isTurn = false;
		this.message = "...";

		if (!this.hasAllIn && this.info.getMoney() == 0) {
			this.isPlaying = false;
		}

		for (int i = 0; i < this.hand.length; i++) {
			if (this.isPlaying) {
			    if (this.hasAllIn) {
			    	this.hand[i].setColor(Color.PURPLE);
				} else {
					this.hand[i].setColor(Color.WHITE);
				}
			} else {
				this.hand[i].setColor(Color.RED);
			}
		}
	}

	public void flipCards() {
		for (Card card : this.hand) {
			card.flip();
		}
	}

	public boolean isTurn() {
		return this.isTurn;
	}

	public Card[] getHand() {
		return hand;
	}

	public int getHandPoints() {
		int points = 0;
		for (int i = 0; i < getHand().length; i++ ){
			points += getHand()[i].getValue();
		}
		return points;
	}


	/**
	 * Handle action of folding.
	 * @return the spent money.
	 */
	protected double handleFold() {
		this.isPlaying = false;
		this.message = "Just folded.";
		return 0;
	}

	/**
	 * Handle action of checking
	 * @return the spent money.
	 */
	protected double handleCheck() {
		this.message = "Just checked.";
		return 0;
	}

	/**
	 * Handle action of betting.
	 * @param money the money to bet.
	 * @return

	 */
	protected double handleBet(double money) throws Exception {
		if (this.info.getMoney() > money) {
			this.info.incrementMoney(-money);
			this.message = "Just bet $" + money;
			this.info.incrementOnTable(money);
			return money;
		} else {
			throw new Exception();
		}
	}

	protected double handleRaise(double amount) throws Exception {
		double onTable = this.info.getOnTable();
		if (this.info.canSpend(amount - onTable) ) {
			this.message = "raised to $" + amount;
			this.info.incrementMoney(- (amount - onTable) );
			this.info.incrementOnTable(amount - onTable);
			return amount;
		} else {
			throw new Exception();
		}
	}

	protected double handleCall(double betPrice) throws Exception {
		double needToPay = betPrice - this.info.getOnTable();
		if (needToPay >= this.info.getMoney()) { throw new Exception(); };
		this.message = "Just called $" + betPrice;
		this.info.incrementMoney(-needToPay);
		this.info.incrementOnTable(needToPay);
		return betPrice;
	}

	protected double handleAllIn() throws Exception {
		double money = this.info.getMoney();
		double ontable = this.info.getOnTable();
		if (money > 0) {
			this.info.incrementOnTable(money);
			this.info.incrementMoney(-money);
			this.message = "All-in with $" + money;
			this.hasAllIn = true;
			return money + ontable;
		} else {
			throw new Exception();
		}
	}

	protected void activatePlayer() {
		for (int i = 0; i < this.hand.length; i++) {
			this.hand[i].setColor(Color.YELLOW);
		}
	}

	public abstract void setHand(Card ... hand);
	public abstract double getInput(Option[] options, double lastBet);
}
