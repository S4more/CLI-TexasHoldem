package holdem.input.dawson;

import holdem.engine.dawson.Action;
import holdem.graphic.dawson.Drawable;
import holdem.hud.dawson.Text;
import holdem.renderer.dawson.RenderType;
import holdem.utils.dawson.Color;

/**
 * Option transforms the ActionEnum in a Drawable class.
 * It is used mainly to validate and get input from the user.
 */
public class Option extends Drawable {

    public static final Option FOLD = new Option(Action.FOLD);
    public static final Option CHECK = new Option(Action.CHECK);
    public static final Option BET = new Option(Action.BET);
    public static final Option CALL = new Option(Action.CALL);
    public static final Option RAISE = new Option(Action.RAISE);
    public static final Option ALLIN = new Option(Action.ALLIN);

    public final int identifier;
    public final Text text;
    public Action action;
    public final boolean hasArgs;

    public Option(Action action) {
        this(action.name(), action.color, action.identifier, action.hasArgs);
        this.action = action;
    }

    private Option(String text, Color color, int identifier, boolean hasArgs) {
        super(0, 0);
        this.identifier = identifier;
        this.text = new Text("" + identifier + ") " + text, color);
        this.hasArgs = hasArgs;
    }

    @Override
    public void setPos(int x, int y) {
       this.cords[0]  = x;
       this.cords[0] = y;
       this.text.setPos(x, y);
    }

    @Override
    public void draw(RenderType renderType) {
        this.text.draw(RenderType.OPTION);
    }

    @Override
    public int getWidth() {
        return this.text.getWidth();
    }

    @Override
    public int getHeight() {
        return this.text.getHeight();
    }

    public String toString() {
        return this.text.toString();
    }
}
