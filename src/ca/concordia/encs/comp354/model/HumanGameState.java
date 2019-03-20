package ca.concordia.encs.comp354.model;

import ca.concordia.encs.comp354.Promise;

/**
 * Human player-facing interface to the {@link GameState}. Largely read-only, but allows delegating decisions like
 * guesses to the view.
 * 
 * @author Mykyta Leonidov
 *
 */
public interface HumanGameState extends ReadOnlyGameState {

    /**
     * Requests a guess from the view. The guess will be placed in the given promise.
     * @param guess the destination promise for the guess
     */
    void requestGuess(Promise<Coordinates> guess);
    
}
