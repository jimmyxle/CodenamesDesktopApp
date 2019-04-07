package ca.concordia.encs.comp354.model;

import org.junit.Test;

import ca.concordia.encs.comp354.model.GameAction;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.action.AbstractActionTest;
import ca.concordia.encs.comp354.controller.GameEvent;

/**
  *
 * @author Vickel Leung
 *
 */
public class GameActionTest extends AbstractActionTest {
		
	GameAction gameAction = new GameAction(model.getTurn()) {

		@Override
		public String getActionText() {
			return null;
		}

		@Override
		protected GameEvent doApply(GameState state) {
			return null;
		}

		@Override
		protected void doUndo(GameState state) {
		}    
    };
	
	//apply() fails when called twice without an undo()
	@Test(expected=IllegalStateException.class)
	public void failOnReapply() {
		gameAction.apply(model);
		gameAction.apply(model);
	}

    //undo() fails if apply() has not been called previously
	@Test(expected=IllegalStateException.class)
	public void failOnUndoUnappliedAction() {
		gameAction.undo(model);
	}
	
    //undo() fails if called twice without a redo()
	@Test(expected=IllegalStateException.class)
	public void failOnUndoUndoneAction() {
		gameAction.undo(model);
		gameAction.undo(model);
	}		
}
