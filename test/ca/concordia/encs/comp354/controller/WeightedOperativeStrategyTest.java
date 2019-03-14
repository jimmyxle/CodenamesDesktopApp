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
import ca.concordia.encs.comp354.model.CodenameWord.AssociatedWord;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Keycard;
import ca.concordia.encs.comp354.model.Team;

public class WeightedOperativeStrategyTest {
	
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
	
	@Test
	public void WeightedSearchTest() throws IOException {
		codenameWords = Card.createNonRandomCodenameList(Paths.get("res/operative_test_word_bank"));
		k_card = Keycard.createRandomKeycard();
		
		g_state = new GameState(new Board(codenameWords, k_card));
		board = g_state.boardProperty().get();
		
		s_master = new SpyMaster(Team.RED, new SequentialSpyMasterStrategy());
		o_weighted = new Operative(Team.RED, new WeightedOperativeStrategy());
		
		actual = new HashSet<String>();
		expected = new HashSet<String>();
		expected.add("teacher");
		expected.add("basis");
		expected.add("personality");	
		
		w_actual = new HashSet<Integer>();
		w_expected = new HashSet<Integer>();
		w_expected.add(100);
		w_expected.add(90);
		w_expected.add(60);
	}
	
	@Test
	public void WeightedSuccessfulWordTest()
	{
		clue = new Clue("polluant", 1);
		
		guess = o_weighted.guessCard(g_state, clue);
		
		String result = board.getCard(guess.getX(), guess.getY()).getCodename();
		
		assertEquals("pollution", result);
	}
	
	@Test
	public void WeightedSuccessfulWeightTest()
	{
		clue = new Clue("polluant", 1);
		guess = o_weighted.guessCard(g_state, clue);
		
		for (int i = 0; i < board.getCard(guess.getX(), guess.getY()).getAssociatedWords().size(); i++)
		{
			int result = board.getCard(guess.getX(), guess.getY()).getAssociatedWords().get(i).getWeight();
			
			if (result == 100)
			{
				assertEquals(100, result);
			}
		}
	}
	
	@Test
	public void WeightedSetWordTest()
	{
		clue = new Clue("classroom", 1);
		guess = o_weighted.guessCard(g_state, clue);
		result = board.getCard(guess.getX(), guess.getY()).getCodename();
		actual.add(result);
		
		clue = new Clue("Enrollment", 1);
		guess = o_weighted.guessCard(g_state, clue);
		result = board.getCard(guess.getX(), guess.getY()).getCodename();
		actual.add(result);
		
		clue = new Clue("Dsm", 1);
		guess = o_weighted.guessCard(g_state, clue);
		result = board.getCard(guess.getX(), guess.getY()).getCodename();
		actual.add(result);
		
		assertEquals(actual, expected);
	}

	// TODO: WeightedSetWeightTest(), fix word bank.
	
	
}
