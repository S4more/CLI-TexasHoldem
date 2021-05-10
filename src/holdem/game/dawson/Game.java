package holdem.game.dawson;

import holdem.entity.dawson.Human;
import holdem.entity.dawson.NPC;
import holdem.entity.dawson.Player;
import holdem.graphic.dawson.MenuScreen;
import holdem.input.dawson.InputHandler;
import holdem.input.dawson.InputResponse;
import holdem.renderer.dawson.Renderer;
import holdem.renderer.dawson.ScreenSection;
import holdem.utils.dawson.Color;

import java.util.Optional;
import java.util.Scanner;

public class Game {

	static Player localPlayer;
	static Table table;
	// TODO -> WHY STRINGS! NUMBERS!
	// TODO -> There is no need to regenerate the whole stuff every time.

	public static void main(String[] args) {

		String name = getUserName();

		table = new Table();
		localPlayer = new Human(ScreenSection.BOTTOMLEFT, name);
		Player npc = new NPC(ScreenSection.TOPRIGHT, "Louis");
		Player npc2 = new NPC(ScreenSection.BOTTOMRIGHT, "Diego");
		Player npc3 = new NPC(ScreenSection.TOPLEFT, "Leko");

		table.addPlayer(localPlayer);
		table.addPlayer(npc);
		table.addPlayer(npc2);
		table.addPlayer(npc3);

		MenuScreen menuScreen = new MenuScreen();

		Renderer.addDrawable(menuScreen);
		for (int i = 0; i < menuScreen.frames(); i++) {
			Renderer.draw();
			InputHandler.waitForEnter(Optional.of("Press enter to continue."));
		}
		Renderer.removeDrawable(menuScreen);

		Renderer.addDrawable(table);


		table.start();
		update();

	}

	/**
	 * Prompts the user for a valid username.
	 * @return the given username.
	 */
	private static String getUserName() {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.print(Color.YELLOW + "Please write your username: "+Color.RESET);
			String name = scanner.nextLine();
			if (0 < name.length() && name.length() < 10) {
				return name;
			} else {
				InputHandler.printError("Please write a number between 1 and 10 characters.");
			}
		}

	}

	/**
	 * Starts the game loop and keeps updating the game until
	 * the user decides to quit or there are no more players
     * with money to play.
	 */
	public static void update() {
		while (true) {

			if (!table.isOver) {
				table.update();
				table.getActions();
				table.nextTurn();
			} else {
				InputHandler.printError("Do you want to keep playing? [yes/NO]: ");
				String ans = new Scanner(System.in).nextLine();
				if (ans.toLowerCase().equals("yes")) {
					table.restart();
				} else {
					System.out.println("Thank you for playing!");
					break;
				}
			}

		}
	}
}
