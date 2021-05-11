package holdem.input.dawson;

import java.util.Optional;

/**
 * Stores input from the InputHandler and,
 * if present, its argument.
 */
public class InputResponse {
    public final Option option;
    public final Optional<Integer> arg;

    public InputResponse(Option option, Optional<Integer> arg) {
       this.option = option;
       this.arg = arg;
    }
}
