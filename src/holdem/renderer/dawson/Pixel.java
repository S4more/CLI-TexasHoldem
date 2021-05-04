package holdem.renderer.dawson;

import holdem.utils.dawson.Color;

public class Pixel {
    // TODO - Change to record?
    public final Color color;
    public final char ascii;

    public Pixel(char ascii, Color color) {
        this.color = color;
        this.ascii = ascii;
    }

    public String toString() {
        return color.toString() + ascii + Color.RESET;
    }

}
