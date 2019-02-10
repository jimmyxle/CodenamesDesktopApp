package ca.concordia.encs.comp354.controller.action;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ca.concordia.encs.comp354.model.Team;

/**
 * Tests command queue functionality.
 * @author Nikita Leonidov
 *
 */
public class GameActionTest extends AbstractActionTest {

    @Test
    public void undoFailsWithEmptyQueue() {
        assertFalse(model.undoAction());
    }
    
    @Test
    public void redoFailsWithEmptyQueue() {
        assertFalse(model.redoAction());
    }
    @Test
    public void advanceAddsActionToHistory() {
        model.pushAction(new ChangeTurnAction(Team.RED));
        assertFalse(model.getHistory().isEmpty());
    }
    
    @Test
    public void undoAddsActionToUndoStack() {
        model.pushAction(new ChangeTurnAction(Team.RED));
        assertTrue(model.undoAction());
        assertFalse(model.getUndone().isEmpty());
    }
    
    @Test
    public void undoRemovesActionFromHistory() {
        model.pushAction(new ChangeTurnAction(Team.RED));
        assertTrue(model.undoAction());
        assertTrue(model.getHistory().isEmpty());
    }
    
    @Test
    public void redoAddsActionToHistory() {
        model.pushAction(new ChangeTurnAction(Team.RED));
        assertTrue(model.undoAction());
        assertTrue(model.redoAction());
        assertFalse(model.getHistory().isEmpty());
    }
    
    @Test
    public void redoRemovesActionFromUndoStack() {
        model.pushAction(new ChangeTurnAction(Team.RED));
        assertTrue(model.undoAction());
        assertTrue(model.redoAction());
        assertTrue(model.getUndone().isEmpty());
    }
}
