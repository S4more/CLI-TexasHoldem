package holdem.hud.dawson;

import holdem.renderer.dawson.Pixel;
import holdem.renderer.dawson.RenderType;
import holdem.renderer.dawson.Renderer;
import holdem.utils.dawson.Color;

import java.util.Arrays;

public class Dialogue extends VBoxContainer{

    private boolean isActive = false;

    public Dialogue(int x, int y, Text ... elements ) {
        super(x, y, elements);

        for (int i = 0; i < elements.length; i++) {
            elements[i].setPos(this.cords[0] , this.cords[1]);
        }

    }

    public Dialogue(int x, int y, int width, int height, Text ... elements ) {
        super(x, y, width, height, elements);

        for (int i = 0; i < elements.length; i++) {
            int deltaX = Align.spacesToAlignMiddle(elements[i], width);
            elements[i].setPos(deltaX + this.cords[0], this.cords[1] + 1);
        }
    }

    public Dialogue(int x, int y, int width, int height, int heightShift, Text ... elements ) {
        super(x, y, width, height, elements);

        for (int i = 0; i < elements.length; i++) {
            int deltaX = Align.spacesToAlignMiddle(elements[i], width);
            elements[i].setPos(deltaX + this.cords[0], this.cords[1] + i + heightShift);
        }
    }

    @Override
    public void draw(RenderType rt) {

        if (!isActive) return;

        Color dialogueColor = Color.YELLOW;

        Pixel[][] bufferArray = new Pixel[this.getHeight()][this.getWidth()];
        for (int y = 0; y < this.getHeight(); y++) {
            for(int x = 0; x < this.getWidth(); x++) {
                bufferArray[y][x] = new Pixel(' ', Color.GREEN);
            }
        }

        bufferArray[0][0] = new Pixel('╭',dialogueColor);
        bufferArray[0][this.getWidth() - 1] = new Pixel('╮', dialogueColor);
        for (int x = 1; x < this.getWidth() - 1; x++) {
            bufferArray[0][x] = new Pixel('─', dialogueColor);
            bufferArray[this.getHeight() - 1][x] = new Pixel('─', dialogueColor);
        }

        for (int y = 1; y < this.getHeight() - 1; y++) {
            bufferArray[y][0] = new Pixel('│', dialogueColor);
            bufferArray[y][this.getWidth() - 1] = new Pixel('│', dialogueColor);
        }

        bufferArray[this.getHeight() - 1][0] = new Pixel('╰', dialogueColor);
        bufferArray[this.getHeight() - 1][this.getWidth() - 1] = new Pixel('╯', dialogueColor);

        Renderer.Render(this.cords, bufferArray);
        Arrays.asList(this.getElements()).forEach(t -> t.draw(rt));
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void setText(String text) {
        Text t = (Text) this.getElements()[0];
        this.getElements()[0] = t.changeText(text);
    }

}
