package ca.concordia.encs.comp354.controller;

import org.junit.Test;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.CodenameWord;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Keycard;
import ca.concordia.encs.comp354.model.OperativeEvent;
import ca.concordia.encs.comp354.model.Team;
import ca.concordia.encs.comp354.CompletablePromise;
import ca.concordia.encs.comp354.Promise;
import ca.concordia.encs.comp354.controller.Clue;
import ca.concordia.encs.comp354.controller.action.OperativeAction;
import ca.concordia.encs.comp354.model.SkipEvent;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 
 * @author Alexandre Briere
 * 
 */
public class HumanOperativeStrategyTest {
	
	private GameState model;
	Operative operative = new Operative(Team.RED, new HumanOperativeStrategy());
	
	public HumanOperativeStrategyTest() {
		Keycard keycard = Keycard.createRandomKeycard();
	    List<CodenameWord> words = new ArrayList<>();
	    for (int i=0; i<25; i++) {
	        words.add(new CodenameWord("foo", Arrays.asList(new CodenameWord.AssociatedWord("bar", 1))));
	    }
	    model = new GameState(new Board(words, keycard));
	}

	
	/*
	This test ensures that GameState.operativeInputProperty() returns 
	a non null value after guessCard() is called.
    */
	
	@Test
	public void rejectsOperativeInputPropertyNullValue() {
		operative.guessCard(model, new Clue("hello", 1));
		assertNotNull(model.operativeInputProperty().getValue());
	}

	
	/*
	This test ensures that the strategy produces the promised OperativeEvent when its input
	request is satisfied
    */
	
	@Test
	public void producesActionWhenUserInputGiven() {
	    // promise1 depends on promise2
		Promise<OperativeAction> promise1 = operative.guessCard(model, new Clue("hello", 1));
		CompletablePromise<OperativeEvent> promise2 = model.operativeInputProperty().get();
		
		// neither promise should be finished right now
		assertFalse(promise1.isFinished());
		assertFalse(promise2.isFinished());
		
		// finishing promise2 should finish promise1
		promise2.finish(new SkipEvent());
		assertTrue(promise1.isFinished());
	}
	
}

