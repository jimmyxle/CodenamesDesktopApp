package ca.concordia.encs.comp354.controller;

import ca.concordia.encs.comp354.controller.action.ChangeTurnAction;
import ca.concordia.encs.comp354.controller.GameAction;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Team;
import ca.concordia.encs.comp354.view.GameView;

import static java.util.Objects.requireNonNull;

/**
 * <p>The game controller coordinates {@link Player}s and generates {@link GameAction}s. It has a read-write view of the 
 * model (the {@link GameState}). The responsibility of the controller is to receive input from the user interface and
 * execute the game loop.
 * 
 * <p>The controller's logic is itself fairly minimal; {@link GameAction}s represent units of game logic - commands - 
 * which may be applied, undone, or inspected to reveal the game history. The {@link GameState} serves as the command
 * manager.
 * 
 * @author Mykyta Leonidov
 * @author Christopher Idah
 */
public class GameController implements GameView.Controller {

    // these fields should be immutable once set by the Builder
    //------------------------------------------------------------------------------------------------------------------
    private GameState model;
    private SpyMaster redSpyMaster;
    private Operative redOperative;
    private SpyMaster blueSpyMaster;
    private Operative blueOperative;
    
    public Team initialTurn;
    
    public static final class Builder {
        private GameState model;
        private SpyMaster redSpyMaster;
        private Operative redOperative;
        private SpyMaster blueSpyMaster;
        private Operative blueOperative;
        
        private Team initialTurn = Team.RED;
       
        public Builder setInitialTurn(Team team) {
            initialTurn  = requireNonNull(team);
            return this;
        }
        
        public Builder setModel(GameState value) {
            model = value;
            return this;
        }
        
        public Builder setRedSpyMaster(SpyMaster value) {
            requireTeam(value, Team.RED);
            redSpyMaster = value;
            return this;
        }
        
        public Builder setRedOperative(Operative value) {
            requireTeam(value, Team.RED);
            redOperative = value;
            return this;
        }
        
        public Builder setBlueSpyMaster(SpyMaster value) {
            requireTeam(value, Team.BLUE);
            blueSpyMaster = value;
            return this;
        }
        
        public Builder setBlueOperative(Operative value) {
            requireTeam(value, Team.BLUE);
            blueOperative = value;
            return this;
        }
        
        public GameController create() {
            GameController ret = new GameController();
            
            ret.model         = requireNonNull(model,         "model");
            ret.redSpyMaster  = requireNonNull(redSpyMaster,  "redSpyMaster");
            ret.redOperative  = requireNonNull(redOperative,  "redOperative");
            ret.blueSpyMaster = requireNonNull(blueSpyMaster, "blueSpyMaster");
            ret.blueOperative = requireNonNull(blueOperative, "blueOperative");
            ret.initialTurn   = initialTurn;
            
            ret.initialize();
            
            return ret;
        }
        
        private static void requireTeam(Player player, Team team) {
            if (player.getTeam()!=team) {
                throw new IllegalArgumentException("player's team must be "+team);
            }
        }
    }

    private GameController() {
    }
    
    private void initialize() {
        model.pushAction(new ChangeTurnAction(initialTurn));
    }
    
    @Override
    public void advanceTurn() {
        /* NB: before proceeding, read:
         *   - ca.concordia.encs.comp354.model.GameState
         *   - ca.concordia.encs.comp354.controller.action.GiveClueAction
         *   - ca.concordia.encs.comp354.controller.action.GuessCardAction
         *   - ca.concordia.encs.comp354.controller.Operative
         *   - ca.concordia.encs.comp354.controller.SpyMaster
         * 
         * turn logic should be something like this:
         * 
         * if the most recent GameEvent stored in the model returns isTerminal() == false, do nothing;
         * otherwise, 
         * given the current team:
         *   - if it's the spymaster's turn, push a GiveClueAction to the model
         *   - if it's the operative's turn, the number of guesses is less than the number in the last
         *     Clue, push a GuessCardAction to the model
         * lastly:
         *   - if the last GameEvent is non-terminal and either the team is out of guesses, or the
         *   last GameEvent is END_TURN (indicating that the turn ended prematurely), advance to the 
         *   next team's turn
         */
    	
    	/*
    	 * red turn
    	 * - spymaster: x, y
    	 * - operative guess 1
    	 * - operative guess 2
    	 * ...
    	 * - operative guess y
    	 * 
    	 */
    	if (model.lastActionProperty().get()==null) {
    	    initialize();
    	    return;
    	}
    	
    	if (model.getLastEvent().isTerminal()) {
    		return;
    	}
    	
    	final Team turn = model.getTurn();
    	
    	// if there's no clue, it's the current spymaster's turn
    	if (model.lastClueProperty().get()==null) {
    		SpyMaster currentSpy = turn==Team.RED? redSpyMaster : blueSpyMaster; 
    		model.pushAction(currentSpy.giveClue(model));
    	} else {
	        // advance to next turn iff guesses remain & last action did not end game
    		if (!model.hasGuesses() && !model.getLastEvent().isTerminal()) {
    			model.pushAction(new ChangeTurnAction(turn==Team.RED? Team.BLUE: Team.RED));
    		} else {
    		    // otherwise, let the current operative make another guess
        		Operative currentOp = turn.equals(Team.RED)? redOperative: blueOperative;
        		model.pushAction(currentOp.guessCard(model, model.lastClueProperty().get()));
    		}
    	}	
    }
    
    @Override
    public boolean undoTurn() {
    	return model.undoAction();
    }

	@Override
	public boolean redoTurn() {
		return model.redoAction();
	}
	

	public void restartGame() {
		System.out.println("restart pressed!");
		
		//call GameState to reset
		model.reset();
		
	}
}