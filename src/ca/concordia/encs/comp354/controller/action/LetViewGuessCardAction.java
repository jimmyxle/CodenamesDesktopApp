package ca.concordia.encs.comp354.controller.action;

import ca.concordia.encs.comp354.CompletablePromise;
import ca.concordia.encs.comp354.Promise;
import ca.concordia.encs.comp354.controller.GameEvent;
import ca.concordia.encs.comp354.controller.Operative;
import ca.concordia.encs.comp354.model.GameState;

public final class LetViewGuessCardAction extends AbstractGuessAction {

    LetViewGuessCardAction(Operative owner) {
        super(owner);
    }

    @Override
    protected Promise<GameEvent> doApply(GameState state) {
        CompletablePromise<GameEvent> ret = new CompletablePromise<>();
        
        // TODO fill this out!
        //
        // 1. use state's `requestGuess()` method to let the view produce a guess; this returns a `Promise`
        // 2. use `Promise.then()` to execute some logic when the `Promise` is finished. the function passed to 
        //    `then()` should:
        //
        //    i.   store the selected coordinates in a field inside this class; call this `lastGuess` or something
        //    ii.  call super.doGuess()
        //    iii. `finish()` the Promise called `ret`, which is instantiated at the top of this method
        //
        // use a lambda expression to do this, the syntax of which is
        //
        //    guess->{ <do something with guess in your function body here> }
        //
        // for an example of using Promises like this, see ca.concordia.encs.comp354.GameState, line 320.
        
        return ret;
    }
    
    @Override
    protected Promise<GameEvent> doRedo(GameState state) {
        // TODO fill this out!
        // this should just call `super.doGuess(`) with the value of `lastGuess` set in `doApply()`
        return null;
    }
}
