package ca.concordia.encs.comp354.controller.action;

import org.junit.Test;

import ca.concordia.encs.comp354.controller.Clue;
import ca.concordia.encs.comp354.controller.SpyMaster;
import ca.concordia.encs.comp354.model.Team;

import static org.junit.Assert.*;

/**
 * Test class for {@link GiveClueAction}.
 * @author Mykyta Leonidov
 *
 */
public class GiveClueActionTest extends AbstractActionTest {
    
    private final SpyMaster opRed = new SpyMaster(Team.RED, new NullSpyMasterStrategy());

    @Test
    public void appliesClue() {
        model.pushAction(new GiveClueAction(opRed, new Clue("foo", 1)));
        assertEquals(new Clue("foo", 1), model.lastClueProperty().get());
    }

    @Test
    public void undoesClue() {
        model.pushAction(new GiveClueAction(opRed, new Clue("foo", 1)));
        model.pushAction(new GiveClueAction(opRed, new Clue("bar", 2)));
        model.undoAction();
        
        assertEquals(new Clue("foo", 1), model.lastClueProperty().get());
    }

    @Test
    public void redoesClue() {
        model.pushAction(new GiveClueAction(opRed, new Clue("foo", 1)));
        model.pushAction(new GiveClueAction(opRed, new Clue("bar", 2)));
        model.undoAction();
        model.redoAction();
        
        assertEquals(new Clue("bar", 2), model.lastClueProperty().get());
    }

    @Test
    public void appliesGuesses() {
        model.pushAction(new GiveClueAction(opRed, new Clue("foo", 1)));
        assertEquals(1, model.guessesRemainingProperty().get());
    }

    @Test
    public void undoesGuesses() {
        model.pushAction(new GiveClueAction(opRed, new Clue("foo", 1)));
        model.pushAction(new GiveClueAction(opRed, new Clue("bar", 2)));
        model.undoAction();

        assertEquals(1, model.guessesRemainingProperty().get());
    }

    @Test
    public void redoesGuesses() {
        model.pushAction(new GiveClueAction(opRed, new Clue("foo", 1)));
        model.pushAction(new GiveClueAction(opRed, new Clue("bar", 2)));
        model.undoAction();
        model.redoAction();

        assertEquals(2, model.guessesRemainingProperty().get());
    }
}
