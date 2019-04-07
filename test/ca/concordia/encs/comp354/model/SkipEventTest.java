package ca.concordia.encs.comp354.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ca.concordia.encs.comp354.controller.GameController;
import ca.concordia.encs.comp354.controller.HumanOperativeStrategy;
import ca.concordia.encs.comp354.controller.Operative;

/**
 * Test suite for {@link GuessEvent} class.
 * @author Nikita Leonidov
 *
 */
public class SkipEventTest {
    
    @Test(expected=UnsupportedOperationException.class)
    public void guessDowncastMethodFails() {
        new SkipEvent().asGuess();
    }
    
    public void guessCheckMethodReturnsFalse() {
        assertFalse(new SkipEvent().isGuess());
    }
    
    @Test
    @SuppressWarnings("unused")
    public void skipDowncastingMethodSucceeds() {
        OperativeEvent ev = new SkipEvent();
        SkipEvent sev = ev.asSkip();
    }
    
    @Test
    public void skipCheckMethodReturnsTrue() {
        OperativeEvent ev = new SkipEvent();
        assertTrue(ev.isSkip());
    }
    
    @Test
    public void eventCompletesInputRequest() {
        // set up model, controller
        List<CodenameWord> words  = new ArrayList<>();
        List<CardValue>    values = new ArrayList<>();
        for (int i=0; i<25; i++) {
            words.add(new CodenameWord("foo", Arrays.asList(new CodenameWord.AssociatedWord("bar", 1))));
            values.add(CardValue.NEUTRAL);
        }
        values.set(0, CardValue.RED);
        values.set(1, CardValue.BLUE);
        
        Keycard keycard = new Keycard(values);
        
        Board board = new Board(words, keycard);
        GameState state = new GameState(board);
        
        GameController controller = new GameController.Builder()
            .setModel(state)
            .setRedOperative(new Operative(Team.RED, new HumanOperativeStrategy()))
            .create();
        
        // step 1: spymaster gives clue
        controller.advanceTurn();
        assertNotEquals(0, state.guessesRemainingProperty().get());
        // step 2: red HumanOperative's turn to guess; requests input
        controller.advanceTurn();
        // step 3: fill input request
        state.operativeInputProperty().get().finish(new SkipEvent());
        // step 4: the request is filled; 
        //         the human operative produces a SkipTurnEvent; 
        //         the GameController updates the GameState
        assertEquals(0, state.guessesRemainingProperty().get());
    }
}
