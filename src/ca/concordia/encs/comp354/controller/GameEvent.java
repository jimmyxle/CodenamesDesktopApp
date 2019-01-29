package ca.concordia.encs.comp354.controller;

import ca.concordia.encs.comp354.model.GameAction;

/**
 * Describes the outcome of a {@link GameAction}.
 * @author Nikita Leonidov
 *
 */
public enum GameEvent {
    /** The action ended unremarkably. */
    NONE(false),
    /** The action ended the turn prematurely without ending the game; the action's team may not make any more guesses. */
    END_TURN(false),
    /** The action revealed an assassin, ending the game with a loss to the action's team. */
    GAME_OVER_ASSASSIN(true),
    /** The action met an objective (not necessarily the calling team's), ending the game. */
    GAME_OVER_OBJECTIVE(true);
    
    private boolean endsGame;
    
    GameEvent(boolean endsGame) {
        this.endsGame = endsGame;
    }
    
    /**
     * @return <tt>true</tt> iff the event ends the game
     */
    public boolean isTerminal() {
        return endsGame;
    }
}
