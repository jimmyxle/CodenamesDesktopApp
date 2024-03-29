package ca.concordia.encs.comp354.model.action;

import ca.concordia.encs.comp354.controller.Clue;
import ca.concordia.encs.comp354.controller.GameEvent;
import ca.concordia.encs.comp354.model.GameAction;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.controller.SpyMaster;

/**
 * Represents a {@link SpyMaster} giving a clue.
 * @author Mykyta Leonidov
 *
 */
public final class GiveClueAction extends GameAction {

    private Clue clue;
    
    private Clue lastClue;
    private int  lastGuesses;

    public GiveClueAction(SpyMaster owner, Clue clue) {
        super(owner.getTeam());
        this.clue = clue;
    }

    @Override
    public String getActionText() {
        return getTeam()+" spymaster gave a clue: "+clue.getWord()+" "+clue.getGuesses();
    }

    @Override
    protected GameEvent doApply(GameState state) {
        // undo logic is simpler if we just record the values of the properties we intend to modify first
    	lastClue    = state.lastClueProperty().get();
    	lastGuesses = state.guessesRemainingProperty().get();
    	
    	// update the model with our new clue
        state.lastClueProperty().set(clue);
        state.guessesRemainingProperty().set(clue.getGuesses());
        return GameEvent.NONE;
    }

    @Override
    protected void doUndo(GameState state) {
        // reset modified properties to pre-apply() state
        state.lastClueProperty().set(lastClue);
        state.guessesRemainingProperty().set(lastGuesses);
    }

    public Clue getClue() {
        return clue;
    }

    // boilerplate
    //==================================================================================================================
    // NB: only immutable state is relevant to hashCode() and equals()! the value of the action does not change with 
    // apply() calls
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((clue == null) ? 0 : clue.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        GiveClueAction other = (GiveClueAction) obj;
        if (clue == null) {
            if (other.clue != null)
                return false;
        } else if (!clue.equals(other.clue))
            return false;
        return true;
    }

    
}
