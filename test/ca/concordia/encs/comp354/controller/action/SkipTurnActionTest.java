package ca.concordia.encs.comp354.controller.action;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
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



public class SkipTurnActionTest {

	/**
	 * 
	 * Author: Yaacov Cohen 
	 * 
	 */
    SkipTurnAction 		 skip;
    Operative 			 op;
    GameState			 state;
    
	public SkipTurnActionTest() {
		op = new Operative(null, new NullOperativeStrategy());
	}
	
	@Test
	public void setGuessesRemainingPropertyToZero(){
		assertTrue(state.guessesRemainingProperty().getValue()==0);
	}
	
	@Test
	public void applyMethodReturnsAPromise() {
	
	}
	
	@Test
	public void undoMethodremembersPreviousTeamsNumberOfGuesses() {
		
	}

}

