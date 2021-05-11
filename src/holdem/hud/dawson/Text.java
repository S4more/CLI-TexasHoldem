package holdem.hud.dawson;

import holdem.graphic.dawson.Drawable;
import holdem.renderer.dawson.Pixel;
import holdem.renderer.dawson.RenderType;
import holdem.renderer.dawson.Renderer;
import holdem.utils.dawson.Color;

/**
 * An engine compliant String representation with colors that extends Drable.
 * Use this if you want strings to be rendered as any other game objects.
 */
public class Text extends Drawable {
	/**
	 * The special separator is not rendered. We can't use white spaces because of the trim method.
	 */
	public static final char SPECIAL_SEPARATOR = '^';


	private int maxWidth;
	private int maxHeight;

	private Color color;
	
	private String text;
	private String[] splitText;

	/**
	 * @param x the X coordinate of the Text.
	 * @param y the Y coordinate of the Text.
	 * @param maxWidth the Max Width this text can have.
	 * @param maxHeight the max number of rows that this text can have.
	 * @param text the content of the Text
	 * @param color the color of the Text.
	 */
	public Text(int x, int y, int maxWidth, int maxHeight, String text, Color color) {
		super(maxWidth, maxHeight);
		this.color = color;
		this.cords = new int[] {x, y};
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.text = text;
		this.splitToFit();
	}

	public Text(String text, Color color) {
		this(0, 0, text.length(), 1, text, color);
	}

	public Text(int x, int y, int maxWidth, int maxHeight, String text) {
	    this(x, y, maxWidth, maxHeight, text, Color.RED);
	}

	/**
	 * Splits the String into multiple Strings in order to make it fit the max given size.
	 */
	private void splitToFit() {
		int textSize = this.text.length();
		int rows = (int) Math.ceil((double) textSize / this.maxWidth);
		boolean isTooLong = false;
		if (rows > this.maxHeight) {
			rows = this.maxHeight;
			isTooLong = true;
		}
		this.splitText = new String[rows];	
		
		for (int i = 0; i < this.splitText.length; i++) {
			try {
				if (i == this.splitText.length - 1 && isTooLong) {	
					this.splitText[i] = this.text.substring(i * this.maxWidth, (i + 1) * this.maxWidth - 3).trim()+ "...";
				} else {
					this.splitText[i] = this.text.substring(i * this.maxWidth, (i + 1) * this.maxWidth).trim();
				}
			}
			catch (StringIndexOutOfBoundsException e) {
				this.splitText[i] = this.text.substring(i * this.maxWidth, textSize).trim();
			}
		}
	}
	
	
	@Override
	public void draw(RenderType rt) {
		Pixel bufferArray[][] = new Pixel[this.splitText.length][this.maxWidth];
		for (int y = 0; y < this.splitText.length; y++) {
			char c[] = this.splitText[y].toCharArray();
			for (int x = 0; x < this.maxWidth; x++) {	
				try {
				    if (c[x] == SPECIAL_SEPARATOR) continue;
					bufferArray[y][x] = new Pixel(c[x], this.color);
				} catch (ArrayIndexOutOfBoundsException e) {
					bufferArray[y][x] = new Pixel(' ', this.color);
				}
			}
		}

		if (rt == RenderType.NORMAL) {
			Renderer.Render(this.cords, bufferArray);
		} else if (rt == RenderType.OPTION) {
			Renderer.RenderOptions(this.cords, bufferArray);
		}
	}

	@Override
	public int getWidth() {
	    return this.maxWidth;
	}

	@Override
	public int getHeight() {
	    return this.splitText.length;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) { this.color = color;}

	/**
	 * Returns a text with the same values but with a different string.
	 * @param text
	 * @return
	 */
	public Text changeText(String text) {
		return new Text(this.cords[0], this.cords[1], this.maxWidth, this.maxHeight, text);
	}

}
