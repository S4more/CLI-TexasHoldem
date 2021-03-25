package gui.game.dawson;

import java.io.IOException;
import java.util.ArrayList;

public class Renderer {

	public static final int WIDTH = 80;
	public static final int HEIGHT = 25;

	private static ArrayList<Drawable> renderQueue = new ArrayList<Drawable>();	

	private static char[][] buffer = new char[HEIGHT][WIDTH];

	public static void draw() {
		clrscr();
		
		renderQueue.forEach(e -> e.draw());
		

		for (int y = 0; y < HEIGHT; y++) {
			System.out.println(new String(buffer[y]));
		}
		
	}
	
	public static void addDrawable(Drawable obj) {
		renderQueue.add(obj);
	}
	
	private static void clrscr(){
	    //Clears Screen in java
		
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				buffer[y][x] = '.';
			}
		}
		
		
	    try {
	        if (System.getProperty("os.name").contains("Windows"))
	            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
	        else
	            Runtime.getRuntime().exec("clear");
	    } catch (IOException | InterruptedException ex) {}
	}

	public static void Render(int[] cords, char[][] drawableArray) {
		
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				
				if (y < cords[1] || x < cords[0]) continue;
				
				int dx = x - cords[0];
				int dy = y - cords[1];

				if (dy >= drawableArray.length || dx >= drawableArray[0].length) break;
				
				char c = drawableArray[dy][dx];
				
				if (c == '\0') {
					c = '.';
				}
				
				Renderer.buffer[y][x] = c;
				
			}
		}
	}
	

}
