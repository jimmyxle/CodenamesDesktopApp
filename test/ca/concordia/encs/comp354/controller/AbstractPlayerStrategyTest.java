package ca.concordia.encs.comp354.controller;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Card;
import ca.concordia.encs.comp354.model.CardValue;
import ca.concordia.encs.comp354.model.CodenameWord;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Keycard;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class AbstractPlayerStrategyTest {
	
	/**
	 * 
	 * Author: Alexandre Briere
	 * 
	 */
	public class isValidGuessReturnsFalse extends AbstractPlayerStrategy{

		protected boolean isValidGuess(Player owner, Board board, int x, int y) {
			return false;
		}
	}
	public class isValidGuessReturnsTrue extends AbstractPlayerStrategy{

		protected boolean isValidGuess(Player owner, Board board, int x, int y) {
			return true;
		}
	}
	
	public AbstractPlayerStrategyTest() {
		Keycard keycard = Keycard.createRandomKeycard();
	    List<CodenameWord> words = new ArrayList<>();
	    for (int i=0; i<25; i++) {
	        words.add(new CodenameWord("foo", Arrays.asList(new CodenameWord.AssociatedWord("bar", 1))));
	    }
	    GameState model = new GameState(new Board(words, keycard));
		Operative operative = new Operative(null, null);
	}
	/*
	This test ensures that beginTurn() returns an 
	empty list when isValidGuess() returns strictly false
    */
	
	@Test
	public void beginTurnReturnsEmptyListWhenGuessInvalid() {
		AbstractPlayerStrategyTest strategy = new AbstractPlayerStrategyTest();
		assertTrue(beginTurn(operative, model).setValue(List).isEmpty());
		}
	}

