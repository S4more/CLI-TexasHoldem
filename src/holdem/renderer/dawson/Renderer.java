package holdem.renderer.dawson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import holdem.graphic.dawson.Drawable;
import holdem.utils.dawson.Color;

public class Renderer {

	public static final int WIDTH = 80;
	public static final int HEIGHT = (int) (WIDTH / 3.2);
	private static final Color BGCOLOR = Color.BACKGROUND_BLACK;

	private static ArrayList<Drawable> renderQueue = new ArrayList<Drawable>();


	private static Pixel[][] buffer = new Pixel[HEIGHT][WIDTH];

	public static void draw() {
		clrscr();
		
		renderQueue.forEach(e -> e.draw());

		// Print buffer
		for (int y = 0; y < HEIGHT; y++) {
			// Trick to use a reference to the string instead of the value itself.
			// I don't like java sometimes...
			String output[] = new String[]{""};

			Arrays.asList(buffer[y]).stream()
					.filter(x -> x != null)
					.forEach(x -> output[0]+= BGCOLOR.toString() + x);

			System.out.println(output[0]);
		}

	}
	
	public static void addDrawable(Drawable obj) {
		renderQueue.add(obj);
	}
	
	private static void clrscr(){
	    //Clears Screen in java
		
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				buffer[y][x] = new Pixel(' ', Color.BLACK);
			}
		}
		
		
	    try {
	        if (System.getProperty("os.name").contains("Windows"))
	            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
	        else
	            Runtime.getRuntime().exec("clear");
	    } catch (IOException | InterruptedException ex) {}
	}

	public static void Render(int[] cords, Pixel[][] drawableArray) {
		
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				
				if (y < cords[1] || x < cords[0]) continue;
				
				
				int dx = x - cords[0];
				int dy = y - cords[1];

				if (dy >= drawableArray.length || dx >= drawableArray[0].length) break;
				
				Pixel c = drawableArray[dy][dx];

				if (c == null) {
					continue;
				}
				
				Renderer.buffer[y][x] = c;
				
			}
		}
	}


}