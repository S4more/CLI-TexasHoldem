package holdem.hud.dawson;

import holdem.graphic.dawson.Drawable;
import holdem.renderer.dawson.RenderType;
import holdem.renderer.dawson.Renderer;
import holdem.utils.dawson.ArrayHelper;

import java.util.Arrays;

public class HBoxContainer extends Drawable {
    // TODO handle strings that are too large.
    private final Drawable elements[];
    private final int WIDTH;
    private final int HEIGHT;
    private final int padding = 2;

    public HBoxContainer(int x, int y, int w, int h, Drawable... elements) {
        super(x, y);
        this.WIDTH = w;
        this.HEIGHT = h;

        this.elements = elements;

        int spacing = ArrayHelper.getWidthOfLargestDrawable(elements) + 5;

        for (int i = 0; i < elements.length; i++) {
           this.elements[i].setPos(
                    this.cords[0] + (i * spacing),
                   this.cords[1]
           );
        }


    }

    @Override
    public void draw(RenderType renderType) {
        Arrays.asList(this.elements).forEach(e -> e.draw(renderType));
    }

    @Override
    public int getWidth() {
        return this.WIDTH;
    }

    @Override
    public int getHeight() {
        return this.HEIGHT;
    }
}