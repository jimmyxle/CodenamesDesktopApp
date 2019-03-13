package ca.concordia.encs.comp354.controller.action;

import ca.concordia.encs.comp354.controller.Clue;
import ca.concordia.encs.comp354.controller.GameEvent;
import ca.concordia.encs.comp354.controller.GameAction;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Team;

/**
 * Represents the start of the next team's turn.
 * @author Mykyta Leonidov
 *
 */
public class ChangeTurnAction extends GameAction {

	private Team lastTeam;
	private Clue lastClue;
	
    public ChangeTurnAction(Team nextTeam) {
        super(nextTeam);
    }
    
    @Override
    public String getActionText() {
        return String.format("It is now %s's turn", getTeam());
    }
    
    @Override
    protected GameEvent doApply(GameState state) {
        // undo logic is simpler if we just record the values of the properties we intend to modify first
    	lastTeam = state.getTurn();
    	lastClue = state.lastClueProperty().get();
    	
    	// update team, clear clue (this lets the controller detect when the spymaster must take his turn)
        state.turnProperty().set(getTeam());
        state.lastClueProperty().set(null);
        
        return GameEvent.NONE;
    }

    @Override
    protected void doUndo(GameState state) {
        // reset modified properties to pre-apply() state
        state.turnProperty().set(lastTeam);
        state.lastClueProperty().set(lastClue);
    }

}
