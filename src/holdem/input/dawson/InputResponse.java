package holdem.input.dawson;

import java.util.Optional;

public class InputResponse {
    public final Option option;
    public final Optional<Integer> arg;

    public InputResponse(Option option, Optional<Integer> arg) {
       this.option = option;
       this.arg = arg;
    }
}
