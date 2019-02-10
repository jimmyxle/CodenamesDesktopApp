package ca.concordia.encs.comp354.controller.action;

import ca.concordia.encs.comp354.controller.GameAction;
import ca.concordia.encs.comp354.controller.GameEvent;
import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Card;
import ca.concordia.encs.comp354.model.Coordinates;
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

    private int redScore;
    private int blueScore;
    private int guesses;
    
    private String codename = null;
    
    public GuessCardAction(Team team, Coordinates coords) {
        super(team);
        this.coords = requireNonNull(coords);
    }

    @Override
    public String getActionText() {
        return getTeam()+" operative guessed: "+codename;
    }

    @Override
    protected GameEvent doApply(GameState state) {
        if (state.getChosenCards().contains(coords)) {
            throw new IllegalStateException("card at "+coords+" has already been chosen");
        }
        
        if (!state.hasGuesses()) {
            throw new IllegalStateException("no more guesses remain");
        }
        
        redScore  = state.getRedScore();
        blueScore = state.getBlueScore();
        guesses   = state.guessesRemainingProperty().get();
        
        Board board = state.getBoard();
        Card  card  = board.getCard(coords);
        
        codename = card.getCodename();
        
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
        	state.guessesRemainingProperty().set(0);
            return GameEvent.GAME_OVER_ASSASSIN;
            
        case NEUTRAL:
        	state.guessesRemainingProperty().set(0);
            return GameEvent.END_TURN;
        }
        
        if (card.getValue() != state.getTurn().getValue()) {
            state.guessesRemainingProperty().set(0);
            return GameEvent.END_TURN;
        } else {
            return GameEvent.NONE;
        }
    }

    @Override
    protected void doUndo(GameState state) {
        state.redScoreProperty().set(redScore);
        state.blueScoreProperty().set(blueScore);
        state.hideCard(coords);
        state.guessesRemainingProperty().set(guesses);
        codename = null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((codename == null) ? 0 : codename.hashCode());
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
        if (codename == null) {
            if (other.codename != null)
                return false;
        } else if (!codename.equals(other.codename))
            return false;
        if (coords == null) {
            if (other.coords != null)
                return false;
        } else if (!coords.equals(other.coords))
            return false;
        return true;
    }
    
    

}
