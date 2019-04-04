package ca.concordia.encs.comp354.controller.action;

import ca.concordia.encs.comp354.Promise;
import ca.concordia.encs.comp354.controller.GameEvent;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.controller.Operative;

import static java.util.Objects.requireNonNull;

/**
 * Represents an {@link Operative} making a guess.
 * @author Mykyta Leonidov
 *
 */
public final class GuessCardAction extends AbstractGuessAction {

    private Coordinates coords;
    
    public GuessCardAction(Operative owner, Coordinates coords) {
        super(owner);
        this.coords = requireNonNull(coords);
    }

    public Coordinates getCoordinates() {
        return coords;
    }

    @Override
    protected Promise<GameEvent> doApply(GameState state) {
        return Promise.finished(super.doGuess(state, coords));
    }

    // boilerplate
    //==================================================================================================================
    // NB: only immutable state is relevant to hashCode() and equals()! the value of the action does not change with 
    // apply() calls
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((coords == null) ? 0 : coords.hashCode());
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
        GuessCardAction other = (GuessCardAction) obj;
        if (coords == null) {
            if (other.coords != null)
                return false;
        } else if (!coords.equals(other.coords))
            return false;
        return true;
    }
}
