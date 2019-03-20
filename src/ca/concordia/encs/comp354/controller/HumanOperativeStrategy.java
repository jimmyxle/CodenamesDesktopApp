package ca.concordia.encs.comp354.controller;

import ca.concordia.encs.comp354.controller.action.OperativeAction;
import ca.concordia.encs.comp354.model.ReadOnlyGameState;

public class HumanOperativeStrategy implements Operative.Strategy {
    
    @Override
    public OperativeAction guessCard(Operative owner, ReadOnlyGameState state, Clue clue) {
        // TODO fill this out!
        // this only has to produce a controller.actions.LetViewGuessCardAction
        return null;
    }

}
