package ca.concordia.encs.comp354.controller;

import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Team;
import javafx.beans.property.IntegerProperty;

/**
 * A superclass for game commands.
 * @author Mykyta Leonidov
 *
 */
public abstract class GameAction {

    private enum HistoryState {
        NEW,
        DONE,
        UNDONE
    }
    
    private Team         team;
    private HistoryState historyState = HistoryState.NEW;

    public GameAction(Team team) {
        this.team = team;
    }

    public final Team getTeam() {
        return team;
    }
    
    public abstract String getActionText();
    
    public GameEvent apply(GameState state) {
        GameEvent ret;
        switch (historyState) {
            case NEW:
                ret = doApply(state);
                break;
            case UNDONE:
                ret = doRedo(state);
                break;
            default:
                throw new IllegalStateException("cannot re-apply action");
        }
        
        // set this iff the action didn't throw an exception
        historyState = HistoryState.DONE;
        
        return ret;
    }
    
    public void undo(GameState state) {
        if (historyState != HistoryState.DONE) {
            throw new IllegalStateException("cannot undo action that has not been applied");
        }
        
        doUndo(state);
        
        // set this iff the action didn't throw an exception
        historyState = HistoryState.UNDONE;
    }
    
    protected abstract GameEvent doApply(GameState state);
    
    /**
     * Override this if redo behaviour differs from apply behaviour.
     * @param state the game state on which to operate
     * @return the result of the action
     */
    protected GameEvent doRedo(GameState state) {
        return doApply(state);
    }

    protected abstract void doUndo(GameState state);
    
    /**
     * Convenience method to add an integer to the value of an integer property and return the value
     * @param prop   property to adjust by <tt>delta</tt>
     * @param delta  the desired change in <tt>prop</tt>'s value
     * @return the sum <tt>prop.getValue() + delta</tt>
     */
    protected static int adjust(IntegerProperty prop, int delta) {
        prop.set(prop.get() + delta);
        return prop.get();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((team == null) ? 0 : team.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GameAction other = (GameAction) obj;
        if (team != other.team)
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return getTeam()+" "+getActionText();
    }
    
}
