package ca.concordia.encs.comp354.controller;

import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Team;
import javafx.beans.property.IntegerProperty;

/**
 * A superclass for game commands.
 * @author Nikita Leonidov
 *
 */
public abstract class GameAction {

    private Team    team;
    private boolean applied;

    public GameAction(Team team) {
        this.team = team;
    }

    public final Team getTeam() {
        return team;
    }
    
    public abstract String getActionText();
    
    public GameEvent apply(GameState state) {
    	applied = true;
    	return doApply(state);
    }
    
    public void undo(GameState state) {
    	if (!applied) {
    		throw new IllegalStateException("action not applied");
    	}
    	applied = false;
    	doUndo(state);
    }
    
    protected abstract GameEvent doApply(GameState state);

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
