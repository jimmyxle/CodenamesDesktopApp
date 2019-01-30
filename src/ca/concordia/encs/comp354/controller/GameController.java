package ca.concordia.encs.comp354.controller;

import ca.concordia.encs.comp354.controller.action.GiveClueAction;
import ca.concordia.encs.comp354.controller.action.GuessCardAction;
import ca.concordia.encs.comp354.model.GameAction;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Team;
import ca.concordia.encs.comp354.view.GameView;

import static java.util.Objects.requireNonNull;

/**
 * The game controller coordinates {@link Player}s and generates {@link GameAction}s. It has
 * a read-write view of the model (the {@link GameState}).
 * @author Nikita Leonidov
 *
 */
public class GameController implements GameView.Controller {

    private GameState model;
    private SpyMaster redSpyMaster;
    private Operative redOperative;
    private SpyMaster blueSpyMaster;
    private Operative blueOperative;
    
    /** is the next player to move the spymaster? */
    private boolean spyMasterNext = true;
    
    public static final class Builder {
        private GameState model;
        private SpyMaster redSpyMaster;
        private Operative redOperative;
        private SpyMaster blueSpyMaster;
        private Operative blueOperative;
       
        
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
            
            ret.model         = requireNonNull(model, "model");
            ret.redSpyMaster  = requireNonNull(redSpyMaster,  "redSpyMaster");
            ret.redOperative  = requireNonNull(redOperative,  "redOperative");
            ret.blueSpyMaster = requireNonNull(blueSpyMaster, "blueSpyMaster");
            ret.blueOperative = requireNonNull(blueOperative, "blueOperative");
            
            return ret;
        }
        
        private static void requireTeam(Player player, Team team) {
            if (player.getTeam()!=team) {
                throw new IllegalArgumentException("player's team must be "+team);
            }
        }
    }

    private GameController() {}
    
    @Override
    public void advanceTurn() {
        // TODO generate a GameAction depending on the current team and player & push it to the model
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
    	if (turn == 0)
    	{
    		model.turnProperty().setValue(Team.RED);
    		spyMasterNext = true;
        	model.pushAction(new GiveClueAction(Team.RED, redSpyMaster.giveClue(model)));
        	spyMasterNext = false;
        	
        	while (guesses < model.lastClueProperty().get().getGuesses() && model.getTurn() == Team.RED && model.lastEventProperty().getValue().isTerminal() == false)
        	{
        		model.pushAction(new GuessCardAction(Team.RED, model.getBoard(), redOperative.guessCard(model, model.lastClueProperty().getValue())));
        		
        		if (model.lastEventProperty().getValue() == GameEvent.END_TURN)
        			break;
        		guesses++;
        	}
        	
        	guesses = 0;
        	turn = 1;
    		
    	}
    	else
    	{
    		model.turnProperty().setValue(Team.BLUE);
        	spyMasterNext = true;
        	
        	model.pushAction(new GiveClueAction(Team.BLUE, blueSpyMaster.giveClue(model)));
        	
        	spyMasterNext = false;
        	
        	
        	while (guesses < model.lastClueProperty().getValue().getGuesses() && model.getTurn() == Team.BLUE && model.lastEventProperty().getValue().isTerminal() == false)
        	{
        		model.pushAction(new GuessCardAction(Team.BLUE, model.getBoard(), blueOperative.guessCard(model, model.lastClueProperty().getValue())));
        		
        		if (model.lastEventProperty().getValue() == GameEvent.END_TURN)
        			break;
        			
        		guesses++;
        	}
        	
        	guesses = 0;
        	turn = 0;
    	}
    	*/
    	
    	if (model.lastEventProperty().get().isTerminal())
    		return;
    	
    	if (spyMasterNext)
    	{
    		SpyMaster currentSpy = model.getTurn().equals(Team.RED)? redSpyMaster : blueSpyMaster; 
    		model.pushAction(new GiveClueAction(Team.RED, currentSpy.giveClue(model)));
    	}
    	
    	else
    	{
    		Operative currentOp = model.getTurn().equals(Team.RED)? redOperative: blueOperative;
    		model.pushAction(new GuessCardAction (model.getTurn(),model.getBoard(), currentOp.guessCard(model, model.lastClueProperty().getValue())));
    		
    		model.turnProperty().setValue(model.getTurn()==Team.RED? Team.BLUE: Team.RED);
    		
    	}
    	
    	spyMasterNext = !spyMasterNext;
    	
    	
    }
}
