package ca.concordia.encs.comp354.controller;

import java.util.Objects;

/**
 * SpyMasters are Players that produce clues for consumption by {@link Operative}s.
 * @author Alex Abrams
 */

import ca.concordia.encs.comp354.model.ReadOnlyGameState;
import ca.concordia.encs.comp354.model.Team;
import ca.concordia.encs.comp354.model.action.GiveClueAction;

/**
 * Spymasters are players that produce clues for {@link Operative}s.
 * @author Alex Abrams
 * @author Mykyta Leonidov
 *
 */
public final class SpyMaster extends Player {
    
    public interface Strategy {
        /**
         * Requests a clue from this strategy, given the current game state.
         * @param owner  the owning player object
         * @param state  a read-only view of the current game state
         * @return a clue, or <tt>null</tt> if there are no more cards to guess
         */
        Clue giveClue(SpyMaster owner, ReadOnlyGameState state);
    }
    
    private final Strategy strategy;
    
	//Constructor uses super
	public SpyMaster(Team team, Strategy strategy) {
		super(team);
        this.strategy = Objects.requireNonNull(strategy, "strategy");
	}
	
	GiveClueAction giveClue(ReadOnlyGameState state) {
	    Clue ret = strategy.giveClue(this, state);
	    if (ret==null) {
	        throw new IllegalStateException("cannot produce another clue");
	    }
	    return new GiveClueAction(this, ret);
	}
}

