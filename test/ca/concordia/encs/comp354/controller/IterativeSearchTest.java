package ca.concordia.encs.comp354.controller;

import static org.junit.Assert.*;
import org.junit.Test;


import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Card;
import ca.concordia.encs.comp354.model.CardValue;
import ca.concordia.encs.comp354.model.CodenameWord;
import ca.concordia.encs.comp354.model.CodenameWord.AssociatedWord;
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
	
	public IterativeSearchTest() throws IOException {

		codenameWords = Card.createRandomCodenameList(Paths.get("res/25wordswithcommonassociatedwords.txt"));
		keycard = Keycard.createRandomKeycard();

		state = new GameState(new Board(codenameWords, keycard));
		board = state.boardProperty().get();
		
		
		
		masterSpy = new SpyMaster(Team.RED, new SequentialSpyMasterStrategy());
		iterative = new Operative(Team.RED, new IterativeOperativeStrategy());
	}
	
	@Test
	public void test1() {
		
		
		    
		Clue clue = masterSpy.giveClue(state);
		
		
		assertEquals(1, 1);
		//fail("Not yet implemented");
	}
	
	@Test
	public void test2() {
		assertEquals(1, 1);
		//fail("Not yet implemented");
	}

}
