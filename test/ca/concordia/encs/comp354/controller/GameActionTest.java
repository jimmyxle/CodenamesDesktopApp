package ca.concordia.encs.comp354.controller;

import org.junit.Test;

import ca.concordia.encs.comp354.controller.action.AbstractActionTest;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.controller.GameAction;

import static org.junit.Assert.*;

public class GameActionTest extends AbstractActionTest {
		
	private GameAction ga;
	private GameState gameState;
	
	
	//apply() fails when called twice
	@Test(expected=IllegalStateException.class)
	public void applyCalledTwice() {
		ga.apply(gameState);
		ga.apply(gameState);
	}


    //undo() fails if apply() has not been called previously
	@Test(expected=IllegalStateException.class)
	public void isApplyCalledBeforeUndo() {
		ga.undo(gameState);
	}
	
    //undo() fails if called twice in a row
	@Test(expected=IllegalStateException.class)
	public void undoCalledTwice() {
		ga.undo(gameState);
		ga.undo(gameState);
	}		
}
