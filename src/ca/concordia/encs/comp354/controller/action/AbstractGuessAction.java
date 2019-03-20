package ca.concordia.encs.comp354.controller.action;

import ca.concordia.encs.comp354.controller.GameEvent;
import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Card;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.controller.Operative;

import static java.util.Objects.requireNonNull;

/**
 * Base class for actions representing an {@link Operative} making a guess.
 * @author Mykyta Leonidov
 *
 */
abstract class AbstractGuessAction extends OperativeAction {

    private int redScore;
    private int blueScore;
    private int guesses;
    
    private String codename = null;
    
    private Coordinates lastCoords;
    
    protected AbstractGuessAction(Operative owner) {
        super(owner);
    }

    @Override
    public String getActionText() {
        return getTeam()+" operative guessed: "+codename;
    }
    
    protected GameEvent doGuess(GameState state, Coordinates coords) {
        if (!state.hasGuesses()) {
            throw new IllegalStateException("no more guesses available");
        }
        
        if (state.getChosenCards().contains(coords)) {
            throw new IllegalStateException("card at "+coords+" has already been chosen");
        }
        
        lastCoords = requireNonNull(coords);
        
        // undo logic is simpler if we just record the values of the properties we intend to modify first
        //--------------------------------------------------------------------------------------------------------------
        redScore  = state.getRedScore();
        blueScore = state.getBlueScore();
        guesses   = state.guessesRemainingProperty().get();
        
        // reveal the value of the card, then modify game state based on that value
        //--------------------------------------------------------------------------------------------------------------
        Board board = state.getBoard();
        Card  card  = board.getCard(coords);
        
        codename = card.getCodename();
        
        state.chooseCard(coords);
        adjust(state.guessesRemainingProperty(), -1);
        
        switch (card.getValue()) {
        case RED:
            // increment red's score, then test victory condition
            final int redScore = adjust(state.redScoreProperty(), +1);
            if (state.redObjectiveProperty().get() == redScore) {
                return GameEvent.GAME_OVER_RED_WON;
            }
            break;
            
        case BLUE:
            // increment blue's score, then test victory condition
            final int blueScore = adjust(state.blueScoreProperty(), +1);
            if (state.blueObjectiveProperty().get() == blueScore) {
                return GameEvent.GAME_OVER_BLUE_WON;
            }
            break;
            
        case ASSASSIN:
            // end the game; the current team loses
        	state.guessesRemainingProperty().set(0);
            return GameEvent.GAME_OVER_ASSASSIN;
            
        case NEUTRAL:
            // end the turn; the current team cannot guess any more times
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
        // reset property values to what they were before the last doApply() call
        state.redScoreProperty().set(redScore);
        state.blueScoreProperty().set(blueScore);
        state.hideCard(lastCoords);
        state.guessesRemainingProperty().set(guesses);
    }

    // boilerplate
    //==================================================================================================================
    // NB: only immutable state is relevant to hashCode() and equals()! the value of the action does not change with 
    // apply() calls
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((codename == null) ? 0 : codename.hashCode());
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
        AbstractGuessAction other = (AbstractGuessAction) obj;
        if (codename == null) {
            if (other.codename != null)
                return false;
        } else if (!codename.equals(other.codename))
            return false;
        return true;
    }
}
