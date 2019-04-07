package ca.concordia.encs.comp354.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.CodenameWord;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Keycard;
import ca.concordia.encs.comp354.model.Team;
import ca.concordia.encs.comp354.controller.Clue;

public class ClueTest {

	/**
	 * 
	 * Author: Yaacov Cohen 
	 * 
	 */
	
	 final GameState      model;
	 final GameController controller;
	 Clue 			      clue;
	 private int x; 
		    
	 public ClueTest() {
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
		    

	/* 
	This test ensures that the guess provided by the spymaster is not the 
	empty string, and therefore, must be a sequence of characters.
    */
	@Test(expected=AssertionError.class)
	public void clueConstructorFailsForNullClue() {
		clue= new Clue ("", 1);
		if (clue.getWord()=="") {
			throw new AssertionError("The guess must be a word or character");
		}
		assertTrue(clue.getWord() != "");
	}
	
	/*This test ensures that the number of guesses provided by the spymaster 
	 is greater or equal to one. Any value less than zero should throw an error. 
	*/
	@Test(expected=IllegalArgumentException.class)
	public void clueConstructorFailsForGuessCountLessThanOne() {
		clue= new Clue ("coby", -1);
		if (clue.getGuesses()<1) {
			throw new IllegalArgumentException("guesses must be greater than 0");
		}
		assertTrue(clue.getGuesses()>=1);
	}
	
}
	

