package ca.concordia.encs.comp354.model.action;

import ca.concordia.encs.comp354.Promise;
import ca.concordia.encs.comp354.controller.Clue;
import ca.concordia.encs.comp354.controller.Operative;
import ca.concordia.encs.comp354.controller.Operative.Strategy;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.action.OperativeAction;

public class NullOperativeStrategy implements Strategy {

    @Override
    public Promise<OperativeAction> guessCard(Operative owner, GameState state, Clue clue) {
        // do nothing
        return null;
    }

}
