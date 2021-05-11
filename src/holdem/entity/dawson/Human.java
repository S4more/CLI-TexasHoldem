package holdem.entity.dawson;

import holdem.engine.dawson.Card;
import holdem.hud.dawson.HBoxContainer;
import holdem.hud.dawson.VBoxContainer;
import holdem.input.dawson.InputHandler;
import holdem.input.dawson.InputResponse;
import holdem.input.dawson.Option;
import holdem.renderer.dawson.RenderType;
import holdem.renderer.dawson.Renderer;
import holdem.renderer.dawson.ScreenSection;
import holdem.utils.dawson.Container;

import java.util.Optional;

import static holdem.input.dawson.InputHandler.printError;

public class Human extends Player {

    public Human(ScreenSection screenSection, String name) {
        super(screenSection, name);
    }

    /**
     * Call the input handler with the given options and
     * try to take the chosen action.
     * @param options
     * @return
     */
    public double getInput(Option[] options, double lastBet) {
        isTurn = true;
        addOptionsToScreen(options);
        activatePlayer();
        while (true) {
            InputResponse answer = InputHandler.getInstance().getValidInput(options);
            switch ( answer.option.action ) {
                case FOLD:
                    return handleFold();
                case CHECK:
                    return handleCheck();
                case BET:
                    try {
                        return handleBet(answer.arg.get());
                    } catch (Exception e) {
                        InputHandler.waitForEnter(Optional.of("You don't have the money to do that!"));
                        continue;
                    }
                case RAISE:
                    if (answer.arg.get() <= lastBet) {
                        printError("Your bet should be higher than the previous bet.");
                        continue;
                    }
                    try {
                        return handleRaise(answer.arg.get());
                    } catch (Exception e){
                        printError("You don't have the money to do that!");
                        continue;
                    }
                case CALL:
                    try {
                        return this.handleCall(lastBet);
                    } catch (Exception e) {
                        InputHandler.waitForEnter(Optional.of("Oupsie oupsie! You don't have the money to call!"));
                        return getInput(new Option[] {Option.FOLD, Option.ALLIN}, lastBet);
                    }
                case ALLIN:
                    try {
                        return handleAllIn();
                    } catch (Exception e) {
                        InputHandler.waitForEnter(
                                Optional.of("You can't all win with 0 money. You are out.")
                        );
                        this.isPlaying = false;
                        return 0;
                    }
            }
        }
    }

    /**
     * Adds a HBoxContainer with all the options that a player can pick in a turn
     * to the Renderer OPTION buffer array.
     * @param options
     */
    private void addOptionsToScreen(Option[] options) {
        VBoxContainer[] splitBoxes = Container.splitIntoMultiple(0, 0, 20, 3,
                options
        );

        HBoxContainer hbox = new HBoxContainer(0, 0, Renderer.WIDTH, 3, splitBoxes);
        Renderer.addDrawable(
                hbox,
                RenderType.OPTION);
        Renderer.addDrawable(InputHandler.getInstance(), RenderType.OPTION);
        Renderer.draw();

    }

    /**
     * Set the hand of the player and flip the cards so they face up.
     * @param hand the cards to set.
     */
    @Override
    public void setHand(Card[] hand) {
        this.hand = hand;
        for (int i = 0; i < hand.length; i++) {
            hand[i].setCords(new int[] {this.xAnchor + (Card.WIDTH - 4) * i, this.yAnchor});
            hand[i].flip();
        }
    }

}
