package holdem.utils.dawson;

public enum Color {

    /*
        Variables that which the colors start with an additional B are the bright version of the color.
     */

    RESET("\u001B[0m"),

    BLACK("\u001B[30m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m"),
    WHITE("\u001B[37m"),

    BACKGROUND_BLACK("\u001b[40m"),
    BACKGROUND_RED("\u001b[41m"),
    BACKGROUND_WHITE("\u001b[47m");

    public final String value;

    private Color(String value) {
       this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
