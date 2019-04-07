package ca.concordia.encs.comp354.controller;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Card;
import ca.concordia.encs.comp354.model.CodenameWord;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Keycard;
import ca.concordia.encs.comp354.model.Team;

public class RandomSpyMasterStrategyTest extends AbstractPlayerTest {
    
    final SpyMaster randSpy;
    final GameState model;
    
    
    public RandomSpyMasterStrategyTest() throws IOException {
        List<CodenameWord> codenameWords = Card.createRandomCodenameList(Paths.get("res/words.txt"));
        Keycard keycard = Keycard.createRandomKeycard();
        
        model = new GameState(new Board(codenameWords, keycard));
        randSpy = new SpyMaster(Team.BLUE, new RandomSpyMasterStrategy());
    }
    
    @Test
    public void randomStrategyGivesAllClues() {
        givesAllClues(new SpyMaster(Team.BLUE, new RandomSpyMasterStrategy()));
    }
    
    @Test (expected = IllegalStateException.class)
    public void randomStrategyFailsNoMoreClues() {
        for (int x=0; x<model.getBoard().getWidth(); x++) {
            for (int y=0; y<model.getBoard().getLength(); y++) {
                Coordinates coords = new Coordinates(y,x);
                model.chooseCard(coords);
            }
        }
        
        randSpy.giveClue(model);     
    }
    
    //Makes sure SpyMaster random strategy returns a clue. Can't test randomness specifically
    @Test
    public void randomStrategyReturnsClue() {
        Clue testClue = giveClue(randSpy, model);
        assertTrue(testClue instanceof Clue);
    }
    
    
}
