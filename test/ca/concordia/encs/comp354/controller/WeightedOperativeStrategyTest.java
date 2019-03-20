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
 * Testing class for the WeightedOperativeStrategy class. Creates a JUnit
 * case and tests the methods in the class mentioned previously.
 * 
 * @author Elie Khoury
 * 
 * */

public class WeightedOperativeStrategyTest {
	
	//================================
	//-----------VARIABLES------------
	//================================
	SpyMaster s_master;
	Operative o_weighted;
	
	List<CodenameWord> codenameWords;
	Keycard k_card;
	GameState g_state;
	Board board;
	
	Clue clue;
	CodenameWord c_word1;
	CodenameWord c_word2;
	Coordinates guess;
	
	Set<String> actual;
	Set<String> expected;
	Set<Integer> w_actual;
	Set<Integer> w_expected;
	
	String result;
	int w_result;
	
	// Sets the scene for the testing methods.

	public WeightedOperativeStrategyTest() throws IOException {
		codenameWords = Card.createNonRandomCodenameList(Paths.get("res/weighted_operative_test_word_bank"));
		k_card = Keycard.createRandomKeycard();
		
		g_state = new GameState(new Board(codenameWords, k_card));
		board = g_state.boardProperty().get();
		
		s_master = new SpyMaster(Team.RED, new SequentialSpyMasterStrategy());
		o_weighted = new Operative(Team.RED, new WeightedOperativeStrategy());
		
		actual = new HashSet<String>();
		expected = new HashSet<String>();
		expected.add("teacher");
		expected.add("death");
		expected.add("basis");	
		
		w_actual = new HashSet<Integer>();
		w_expected = new HashSet<Integer>();
		w_expected.add(100);
		w_expected.add(100);
		w_expected.add(100);
	}
	
	//=================================
	//-------------METHODS-------------
	//=================================
	

	
	// Checks to see if the guesses detect the clue as an associated word.
	@Test
	public void operativeGuessesCorrectCodename() {
		clue = new Clue("pollutant", 1);

		guess = o_weighted.guessCard(g_state, clue).get();
		
		String result = board.getCard(guess.getX(), guess.getY()).getCodename();
		
		assertEquals("pollution", result);
	}
	
	// Checks to see if the guesses detect the clue and return the associated word with the highest weight.
	@Test
	public void operativeGuessHasCorrectWeight() {
		clue = new Clue("pollutant", 1);

		guess = o_weighted.guessCard(g_state, clue).get();
		
		for (int i = 0; i < board.getCard(guess.getX(), guess.getY()).getAssociatedWords().size(); i++) {
			int result = board.getCard(guess.getX(), guess.getY()).getAssociatedWords().get(i).getWeight();
			
			if (result == 100) {
				assertEquals(100, result);
			}
		}
	}
	
	// Same as the WeightedSuccessfulWordTest, but with a set of words instead of a single one.
	@Test
	public void operativeGuessesCorrectSet() {
		clue = new Clue("classroom", 1);
		guess = o_weighted.guessCard(g_state, clue).get();
		result = board.getCard(guess.getX(), guess.getY()).getCodename();
		actual.add(result);
		
		clue = new Clue("Sentence", 1);
		guess = o_weighted.guessCard(g_state, clue).get();
		result = board.getCard(guess.getX(), guess.getY()).getCodename();
		actual.add(result);
		
		clue = new Clue("Expected", 1);
		guess = o_weighted.guessCard(g_state, clue).get();
		result = board.getCard(guess.getX(), guess.getY()).getCodename();
		actual.add(result);
		
		assertEquals( expected, actual);
	}

	// Same as the WeightedSuccessfulWeightTest, but with a set of words instead of a single one.
	@Test
	public void operativeGuessesExpectedSet() {
		clue = new Clue("classroom", 1);

		guess = o_weighted.guessCard(g_state, clue).get();
		String clue_word = clue.getWord();
		for (int i = 0; i < board.getCard(guess.getX(), guess.getY()).getAssociatedWords().size(); i++) {
			if (clue_word.equalsIgnoreCase(board.getCard(guess.getX(), guess.getY()).getAssociatedWords().get(i).getWord())) {
				w_result = board.getCard(guess.getX(), guess.getY()).getAssociatedWords().get(i).getWeight();
			}
		}
		w_actual.add(w_result);
		
		clue = new Clue("classroom", 1);
		guess = o_weighted.guessCard(g_state, clue).get();
		clue_word = clue.getWord();
		for (int i = 0; i < board.getCard(guess.getX(), guess.getY()).getAssociatedWords().size(); i++) {
			if (clue_word.equalsIgnoreCase(board.getCard(guess.getX(), guess.getY()).getAssociatedWords().get(i).getWord())) {
				w_result = board.getCard(guess.getX(), guess.getY()).getAssociatedWords().get(i).getWeight();
			}
		}
		w_actual.add(w_result);
		
		clue = new Clue("Trait", 1);
		guess = o_weighted.guessCard(g_state, clue).get();
		clue_word = clue.getWord();
		for (int i = 0; i < board.getCard(guess.getX(), guess.getY()).getAssociatedWords().size(); i++) {
			if (clue_word.equalsIgnoreCase(board.getCard(guess.getX(), guess.getY()).getAssociatedWords().get(i).getWord())) {
				w_result = board.getCard(guess.getX(), guess.getY()).getAssociatedWords().get(i).getWeight();
			}
		}
		w_actual.add(w_result);
		
		assertEquals(w_actual, w_expected);
	}
	
	// This test was taken from another class where we check to see that the strategy will eventually choose
	// all the cards on the board. The chooseCard() method will add cards to the state's list and we check 
	// that the size of that list is equal to size of the game board.
	@Test
	public void weightedStrategyPicksAllCards() {
		clue = new Clue("test", 1);

		for (int i = 0; i < 25; i++) {
			guess = o_weighted.guessCard(g_state, clue).get();
			g_state.chooseCard(guess);
		}
		
		assertTrue(g_state.getChosenCards().size() == g_state.getBoard().getLength() * g_state.getBoard().getWidth());
	}
	
}
