package ca.concordia.encs.comp354.controller;

import org.junit.Test;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.CodenameWord;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Keycard;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HumanOperativeStrategyTest {
	
	/**
	 * 
	 * Author: Alexandre Briere
	 * 
	 */
	
	public HumanOperativeStrategyTest() {
		Keycard keycard = Keycard.createRandomKeycard();
	    List<CodenameWord> words = new ArrayList<>();
	    for (int i=0; i<25; i++) {
	        words.add(new CodenameWord("foo", Arrays.asList(new CodenameWord.AssociatedWord("bar", 1))));
	    }
	    GameState model = new GameState(new Board(words, keycard));
		Operative operative = new Operative(null, null);
	}

	
	/*
	This test ensures that GameState.operativeInputProperty() returns 
	a non null value after GuessCardAction() is called.
    */
	
	@Test(expected=NullPointerException.class)
	public void rejectsOperativeInputPropertyNullValue() {
		HumanOperativeStrategyTest strategy = new HumanOperativeStrategyTest();
		model.GuessCardAction();
		assertTrue(model.operativeInputProperty().getValue() != null);
	}

	
	/*
	This test ensures that the Promise returned by guessCard() is finished 
	when the promise returned by GameState.operativeInputProperty() is finished
    */
	
	@Test
	public void guessCardPromiseFinishedWhenInputPropertyPromiseFinished() {
		HumanOperativeStrategyTest strategy = new HumanOperativeStrategyTest();
		assertTrue(model.GuessCardAction().getValue().isFinished);
	}
	
}

