package ca.concordia.encs.comp354.controller.action;

import ca.concordia.encs.comp354.controller.GameEvent;
import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Card;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.GameAction;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Team;
import ca.concordia.encs.comp354.controller.Operative;

import static java.util.Objects.requireNonNull;

/**
 * Represents an {@link Operative} making a guess.
 * @author Nikita Leonidov
 *
 */
public class GuessCardAction extends GameAction {

    private Coordinates coords;
    private Card        card;

    public GuessCardAction(Team team, Board board, Coordinates coords) {
        super(team);
        this.coords = requireNonNull(coords);
        this.card   = board.getCard(coords);
    }

    @Override
    public String getActionText() {
        return "operative guessed: "+card.getCodename();
    }

    @Override
    protected GameEvent apply(GameState state) {
        if (state.getChosenCards().contains(coords)) {
            throw new IllegalStateException("card at "+coords+" has already been chosen");
        }
        
        Board board = state.getBoard();
        Card  card  = board.getCard(coords);
        
        state.chooseCard(coords);
        
        switch (card.getValue()) {
        case RED:
            state.redScoreProperty().set(state.redScoreProperty().get()+1);
            if (state.redObjectiveProperty().get() == state.redScoreProperty().get()) {
                return GameEvent.GAME_OVER_RED_WON;
            }
            break;
            
        case BLUE:
            state.blueScoreProperty().set(state.blueScoreProperty().get()+1);
            if (state.blueObjectiveProperty().get() == state.blueScoreProperty().get()) {
                return GameEvent.GAME_OVER_BLUE_WON;
            }
            break;
            
        case ASSASSIN:
            return GameEvent.GAME_OVER_ASSASSIN;
            
        case NEUTRAL:
            return GameEvent.END_TURN;
        }
        
        return card.getValue() != state.getTurn().getValue() ? GameEvent.END_TURN : GameEvent.NONE;
    }

    @Override
    protected void undo(GameState gameState) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((card == null) ? 0 : card.hashCode());
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
        if (card == null) {
            if (other.card != null)
                return false;
        } else if (!card.equals(other.card))
            return false;
        if (coords == null) {
            if (other.coords != null)
                return false;
        } else if (!coords.equals(other.coords))
            return false;
        return true;
    }
    
    

}
