package ca.concordia.encs.comp354.controller.action;

import ca.concordia.encs.comp354.controller.Clue;
import ca.concordia.encs.comp354.controller.Operative;
import ca.concordia.encs.comp354.controller.Operative.Strategy;
import ca.concordia.encs.comp354.model.ReadOnlyGameState;

public class NullOperativeStrategy implements Strategy {

    @Override
    public OperativeAction guessCard(Operative owner, ReadOnlyGameState state, Clue clue) {
        // do nothing
        return null;
    }

}
