package ca.concordia.encs.comp354.controller;

import static org.junit.Assert.*;
import org.junit.Test;


import java.io.IOException;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Card;
import ca.concordia.encs.comp354.model.CodenameWord;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Keycard;
import ca.concordia.encs.comp354.model.Team;


/**
 * Tests for the iterative search strategy
 * @author Jimmy Le
 */




public class IterativeSearchTest {

	final SpyMaster masterSpy;
	final Operative iterative;
	
	final List<CodenameWord> codenameWords;
	final Keycard keycard;
	final GameState state;
	final Board board;
	
	Clue clue; 
	Coordinates guess;
	
	Set<String> actual;
	Set<String> expected;
	
	String result;
	
	/*
	 * Initialize all relevant variables 
	 */
	public IterativeSearchTest() throws IOException {

		codenameWords = Card.createNonRandomCodenameList(Paths.get("res/operative_test_word_bank"));
		keycard = Keycard.createRandomKeycard();

		state = new GameState(new Board(codenameWords, keycard));
		board = state.boardProperty().get();
		
		masterSpy = new SpyMaster(Team.RED, new SequentialSpyMasterStrategy());
		iterative = new Operative(Team.RED, new IterativeOperativeStrategy());
		
		actual = new HashSet<String>(); 
		expected = new HashSet<String>();
		expected.add("teacher");
		expected.add("basis");
		expected.add("personality");
	}
	
	/*
	 * 	This test uses the dummmy text file that contains a smaller set of words and associated words. We're looking for the 
	 *  codename "pollution" by giving the clue "pollutant". It should return true.
	 */
	@Test
	public void iterativeSuccessTest() {
		
		clue = new Clue("pollutant", 1);
		guess = iterative.guessCard(state, clue);
		
		String result = board.getCard(guess.getX(), guess.getY()).getCodename() ;

		assertEquals("pollution", result );
	}
	
	/*
	 * This test checks that the strategy will search for three different hints for three different codenames. 
	 * There is an expected set of words and this test will produce an set of actual results. Then check if the
	 * sets are equal to each other.
	 */
	@Test
	public void iterativeSetTest() {
		clue = new Clue("classroom", 1);
		guess = iterative.guessCard(state, clue);	
		result = board.getCard(guess.getX(), guess.getY()).getCodename() ;
		actual.add(result);
		
		clue = new Clue("Enrollment", 1);
		guess = iterative.guessCard(state, clue);
		result = board.getCard(guess.getX(), guess.getY()).getCodename() ;
		actual.add(result);


		clue = new Clue("Dsm", 1);
		guess = iterative.guessCard(state, clue);
		result = board.getCard(guess.getX(), guess.getY()).getCodename() ;
		actual.add(result);

		assertEquals(actual, expected);
	}
	
	/*
	 * This test was taken from another class where we check to see that the strategy will eventually choose
	 * all the cards on the board. The chooseCard() method will add cards to the state's list and we check 
	 * that the size of that list is equal to size of the game board.
	 */
	@Test
	public void iterativeStrategyPicksAllCards() {
		clue = new Clue("test", 1);
		for (int i = 0; i < 25; i++) {
			Coordinates guess = iterative.guessCard(state, clue);
			state.chooseCard(guess);
		}
		
		assertTrue(state.getChosenCards().size() == state.getBoard().getLength() * state.getBoard().getWidth());
	}
}
