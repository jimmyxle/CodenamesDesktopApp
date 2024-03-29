package ca.concordia.encs.comp354.controller;

import ca.concordia.encs.comp354.CompletablePromise;
import ca.concordia.encs.comp354.Promise;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.action.GuessCardAction;
import ca.concordia.encs.comp354.model.action.OperativeAction;
import ca.concordia.encs.comp354.model.action.SkipTurnAction;

public class HumanOperativeStrategy implements Operative.Strategy {
    
    @Override
    public Promise<OperativeAction> guessCard(Operative owner, GameState state, Clue clue) {
        CompletablePromise<OperativeAction> ret = new CompletablePromise<>();
        
        state.requestOperativeInput().then(event->{
            if (event.isSkip()) {
                ret.finish(new SkipTurnAction(owner));
            } else if (event.isGuess()) {
                ret.finish(new GuessCardAction(owner, event.asGuess().getCoords()));
            } else {
                throw new IllegalArgumentException("input event of type "+event.getClass());
            }
        });
        
        return ret;
    }
    
    @Override
    public boolean isHuman() {
        return true;
    }

}
