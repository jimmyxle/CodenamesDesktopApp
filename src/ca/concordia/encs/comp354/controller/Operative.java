package ca.concordia.encs.comp354.controller;

import java.util.Objects;

import ca.concordia.encs.comp354.controller.action.OperativeAction;
import ca.concordia.encs.comp354.model.*;

/**
 * Operatives are Players that produce guesses from {@link SpyMaster}s' clues.
 * @author Alex Abrams
 * @author Mykyta Leonidov
 */

public class Operative extends Player {
	
	public interface Strategy {
	    /**
	     * Requests a guess from this strategy, given the current game state.
	     * @param owner  the owning player object
	     * @param state  a read-only view of the current game state
	     * @param clue   the last clue produced by this player's spymaster
	     * @return an action that will produce the selected coordinates
	     */
	    OperativeAction guessCard(Operative owner, ReadOnlyGameState state, Clue clue);
	}
	
	private final Strategy strategy;
	
	//constructor
	public Operative(Team team, Strategy strategy) {
		super(team);
		this.strategy = Objects.requireNonNull(strategy, "strategy");
	}
	
	OperativeAction guessCard(ReadOnlyGameState state, Clue clue) {
		OperativeAction ret = strategy.guessCard(this, state, clue);
		if (ret==null) {
            throw new IllegalStateException("cannot produce another guess");
        }
		return ret;
	}
}


