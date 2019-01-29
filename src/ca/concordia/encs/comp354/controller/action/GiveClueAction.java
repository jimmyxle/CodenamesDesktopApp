package ca.concordia.encs.comp354.controller.action;

import ca.concordia.encs.comp354.controller.GameEvent;
import ca.concordia.encs.comp354.model.GameAction;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Team;
import ca.concordia.encs.comp354.controller.SpyMaster;

/**
 * Representing a {@link SpyMaster} giving a clue.
 * @author Nikita Leonidov
 *
 */
public class GiveClueAction extends GameAction {

    private String clue;

    public GiveClueAction(Team team, String clue) {
        super(team);
        this.clue = clue;
    }

    @Override
    public String getActionText() {
        return "spymaster gave a clue: "+clue;
    }

    @Override
    protected GameEvent apply(GameState state) {
        // clues don't do anything!
        return GameEvent.NONE;
    }

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
