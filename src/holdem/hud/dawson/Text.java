package holdem.hud.dawson;

import holdem.graphic.dawson.Drawable;
import holdem.renderer.dawson.Renderer;

public class Text extends Drawable{
	private int maxWidth;
	private int maxHeight;
	
	private String text;
	private String[] splitText;

	/**
	 * 
	 * @param maxWidth should be at least 3.
	 * @param maxHeight
	 * @param x
	 * @param y
	 * @param text
	 */
	public Text(int x, int y, int maxWidth, int maxHeight, String text) {
		this.cords = new int[] {x, y};
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.text = text;
		this.splitToFit();
	}

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
	public void draw() {
		char bufferArray[][] = new char[this.splitText.length][this.maxWidth];
		for (int y = 0; y < this.splitText.length; y++) {
			char c[] = this.splitText[y].toCharArray();
			for (int x = 0; x < this.maxWidth; x++) {	
				try {
					bufferArray[y][x] = c[x];
				} catch (ArrayIndexOutOfBoundsException e) {
					bufferArray[y][x] = '\0';
				}
			}
		}
		
		Renderer.Render(this.cords, bufferArray);
	}
	
	public int getRowCount() {
		return this.splitText.length;
	}
	
}
