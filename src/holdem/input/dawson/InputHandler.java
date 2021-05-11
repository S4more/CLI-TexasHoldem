package holdem.input.dawson;

import holdem.graphic.dawson.Drawable;
import holdem.hud.dawson.Text;
import holdem.renderer.dawson.RenderType;
import holdem.renderer.dawson.Renderer;
import holdem.utils.dawson.Color;

import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

/**
 * Singleton class to handle all user related input.
 */
public class InputHandler extends Drawable {

    private static InputHandler instance = null;

    private static final Scanner scanner = new Scanner(System.in);
    // TODO find a decent way of printing a custom input.
    private final Text message = new Text("Your choice: ", Color.RED);

    /** This magic ascii scape moves the cursor to the end + 1 char of the message variable. */
    private final String CURSOR_MOVE_MESSAGE = String.format("%c[%d;%df",0x1B ,Renderer.OPTION_HEIGHT + Renderer.HEIGHT, message.getWidth() + 1);
    protected InputHandler(int x, int y) {
        super(x, y);
        this.message.setPos(x, y);
    }

    /**
     * Prompt the user and expect a valid input based on the integers.
     * It waits for the next draw call so it can display it's text.
     * @param inputIndexes the valid integers that the handler should expect.
     * @return the selected input.
     */
    public InputResponse getValidInput(Option[] inputIndexes) {
        while (true) {
            System.out.print(CURSOR_MOVE_MESSAGE);
            for (int i = 0; i < Renderer.WIDTH - message.getWidth() - 1; i++) {
                System.out.print(Color.BACKGROUND_BLACK + " " + Color.RESET);
            }

            System.out.print(CURSOR_MOVE_MESSAGE);
            String input = this.scanner.nextLine();

            // Splits input into multiple parts to check if arguments were provided.
            String[] splitInput = input.split(" ");

            int identifier;
            try {
                identifier = Integer.parseInt(splitInput[0]);
            } catch (NumberFormatException e) {
                printError("Please write a number!");
                continue;
            }

            Optional<Option> match = Arrays.stream(inputIndexes)
                    .filter(option -> option.identifier == identifier)
                    .findFirst();

            if (match.isEmpty()) {
                printError("Wowowow! This is an invalid input. Try again!");
                continue;
            }

            Option option = match.get();

            if (option.hasArgs) {
                try {
                    System.out.print(Color.RESET);
                    if(Integer.valueOf(splitInput[1]) < 0) {
                        printError("Please input a positive argument!");
                        continue;
                    }
                    return new InputResponse(option, Optional.of(Integer.valueOf(splitInput[1])));
                } catch (ArrayIndexOutOfBoundsException e) {
                    printError("This option requires an argument!");
                    continue;
                } catch (NumberFormatException e) {
                    printError("This number is too big.");
                    continue;
                }
            }

            System.out.print(Color.RESET);
            return new InputResponse(option, Optional.empty());
        }
    }

    /**
     * Clears the actual line and prints an error message.
     * @param text
     */
    public static void printError(String text) {
        System.out.print("\033[2K"); // Clears the current line.
        System.out.println(Color.ERROR + text + Color.RESET);
    }

    /**
     * Stop the program execution with a scanner and, if present
     * displays a message.
     */
    public static void waitForEnter(Optional<String> message) {
        System.out.print("\033[2K"); // Clears the current line.
        if (message.isPresent()) {
            System.out.print(Color.ERROR + message.get() + Color.RESET);
        }
        scanner.nextLine();
    }

    public static InputHandler getInstance() {
        if (instance == null) instance = new InputHandler(0, 3);
        return instance;
    }

    @Override
    public void draw(RenderType renderType) {
        this.message.draw(RenderType.OPTION);
    }

    @Override
    public int getWidth() {
        return this.message.getWidth();
    }

    @Override
    public int getHeight() {
        return this.message.getHeight() + 1;
    }
}
