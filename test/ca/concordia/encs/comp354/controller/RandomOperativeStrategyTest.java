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

public class RandomOperativeStrategyTest extends AbstractPlayerTest {
    
    final GameState model;
    
    public RandomOperativeStrategyTest() throws IOException {
        List<CodenameWord> codenameWords = Card.createRandomCodenameList(Paths.get("res/words.txt"));
        Keycard keycard = Keycard.createRandomKeycard();
        
        model = new GameState(new Board(codenameWords, keycard));
    }
    
    @Test
    public void randomStrategyPicksAllCards() {
        Operative seq = new Operative(Team.RED, new RandomOperativeStrategy());
        Clue clue = new Clue("test", 1);
        for (int i = 0; i < 25; i++) {
            Coordinates guess = guessCard(seq, model, clue);
            model.chooseCard(guess);
        }
        
        assertTrue(model.getChosenCards().size() == model.getBoard().getLength() * model.getBoard().getWidth());
    }
    
    @Test (expected = IllegalStateException.class)
    public void randomStrategyFailsWithNoValidMoves() {
        Operative seq = new Operative(Team.RED, new RandomOperativeStrategy());
        Clue clue = new Clue("test", 1);
        for (int i = 0; i < 26; i++) {
            Coordinates guess = guessCard(seq, model, clue);
            model.chooseCard(guess);
        }
    }
    
    
    
}
