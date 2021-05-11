package holdem.entity.dawson;

import holdem.engine.dawson.Card;
import holdem.input.dawson.Option;
import holdem.renderer.dawson.ScreenSection;
import java.util.Random;

public class NPC extends Player {
    public NPC(ScreenSection screenSection, String name) {
        super(screenSection, name);
    }

    public void setHand(Card[] hand) {
        this.hand = hand;

        for (int i = 0; i < hand.length; i++) {
            hand[i].setCords(new int[]{this.xAnchor + (Card.WIDTH - 4) * i, this.yAnchor});
        }
    }

    @Override
    public double getInput(Option[] options, double lastBet) {
        isTurn = true;
        Random rand = new Random();
        activatePlayer();

        Option chosenOption = options[rand.nextInt(options.length)];
        switch ( chosenOption.action ) {
            case FOLD:
                return this.handleFold();
            case CHECK:
                return this.handleCheck();
            case BET: {
                double randomPrice = rand.nextInt((int) this.info.getMoney());
                try {
                    return handleBet(randomPrice);
                } catch (Exception e) {
                }
            }
            case RAISE: {
                double spendLimit = this.info.getMoney();
                try {
                    double randomPrice = 1 + rand.nextInt((int) (spendLimit - lastBet - 1));
                    return handleRaise(randomPrice + lastBet);
                } catch (Exception e) {
                    return getInput(new Option[]{Option.FOLD, Option.ALLIN}, lastBet);
                }
            }
            case CALL:
                try {
                    return handleCall(lastBet);
                } catch (Exception e) {
                    return getInput(new Option[]{Option.FOLD, Option.ALLIN}, lastBet);
                }

            case ALLIN:
                try {
                    return handleAllIn();
                } catch (Exception e) {
                    this.isPlaying = false;
                    return 0;
                }
        }
        return 0;
    }
}
