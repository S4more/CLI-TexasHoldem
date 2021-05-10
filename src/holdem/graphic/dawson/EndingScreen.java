package holdem.graphic.dawson;

import holdem.engine.dawson.Card;
import holdem.engine.dawson.PokerResponse;
import holdem.hud.dawson.Dialogue;
import holdem.hud.dawson.Text;
import holdem.renderer.dawson.Pixel;
import holdem.renderer.dawson.RenderType;
import holdem.renderer.dawson.Renderer;
import holdem.utils.dawson.Color;

public class EndingScreen extends Drawable {


    // TODO won by resignation
    // TODO all in.


    private final Dialogue congratulation;

    public EndingScreen(String playerName) {
        super(0, 0);

        congratulation = new Dialogue(10, 10, 60, 3, 0,
                new Text(playerName + " won!", Color.YELLOW),
                new Text("Last standing player!", Color.YELLOW),
                new Text("Thank you for playing", Color.YELLOW));

        congratulation.activate();

    }

    public EndingScreen(String playerName, PokerResponse combinations) {
        super(0, 0);

        congratulation = new Dialogue(10, 10, 60, 4, 0,
                new Text(playerName + " won!", Color.YELLOW),
                new Text("Combination: " + combinations.combination.toString(), Color.YELLOW),
                new Text(combinations.points + " points!", Color.YELLOW),
                new Text("Thank you for playing", Color.YELLOW));

        congratulation.activate();
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
        congratulation.draw(renderType);
    }

    @Override
    public int getWidth() {
        return Renderer.WIDTH;
    }

    @Override
    public int getHeight() {
        return Renderer.HEIGHT;
    }
}
