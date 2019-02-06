package ca.concordia.encs.comp354.model;

import ca.concordia.encs.comp354.controller.GameEvent;

/**
 * A superclass for game commands.
 * @author Nikita Leonidov
 *
 */
public abstract class GameAction {

    private Team team;

    public GameAction(Team team) {
        this.team = team;
    }

    public final Team getTeam() {
        return team;
    }
    
    public abstract String getActionText();
    
    protected abstract GameEvent apply(GameState state);

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
