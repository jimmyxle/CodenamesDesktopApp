package ca.concordia.encs.comp354.controller;

import ca.concordia.encs.comp354.model.GameAction;

/**
 * Describes the outcome of a {@link GameAction}.
 * @author Nikita Leonidov
 *
 */
public enum GameEvent {
    /** The action ended unremarkably. */
    NONE,
    /** The action revealed an assassin, ending the game with a loss to the action's team. */
    GAME_OVER_ASSASSIN,
    /** The action met an objective (not necessarily the calling team's), ending the game. */
    GAME_OVER_OBJECTIVE, 
    /** The action ended the turn without ending the game; the action's team may not make any more guesses. */
    END_TURN
}
