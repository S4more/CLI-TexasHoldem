package holdem.hud.dawson;

import java.util.Arrays;

import holdem.graphic.dawson.Drawable;
import holdem.renderer.dawson.Renderer;

public class VBoxContainer extends Drawable {
	private Text texts[];
	private int width;
	private int height;
	private int separator = 0;

	public VBoxContainer(int x, int y, int width, int height, String[] texts) {
		this.cords = new int[] {x, y};
		this.width = width;
		this.height = height;
		this.width = width;
		this.height = height;
		
		int offset = 0;
		
		this.texts = new Text[texts.length];
		for (int i = 0; i < texts.length; i++) {
			if (offset + (i * separator) > this.height) System.out.println("Overflowing VBox Container.");
			this.texts[i] = new Text(x, y + offset + (i * separator), width, height, texts[i]);
			offset += this.texts[i].getRowCount();
		}	
	}
	
	@Override
	public void draw() {
		char[][] bufferArray = new char[this.height][this.width];
		for (int y = 0; y < this.height; y++) {
			for(int x = 0; x < this.width; x++) {
				bufferArray[y][x] = '\0';
			}
		}
		Renderer.Render(this.cords, bufferArray);
		Arrays.asList(texts).forEach(t -> t.draw());
	}
}
