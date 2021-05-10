package holdem.hud.dawson;

import java.util.Arrays;

import holdem.graphic.dawson.Drawable;
import holdem.renderer.dawson.Pixel;
import holdem.renderer.dawson.RenderType;
import holdem.renderer.dawson.Renderer;
import holdem.utils.dawson.ArrayHelper;
import holdem.utils.dawson.Color;

public class VBoxContainer extends Drawable {
	private Drawable elements[];
	private int width;
	private int height;
	private int separator = 0;


	public VBoxContainer(int x, int y, Drawable[] elements) {
		this(x, y, ArrayHelper.getWidthOfLargestDrawable(elements), elements.length, elements);
	}

	public VBoxContainer(int x, int y, int width, int height, Drawable[] elements) {
		super(width, height);
		this.cords = new int[] {x, y};
		this.width = width;
		this.height = height;
		this.width = width;
		this.height = height;
		
		int offset = 0;
		
		this.elements = new Drawable[elements.length];

		boolean hasTextField = Arrays.stream(elements[0].getClass().getDeclaredFields())
									.anyMatch(f -> f.getName().equals("text"));

		for (int i = 0; i < elements.length; i++) {
			if (offset + (i * separator) > this.height) System.out.println("Overflowing VBox Container.");

			if (hasTextField) {
				elements[i].setPos(x, y + offset + (i * separator));
				this.elements[i] = elements[i];
			} else {
				this.elements[i] = new Text(x, y + offset + (i * separator), width, height, elements[i].toString());
			}

			offset += this.elements[i].getHeight();
		}	
	}

	@Override
	public void setPos(int x, int y) {
		super.setPos(x, y);
		int offset = 0;
		for (int i = 0; i < this.elements.length; i++) {
			this.elements[i].setPos(x, y + offset + (i * this.separator));
			offset += this.elements[i].getHeight();
		}
	}

	@Override
	public void draw(RenderType rt) {
		Pixel[][] bufferArray = new Pixel[this.height][this.width];
		for (int y = 0; y < this.height; y++) {
			for(int x = 0; x < this.width; x++) {
				//bufferArray[y][x] = new Pixel('#', Color.GREEN);
			}
		}

		Renderer.Render(this.cords, bufferArray);
		Arrays.asList(elements).forEach(t -> t.draw(rt));
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	Drawable[] getElements() {
		return this.elements;
	}
}
