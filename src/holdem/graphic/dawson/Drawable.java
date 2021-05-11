package holdem.graphic.dawson;

import holdem.renderer.dawson.RenderType;

public abstract class Drawable {
	protected int[] cords = new int[2];

	protected Drawable(int x, int y) {
	    this.cords[0] = x;
	    this.cords[1] = y;
	}


	public void setPos(int x, int y) {
		this.cords[0] = x;
		this.cords[1] = y;
	}

	/**
	 * Creates a Pixel array representation of the Drawable object.
	 * @param renderType which type of render it should call.
	 */
	abstract public void draw(RenderType renderType);
	abstract public int getWidth();
	abstract public int getHeight();

}
