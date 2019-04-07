package ca.concordia.encs.comp354.controller;

import org.junit.Test;

import ca.concordia.encs.comp354.controller.action.AbstractActionTest;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.Promise;
import ca.concordia.encs.comp354.controller.GameAction;

import static org.junit.Assert.*;

public class GameActionTest extends AbstractActionTest {
		
	GameAction gameAction = new GameAction(model.getTurn()) {

		@Override
		public String getActionText() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected Promise<GameEvent> doApply(GameState state) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void doUndo(GameState state) {
			// TODO Auto-generated method stub
			
		}
        
    };
	
	//apply() fails when called twice
	@Test(expected=IllegalStateException.class)
	public void applyCalledTwice() {
		gameAction.apply(model);
		gameAction.apply(model);
	}

	

    //undo() fails if apply() has not been called previously
	@Test(expected=IllegalStateException.class)
	public void isApplyCalledBeforeUndo() {
		gameAction.undo(model);
	}
	
    //undo() fails if called twice in a row
	@Test(expected=IllegalStateException.class)
	public void undoCalledTwice() {
		gameAction.undo(model);
		gameAction.undo(model);
	}		
}
