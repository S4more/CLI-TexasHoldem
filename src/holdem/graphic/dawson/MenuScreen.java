package holdem.graphic.dawson;

import holdem.hud.dawson.Dialogue;
import holdem.hud.dawson.Text;
import holdem.renderer.dawson.Pixel;
import holdem.renderer.dawson.RenderType;
import holdem.renderer.dawson.Renderer;
import holdem.utils.dawson.Color;

import java.util.Arrays;

public class MenuScreen extends Drawable {

    private Dialogue outsideBorder;
    private Dialogue instructions[];
    private int actualFrame = 1;

    public MenuScreen() {
        super(0, 0);

        outsideBorder = new Dialogue(10, 0, 60, Renderer.HEIGHT - 1, 0,
                new Text("Texas Hold'em by Guilherme Correa", Color.YELLOW));

        this.instructions = new Dialogue[4];


        instructions[0] = new Dialogue(25, 2, 30, 5, 1,
                    new Text("Each turn, you will be", Color.CYAN),
                    new Text("prompted to insert one", Color.CYAN),
                    new Text("number to pick an action.", Color.CYAN)
                );

        instructions[1] = new Dialogue(25, 7, 30, 6, 1,
                new Text("Some actions require", Color.CYAN),
                new Text("arguments. The usage", Color.CYAN),
                new Text("for these these are:", Color.CYAN),
                new Text("option_number money", Color.RED)
        );

        instructions[2] = new Dialogue(25, 13, 30, 6, 1,
            new Text("Opponents in red are", Color.CYAN),
            new Text("not playing. Opponents", Color.CYAN),
            new Text("in yellow are making a", Color.CYAN),
            new Text("decision.", Color.CYAN)
        );

        instructions[3] = new Dialogue(25, 19, 30, 3, 1,
            new Text("Have fun!", Color.YELLOW)
        );

        outsideBorder.activate();
        Arrays.stream(instructions).forEach(i -> i.activate());
    }

    @Override
    public void draw(RenderType renderType) {
        Pixel[][] bufferArray = new Pixel[Renderer.HEIGHT][Renderer.WIDTH];

        for (int y = 0; y < Renderer.HEIGHT; y++) {
            for (int x = 0; x < Renderer.WIDTH; x++) {
                bufferArray[y][x] = new Pixel(' ', Color.CYAN);
            }
        }


        Renderer.Render(this.cords, bufferArray);
        outsideBorder.draw(renderType);
        Arrays.stream(instructions).limit(actualFrame).forEach(e -> e.draw(renderType));
        actualFrame++;

    }

    @Override
    public int getWidth() {
        return 60;
    }

    @Override
    public int getHeight() {
        return Renderer.HEIGHT;
    }

    public int frames() {
       return this.instructions.length;
    }
}
