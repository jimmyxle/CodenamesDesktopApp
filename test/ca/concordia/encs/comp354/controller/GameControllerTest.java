package ca.concordia.encs.comp354.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ca.concordia.encs.comp354.controller.GameController;
import ca.concordia.encs.comp354.controller.Operative;
import ca.concordia.encs.comp354.controller.SequentialOperativeStrategy;
import ca.concordia.encs.comp354.controller.SequentialSpyMasterStrategy;
import ca.concordia.encs.comp354.controller.SpyMaster;
import ca.concordia.encs.comp354.controller.action.ChangeTurnAction;
import ca.concordia.encs.comp354.controller.action.GiveClueAction;
import ca.concordia.encs.comp354.controller.action.GuessCardAction;
import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.CodenameWord;
import ca.concordia.encs.comp354.model.GameAction;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Keycard;
import ca.concordia.encs.comp354.model.Team;

import static org.junit.Assert.*;


/**
 * Test class for {@link GameController}
 * 
 * @author Christopher Idah
 *
 */
public class GameControllerTest {

    final GameState      model;
    final GameController controller;
    
    public GameControllerTest() {
        Keycard keycard = Keycard.createRandomKeycard();
        List<CodenameWord> words = new ArrayList<>();
        for (int i=0; i<25; i++) {
            words.add(new CodenameWord("foo", Arrays.asList(new CodenameWord.AssociatedWord("bar", 1))));
        }
        
        model = new GameState(new Board(words, keycard));
        controller = new GameController.Builder()
                .setRedSpyMaster (new SpyMaster(Team.RED,  new SequentialSpyMasterStrategy()))
                .setBlueSpyMaster(new SpyMaster(Team.BLUE, new SequentialSpyMasterStrategy()))
                .setRedOperative (new Operative(Team.RED,  new SequentialOperativeStrategy()))
                .setBlueOperative(new Operative(Team.BLUE, new SequentialOperativeStrategy()))
                .setInitialTurn(Team.RED)
                .setModel(model)
                .create();
    }
    
    @Test  
    public void initialTurnMatchesBuilder() {
       //Verifies that the constructor does set the right team to go first.
    	//This will be implemented as the first of of the game.
    	
    	controller.advanceTurn();
        assertEquals(Team.RED, model.getTurn());
    }
    
    @Test
    public void firstActionSetsTurn() {
    	
    	//The first move of the game always sets the current player to the default initialTurn.
    	
        assertTrue(model.lastActionProperty().get() instanceof ChangeTurnAction);
    }
    
    
    @Test
    public void redSpyMasterGivesClue() {
    	
    	//As designated, red will go first for the first iteration. 
    	//The first move made by the red team is the clue given by the spyMaster.
    	controller.advanceTurn();
    	final GameAction action = model.lastActionProperty().get();
    	
    	
    	//Verifies that the action was made by the red team and also that it was a clue and not a guess.
    	assertEquals (Team.RED, action.getTeam());
    	assertTrue (action instanceof GiveClueAction);
    	
    }
    
    
    @Test
    public void redOpAfterRedSpy()  {
    	
    	//Two initial moves to give a clue and get a guess as per the game logic.
    	controller.advanceTurn();
    	controller.advanceTurn();
    	
    	final GameAction action = model.lastActionProperty().get();
    	
    	
    	//Verifies that an operative from the red team has made a guess following their spyMaster.
    	assertEquals (Team.RED, action.getTeam());
    	assertTrue (action instanceof GuessCardAction);
    	
    	
    	
    }
    
    @Test 
    public void changeToBlueSpy() {
    	
    	//As  per the first iteration, every team only gets one guess. So the team should change after every guess.
    	//These advances set the turn, give a clue, receive a guess, and then set the turn to the blue team.
    	controller.advanceTurn();
    	controller.advanceTurn();
    	controller.advanceTurn();
    	controller.advanceTurn();
    
    
    	final GameAction action = model.lastActionProperty().get();
    	
    	
    	//Verifies that the last action was indeed the blue spyMaster giving a clue. 
    	assertEquals(Team.BLUE, action.getTeam());
    	assertTrue (action instanceof GiveClueAction);
    	
    }
  
    
    @Test
    public void redGivesWrongGuess() {
    	
    	//Gives a clue and a guess to the set up the scenario.
    	controller.advanceTurn();
    	controller.advanceTurn();
    	final GameAction action = model.lastActionProperty().get();
    	
    	//The guess will be wrong when red picks the assassin card, a blue card or a civilian card.
    	assertTrue (action instanceof GuessCardAction);
    	assertFalse( model.getLastEvent().equals(GameEvent.GAME_OVER_ASSASSIN) | action.getTeam()!=Team.RED);
    	
    }
    
  
    
   
}
