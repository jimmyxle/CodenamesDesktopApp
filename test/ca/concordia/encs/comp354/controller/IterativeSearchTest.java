package ca.concordia.encs.comp354.controller;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;


import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Card;
import ca.concordia.encs.comp354.model.CardValue;
import ca.concordia.encs.comp354.model.CodenameWord;
import ca.concordia.encs.comp354.model.CodenameWord.AssociatedWord;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Keycard;
import ca.concordia.encs.comp354.model.Team;





public class IterativeSearchTest {

	final SpyMaster masterSpy;
	final Operative iterative;
	
	final List<CodenameWord> codenameWords;
	final Keycard keycard;
	final GameState state;
	final Board board;
	
	Clue clue; 
	Coordinates guess;
	
	public IterativeSearchTest() throws IOException {

		codenameWords = Card.createRandomCodenameList(Paths.get("res/operative_test_word_bank"));
		keycard = Keycard.createRandomKeycard();

		state = new GameState(new Board(codenameWords, keycard));
		board = state.boardProperty().get();
		
		masterSpy = new SpyMaster(Team.RED, new SpyMasterCountStrategy());
		iterative = new Operative(Team.RED, new IterativeOperativeStrategy());
	}
	
	@Test
	public void iterative_success() {
		
		clue = masterSpy.giveClue(state);
		guess = iterative.guessCard(state, clue);
		
		assertEquals("supermarket", board.getCard(guess.getX(), guess.getY()).getCodename() );
	}
	
	@Test
	public void iterative_fail() {
		clue = masterSpy.giveClue(state);
		guess = iterative.guessCard(state, clue);

		assertThat("pollution", not(board.getCard(guess.getX(), guess.getY()).getCodename()) );
	}

}
