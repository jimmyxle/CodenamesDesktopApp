package ca.concordia.encs.comp354.controller;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Card;
import ca.concordia.encs.comp354.model.CardValue;
import ca.concordia.encs.comp354.model.CodenameWord;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Keycard;
import ca.concordia.encs.comp354.model.Team;
import ca.concordia.encs.comp354.model.CodenameWord.AssociatedWord;

public class SequentialSpyMasterStrategyTest extends AbstractPlayerTest {
    
    final SpyMaster seqSpy;
    final GameState model;
    
    
    public SequentialSpyMasterStrategyTest() throws IOException {
        List<CodenameWord> codenameWords = Card.createRandomCodenameList(Paths.get("res/words.txt"));
        Keycard keycard = Keycard.createRandomKeycard();
        
        model = new GameState(new Board(codenameWords, keycard));
        seqSpy = new SpyMaster(Team.RED, new SequentialSpyMasterStrategy());
    }
    
    //Checks if manually generated clues match the clues from each strategy
    @Test
    public void sequentialStrategyGivesAllClues() {
        givesAllClues(new SpyMaster(Team.RED, new SequentialSpyMasterStrategy()));
    }
    
    //Tests that the SpyMaster can't generate extra clues
    @Test (expected = IllegalStateException.class)
    public void sequentialStrategyFailsNoMoreClues() {
        for (int x=0; x<model.getBoard().getWidth(); x++) {
            for (int y=0; y<model.getBoard().getLength(); y++) {
                Coordinates coords = new Coordinates(y,x);
                model.chooseCard(coords);
            }
        }
        
        seqSpy.giveClue(model);     
    }

    //Checks Sequential Strategy to make sure the Spymaster returns the first clue of the right CardValue (in this case RED)
    @Test
    public void sequentialStrategyPicksFirstClue() {
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
        
        Clue testClue = giveClue(seqSpy, model);
        String testClueWord = testClue.getWord();
        
        assertTrue(testClue instanceof Clue);
        assertTrue(boardWordsList.contains(testClueWord));      
    }
}
