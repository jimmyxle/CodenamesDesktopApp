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

    private int redScore;
    private int blueScore;
    
    private boolean applied;
    
    public GuessCardAction(Team team, Board board, Coordinates coords) {
        super(team);
        this.coords = requireNonNull(coords);
        this.card   = board.getCard(coords);
    }

    @Override
    public String getActionText() {
        return getTeam()+" operative guessed: "+card.getCodename();
    }

    @Override
    protected GameEvent apply(GameState state) {
    	if (applied) {
    		throw new IllegalStateException();
    	}
    	
        if (state.getChosenCards().contains(coords)) {
            throw new IllegalStateException("card at "+coords+" has already been chosen");
        }
        
        redScore  = state.getRedScore();
        blueScore = state.getBlueScore();
        
        Board board = state.getBoard();
        Card  card  = board.getCard(coords);
        
        state.chooseCard(coords);
        adjust(state.guessesRemainingProperty(), -1);
        
        switch (card.getValue()) {
        case RED:
            final int redScore = adjust(state.redScoreProperty(), +1);
            if (state.redObjectiveProperty().get() == redScore) {
                return GameEvent.GAME_OVER_RED_WON;
            }
            break;
            
        case BLUE:
            final int blueScore = adjust(state.blueScoreProperty(), +1);
            if (state.blueObjectiveProperty().get() == blueScore) {
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
    protected void undo(GameState state) {
        if (!applied) {
        	throw new IllegalStateException();
        }
        
        state.redScoreProperty().set(redScore);
        state.blueScoreProperty().set(blueScore);
        state.hideCard(coords);
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
