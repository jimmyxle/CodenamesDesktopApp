package ca.concordia.encs.comp354.controller.action;

import ca.concordia.encs.comp354.controller.GameEvent;
import ca.concordia.encs.comp354.model.GameAction;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Team;

/**
 * Represents the start of the next team's turn.
 * @author Nikita Leonidov
 *
 */
public class ChangeTurnAction extends GameAction {

	private Team lastTeam;
    
    private boolean applied;
	
    public ChangeTurnAction(Team nextTeam) {
        super(nextTeam);
    }
    
    @Override
    public String getActionText() {
        return String.format("It is now %s's turn", getTeam());
    }

    @Override
    protected GameEvent apply(GameState state) {
    	if (applied) {
    		throw new IllegalStateException();
    	}
    	
    	lastTeam = state.getTurn();
        state.turnProperty().set(getTeam());
        return GameEvent.NONE;
    }

    @Override
    protected void undo(GameState state) {
    	if (!applied) {
    		throw new IllegalStateException();
    	}
    	
        state.turnProperty().set(lastTeam);
    }

}
