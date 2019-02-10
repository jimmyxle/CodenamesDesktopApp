package ca.concordia.encs.comp354.controller;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Card;
import ca.concordia.encs.comp354.model.CardValue;
import ca.concordia.encs.comp354.model.CodenameWord;
import ca.concordia.encs.comp354.model.CodenameWord.AssociatedWord;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Keycard;
import ca.concordia.encs.comp354.model.Team;


public class SpyMasterTest extends AbstractPlayerTest {
	
	final SpyMaster seqSpy;
	final SpyMaster randSpy;
	final GameState model;
	
	
	public SpyMasterTest() throws IOException {
		List<CodenameWord> codenameWords = Card.createRandomCodenameList(Paths.get("res/words.txt"));
		Keycard keycard = Keycard.createRandomKeycard();
		
		model = new GameState(new Board(codenameWords, keycard));
		seqSpy = new SpyMaster(Team.RED, new SequentialSpyMasterStrategy());
		randSpy = new SpyMaster(Team.BLUE, new RandomSpyMasterStrategy());
	}
	
	@Test
	public void guessAllCluesSeq() {
		givesAllClues(new SpyMaster(Team.RED, new SequentialSpyMasterStrategy()));
	}
	
	@Test
	public void guessAllCluesRand() {
		givesAllClues(new SpyMaster(Team.BLUE, new RandomSpyMasterStrategy()));
	}
	
	@Test (expected = IllegalStateException.class)
	public void pickTooManyClues() {
        for (int x=0; x<model.getBoard().getWidth(); x++) {
            for (int y=0; y<model.getBoard().getLength(); y++) {
            	Coordinates coords = new Coordinates(y,x);
            	model.chooseCard(coords);
            }
        }
        Clue test = seqSpy.giveClue(model);     
    }

	//Checks Sequential Strategy to make sure the Spymaster returns the first clue of the right CardValue (in this case RED)
	@Test
	public void sequentialPicksFirstClue() {
		int x = 0;
		int y = 0;
		Board board = model.getBoard();
		Card firstCard = board.getCard(x,y);
		while (firstCard.getValue() != CardValue.RED) {
			x++;
			if (x == 5) {
				x = 0;
				y++;
			}
			firstCard = board.getCard(x,y);
		}
		List<AssociatedWord> boardClueList = firstCard.getAssociatedWords();
		String[] boardWords = new String[boardClueList.size()];
		for (int i = 0; i < boardWords.length; i++) {
			boardWords[i] = boardClueList.get(i).getWord();
		}
		List<String> boardWordsList = Arrays.asList(boardWords);
		
		Clue testClue = seqSpy.giveClue(model);
		String testClueWord = testClue.getWord();
		
		assertTrue(testClue instanceof Clue);
		assertTrue(boardWordsList.contains(testClueWord));		
	}
	
	@Test
	public void randomReturnsClue() {
		Clue testClue = randSpy.giveClue(model);
		assertTrue(testClue instanceof Clue);
	}
	
	
}
