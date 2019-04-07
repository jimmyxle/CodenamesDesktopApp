package ca.concordia.encs.comp354.controller.action;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

import ca.concordia.encs.comp354.Promise;
import ca.concordia.encs.comp354.controller.GameController;
import ca.concordia.encs.comp354.controller.Operative;
import ca.concordia.encs.comp354.controller.GameEvent;
import ca.concordia.encs.comp354.controller.SequentialOperativeStrategy;
import ca.concordia.encs.comp354.controller.SequentialSpyMasterStrategy;
import ca.concordia.encs.comp354.controller.SpyMaster;
import ca.concordia.encs.comp354.controller.action.ChangeTurnAction;
import ca.concordia.encs.comp354.controller.action.GiveClueAction;
import ca.concordia.encs.comp354.controller.action.GuessCardAction;
import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.CodenameWord;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Keycard;
import ca.concordia.encs.comp354.model.Team;
import ca.concordia.encs.comp354.controller.action.SkipTurnAction;



public class SkipTurnActionTest extends AbstractActionTest {

	/**
	 * 
	 * Author: Yaacov Cohen 
	 * 
	 */
   
    Operative 		op = new Operative(Team.RED, new NullOperativeStrategy());
    SkipTurnAction  skip = new SkipTurnAction(op);
   
	
	@Test
	public void setGuessesRemainingPropertyToZero(){
		model.guessesRemainingProperty().setValue(1);
		model.pushAction(skip);
		assertEquals(0, model.guessesRemainingProperty().get());
	}
	
	@Test
	public void applyMethodReturnsAPromise() {
		assertEquals(GameEvent.END_TURN, skip.apply(model).get());	
	}
	
	@Test
	public void undoRestoresNumberOfGuesses() {
		model.guessesRemainingProperty().setValue(2);
		model.pushAction(skip);
		skip.undo(model);
		assertEquals(2, model.guessesRemainingProperty().get());
	}

}

