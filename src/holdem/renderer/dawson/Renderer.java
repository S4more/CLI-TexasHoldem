package holdem.renderer.dawson;

import java.util.ArrayList;
import java.util.Arrays;

import holdem.graphic.dawson.Drawable;
import holdem.utils.dawson.Color;

public class Renderer {

	public static final int WIDTH = 80;
	public static final int HEIGHT = (int) (WIDTH / 3.2);
	public static final int OPTION_HEIGHT = 4;
	private static final Color BGCOLOR = Color.BACKGROUND_BLACK;

	private static ArrayList<Drawable> renderQueue = new ArrayList<Drawable>();

	/**
	 * The option queue only draws once and them empties itself.
	 */
	private static ArrayList<Drawable> optionQueue = new ArrayList<>();


	private static Pixel[][] buffer = new Pixel[HEIGHT][WIDTH];
	private static Pixel[][] optionBuffer = new Pixel[OPTION_HEIGHT][WIDTH];

	public static void draw() {
		clrscr();

		renderQueue.forEach(e -> e.draw(RenderType.NORMAL));

		// Print buffer
		for (int y = 0; y < HEIGHT; y++) {
			// Trick to use a reference to the string instead of the value itself.
			// I don't like java sometimes...
			String output[] = new String[]{""};

			Arrays.asList(buffer[y]).stream()
					.filter(x -> x != null)
					.forEach(x -> output[0] += BGCOLOR.toString() + x);

			System.out.println(output[0]);
		}

		optionQueue.forEach(e -> e.draw(RenderType.OPTION));

		if (optionQueue.size() > 0) {
			for (int y = 0; y < OPTION_HEIGHT; y++) {
				String output[] = new String[]{""};

				Arrays.asList(optionBuffer[y]).stream()
						.filter(x -> x != null)
						.forEach(x -> output[0] += BGCOLOR.toString() + x);

				System.out.println(output[0]);
		}

		}

		optionQueue.clear();


	}

	public static void addDrawable(Drawable obj) {
		renderQueue.add(obj);
	}

	public static void addDrawable(Drawable obj, RenderType rt) {
		if (rt == RenderType.NORMAL) {
			renderQueue.add(obj);
		} else if (rt == RenderType.OPTION){
			optionQueue.add(obj);
		}
	}

	private static void clrscr(){
	    //Clears Screen in java
		
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				buffer[y][x] = new Pixel(' ', Color.BLACK);
			}
		}

		for (int y = 0; y < OPTION_HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				optionBuffer[y][x] = new Pixel(' ', Color.BLACK);
			}
		}

		System.out.print("\033[H\033[2J"); // Clears the console
		System.out.flush(); // Move the cursor

	}

	public static void removeDrawable(Drawable obj) {
		renderQueue.remove(obj);
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

	// Only vbox container for now...
	public static void RenderOptions(int[] cords, Pixel[][] drawableArray) {
		for (int y = 0; y < OPTION_HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {

				if (y < cords[1] || x < cords[0]) continue;



				int dx = x - cords[0];
				int dy = y - cords[1];

				if (dy >= drawableArray.length || dx >= drawableArray[0].length) break;

				Pixel c = drawableArray[dy][dx];

				if (c == null) {
					continue;
				}

				Renderer.optionBuffer[y][x] = c;

			}
		}
	}


}