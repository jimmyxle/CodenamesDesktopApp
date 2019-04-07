package ca.concordia.encs.comp354.controller;

import org.junit.Test;
import ca.concordia.encs.comp354.model.GameState;
import static org.junit.Assert.*;


public class HumanOperativeStrategyTest {
	
	/**
	 * 
	 * Author: Alexandre Briere
	 * 
	 */
	
	private GameState model;

	
	/*
	This test ensures that GameState.operativeInputProperty() returns 
	a non null value after GuessCardAction() is called.
    */
	
	@Test(expected=NullPointerException.class)
	public void rejectsOperativeInputPropertyNullValue() {
		model.GuessCardAction();
		assertTrue(model.operativeInputProperty().getValue() != null);
	}

	
	/*
	This test ensures that the Promise returned by guessCard() is finished 
	when the promise returned by GameState.operativeInputProperty() is finished
    */
	
	@Test
	public void guessCardPromiseFinishedWhenInputPropertyPromiseFinished() {
		assertTrue(model.GuessCardAction().getValue().isFinished);
	}
	
}

