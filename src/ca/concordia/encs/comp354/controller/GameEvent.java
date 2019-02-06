package ca.concordia.encs.comp354.controller;

import ca.concordia.encs.comp354.model.GameAction;

/**
 * Describes the outcome of a {@link GameAction}.
 * @author Nikita Leonidov
 *
 */
public enum GameEvent {
    /** The action ended unremarkably. */
    NONE              (false, ""),
    /** The action ended the turn prematurely without ending the game; the action's team may not make any more guesses. */
    END_TURN          (false, "turn ended"),
    /** The action revealed an assassin, ending the game with a loss to the action's team. */
    GAME_OVER_ASSASSIN(true, "assassin"),
    /** The action met the red team's objective, ending the game. */
    GAME_OVER_RED_WON (true, "red wins"),
    /** The action met the blue team's objective, ending the game. */
    GAME_OVER_BLUE_WON(true, "blue wins");
    
    private final boolean endsGame;
    private final String  text;
    
    GameEvent(boolean endsGame, String text) {
        this.endsGame = endsGame;
        this.text     = text;
    }
    
    /**
     * @return <tt>true</tt> iff the event ends the game
     */
    public boolean isTerminal() {
        return endsGame;
    }
    
    public String getEventText() {
        return text;
    }
}
