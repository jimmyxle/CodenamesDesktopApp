package ca.concordia.encs.comp354.controller.action;

import org.junit.Test;
import ca.concordia.encs.comp354.model.Team;

import static org.junit.Assert.*;
import static ca.concordia.encs.comp354.model.Team.*;

/**
 * Test class for {@link ChangeTurnAction}.
 * @author Nikita Leonidov
 *
 */
public class ChangeTurnActionTest extends AbstractActionTest {

    @Test
    public void changesTurnFromRedToBlue() {
        changesTurn(RED, BLUE);
    }
    
    @Test
    public void changesTurnFromBlueToRed() {
        changesTurn(BLUE, RED);
    }
    
    @Test
    public void undoesFromRedToBlue() {
        undoesTurn(RED, BLUE);
    }
    
    @Test
    public void undoesFromBlueToRed() {
        undoesTurn(BLUE, RED);
    }
    
    @Test
    public void redoesFromRedToBlue() {
        redoesTurn(RED, BLUE);
    }
    
    @Test
    public void redoesFromBlueToRed() {
        redoesTurn(BLUE, RED);
    }
    
    private void changesTurn(Team from, Team to) {
        pushTurn(from, to);
        assertEquals(to, model.getTurn());
    }
    
    private void undoesTurn(Team from, Team to) {
        pushTurn(from, to);
        assertTrue(model.undoAction());
        assertEquals(from, model.getTurn());
    }
    
    private void redoesTurn(Team from, Team to) {
        pushTurn(from, to);
        assertTrue(model.undoAction());
        assertTrue(model.redoAction());
        assertEquals(from, model.getTurn());
    }
    
    private void pushTurn(Team from, Team to) {
        model.turnProperty().set(from);
        model.pushAction(new ChangeTurnAction(to));
    }
}
