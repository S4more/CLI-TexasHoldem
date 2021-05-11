package holdem.renderer.dawson;

import java.util.ArrayList;
import java.util.Arrays;

import holdem.graphic.dawson.Drawable;
import holdem.utils.dawson.Color;

/**
 * Render all registered drawable objects to the screen in its buffer array.
 */
public class Renderer {

	public static final int WIDTH = 80;
	public static final int HEIGHT = (int) (WIDTH / 3.2);
	public static final int OPTION_HEIGHT = 4;
	private static final Color BGCOLOR = Color.BACKGROUND_BLACK;

	private static ArrayList<Drawable> renderQueue = new ArrayList<>();

	/** The option queue only draws once and them empties itself. */
	private static ArrayList<Drawable> optionQueue = new ArrayList<>();

	private static Pixel[][] buffer = new Pixel[HEIGHT][WIDTH];
	private static Pixel[][] optionBuffer = new Pixel[OPTION_HEIGHT][WIDTH];

	/**
	 * Clears the previous buffer array and puts the new one into the terminal.
	 */
	public static void draw() {
		clrscr();
		renderQueue.forEach(e -> e.draw(RenderType.NORMAL));
		for (int y = 0; y < HEIGHT; y++) {
			// Trick to use a reference to the string instead of the value itself.
			String output[] = new String[]{""};

			// For each non null element in the buffer, adds the background.
			Arrays.asList(buffer[y]).stream()
					.filter(x -> x != null)
					.forEach(x -> output[0] += BGCOLOR.toString() + x);

			System.out.println(output[0]);
		}

		optionQueue.forEach(e -> e.draw(RenderType.OPTION));

		if (optionQueue.size() > 0) {
			for (int y = 0; y < OPTION_HEIGHT; y++) {
				String output[] = new String[]{""};

				// For each non null element in the buffer, adds the background.
				Arrays.asList(optionBuffer[y]).stream()
						.filter(x -> x != null)
						.forEach(x -> output[0] += BGCOLOR.toString() + x);

				System.out.println(output[0]);
			}
		}
		// The option Queue only lasts for one draw call.
		optionQueue.clear();
	}


	/**
	 * Clears the terminal and the buffers array.
	 */
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

	/**
	 * Write the given element to the buffer array in the specified coordinates.
	 * @param cords where to draw the element.
	 * @param drawableArray element Pixel representation.
	 */
	public static void Render(int[] cords, Pixel[][] drawableArray) {
		
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {

				// If not in the given object area
				if (y < cords[1] || x < cords[0]) continue;

				// Offset the screen area by the object area
				int dx = x - cords[0];
				int dy = y - cords[1];

				// If the offset is greater than the object area
				if (dy >= drawableArray.length || dx >= drawableArray[0].length) break;

				// Get the offset coordinate of the object
				Pixel c = drawableArray[dy][dx];

				if (c == null) {
					continue;
				}

				// Write it to the buffer array
				Renderer.buffer[y][x] = c;
			}
		}
	}

	/**
	 * Write the given element to the elementBuffer array in the specified coordinates.
	 * @param cords where to draw the element.
	 * @param drawableArray element Pixel representation.
	 */
	public static void RenderOptions(int[] cords, Pixel[][] drawableArray) {
		for (int y = 0; y < OPTION_HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {

				// If not in the given object area
				if (y < cords[1] || x < cords[0]) continue;

				// Offset the screen area by the object area
				int dx = x - cords[0];
				int dy = y - cords[1];

				// If the offset is greater than the object area
				if (dy >= drawableArray.length || dx >= drawableArray[0].length) break;

				// Get the offset coordinate of the object
				Pixel c = drawableArray[dy][dx];

				if (c == null) {
					continue;
				}

				// Write it to the buffer array
				Renderer.optionBuffer[y][x] = c;

			}
		}
	}

	public static void removeDrawable(Drawable obj) {
		renderQueue.remove(obj);
	}

	public static void addDrawable(Drawable obj, RenderType rt) {
		if (rt == RenderType.NORMAL) {
			renderQueue.add(obj);
		} else if (rt == RenderType.OPTION){
			optionQueue.add(obj);
		}
	}

	public static void addDrawable(Drawable obj) {
		renderQueue.add(obj);
	}

}