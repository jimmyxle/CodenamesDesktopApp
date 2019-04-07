package ca.concordia.encs.comp354.model.action;

import org.junit.Test;
import static org.junit.Assert.*;

import ca.concordia.encs.comp354.controller.Operative;
import ca.concordia.encs.comp354.controller.GameEvent;
import ca.concordia.encs.comp354.model.Team;
import ca.concordia.encs.comp354.model.action.SkipTurnAction;

/**
 * 
 * @author Yaacov Cohen 
 * 
 */
public class SkipTurnActionTest extends AbstractActionTest {
   
    Operative      op   = new Operative(Team.RED, new NullOperativeStrategy());
    SkipTurnAction skip = new SkipTurnAction(op);
   
	
	@Test
	public void setsRemainingGuessesToZero(){
		model.guessesRemainingProperty().setValue(1);
		model.pushAction(skip);
		assertEquals(0, model.guessesRemainingProperty().get());
	}
	
	@Test
	public void endsTurn() {
		assertEquals(GameEvent.END_TURN, model.pushAction(skip));	
	}
	
	@Test
	public void undoRestoresRemainingGuesses() {
		model.guessesRemainingProperty().setValue(2);
		model.pushAction(skip);
        assertEquals(0, model.guessesRemainingProperty().get());
		model.undoAction();
		assertEquals(2, model.guessesRemainingProperty().get());
	}

}

