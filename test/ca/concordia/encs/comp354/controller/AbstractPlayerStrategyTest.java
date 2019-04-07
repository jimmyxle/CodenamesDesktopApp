package ca.concordia.encs.comp354.controller;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.CodenameWord;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Keycard;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.controller.AbstractPlayerStrategy;

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

	private GameState model;
	
	public AbstractPlayerStrategyTest() {
		Keycard keycard = Keycard.createRandomKeycard();
	    List<CodenameWord> words = new ArrayList<>();
	    for (int i=0; i<25; i++) {
	        words.add(new CodenameWord("foo", Arrays.asList(new CodenameWord.AssociatedWord("bar", 1))));
	    }
	    model = new GameState(new Board(words, keycard));
	}
	/*
	This test ensures that beginTurn() returns an 
	empty list when isValidGuess() returns strictly false
    */
	
	@Test
	public void beginTurnReturnsEmptyListWhenGuessInvalid() {
		assertTrue(new isValidGuessReturnsFalse().beginTurn(null, model).isEmpty());
		}
	
	/*
	This test ensures that beginTurn() does not return marked cards
    */
	@Test
	public void beginTurnDoesNotReturnMarkedCards() {
		Coordinates coords = new Coordinates(1, 1);
		model.chooseCard(coords);
		assertFalse(new isValidGuessReturnsTrue().beginTurn(null, model).contains(coords));
		}
	
	/*
	This test ensures that beginTurn() returns an empty list when 
	all cards are marked
    */
	@Test
	public void beginTurnReturnsEmptyListWhenAllCardsMarked() {
		Board board = model.boardProperty().get(); 
		for(int i=0; i<board.getWidth(); i++) {
			for(int j=0; j<board.getLength(); j++) {
				Coordinates coords = new Coordinates(i, j);
				model.chooseCard(coords);
			}
		}
		assertTrue(new isValidGuessReturnsTrue().beginTurn(null, model).isEmpty());
		}
	}

