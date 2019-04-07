package ca.concordia.encs.comp354.controller;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Card;
import ca.concordia.encs.comp354.model.CardValue;
import ca.concordia.encs.comp354.model.GameState;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class AbstractPlayerStrategyTest extends AbstractPlayerStrategy{
	
	/**
	 * 
	 * Author: Alexandre Briere
	 * 
	 */
	
	private CardValue x;
	private GameState model;
	private Operative operative;

	@Override
	protected boolean isValidGuess(Player owner, Board board, int x, int y) {
		return false;
	}
	
	/*
	This test ensures that beginTurn() returns an 
	empty list when isValidGuess() returns strictly false
    */
	
	@Test
	public void beginTurnReturnsEmptyListWhenGuessInvalid() {
		Board lastBoard = null;
		if (isValidGuess(null, lastBoard, 0, 0) == false) {
			List list = new ArrayList();
			assertTrue(beginTurn(operative, model) == list);
		}
		
	}
	
	/*
	This test ensures that beginTurn() returns an 
	empty list when all cards are marked
    */
	
	@Test
	public void beginTurnReturnsEmptyListWhenAllCardsMarked() {
		List list = new ArrayList();
		assertTrue(beginTurn(operative, model) == list);
	}
	
	/*
	This test ensures that beginTurn() does not return
	marked cards
    */
	
	@Test(expected=IllegalArgumentException.class)
	public void rejectsBeginTurnMarkedCardValue() {
		assertTrue(beginTurn(operative, model) != Card(null, x));
	}

}
