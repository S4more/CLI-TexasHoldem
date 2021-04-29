package holdem.game.dawson;

import holdem.engine.dawson.Turn;
import holdem.entity.dawson.Player;
import holdem.hud.dawson.Align;
import holdem.hud.dawson.VBoxContainer;
import holdem.renderer.dawson.Renderer;
import holdem.renderer.dawson.ScreenSection;

public class Game {

	static Player localPlayer;
	static Table table;
	// TODO -> WHY STRINGS! NUMBERS!
	// TODO -> There is no need to regenerate the whole stuff every time.
	
	

	public static void main(String[] args) {
		
//		VBoxContainer box = new VBoxContainer(0, 0, 16, 10, new String[] {"This is a test and this is just a sentence.", "This is another sentence", "xablau!"});
//		Renderer.addDrawable(box);
			
		table = new Table();
		localPlayer = new Player(false, ScreenSection.BOTTOMLEFT);
		Player npc = new Player(true, ScreenSection.TOPRIGHT);
		Player npc2 = new Player(true, ScreenSection.BOTTOMRIGHT);
		Player npc3 = new Player(true, ScreenSection.TOPLEFT);
		
		table.addPlayer(localPlayer);
		table.addPlayer(npc);
		table.addPlayer(npc2);
		table.addPlayer(npc3);
		
		Renderer.addDrawable(table);
		Renderer.addDrawable(localPlayer);
		Renderer.addDrawable(npc);
		Renderer.addDrawable(npc2);
		Renderer.addDrawable(npc3);
	

		//Text text = new Text(1, 1, 10, 5, "NPC 1. Money:");
		//Renderer.addDrawable(text);

		update();
		draw();
//		
//		update();
//		draw();
//
//		update();
//		draw();	
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
