package ca.concordia.encs.comp354.controller;

import ca.concordia.encs.comp354.Promise;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Team;


public class SkipTurnAction extends GameAction{
		
		
		 private int value;

	public SkipTurnAction(Team team) {
		super(team);
		
	}

	@Override
	public String getActionText() {
		return String.format("It is now %s's turn", getTeam());
	}

	@Override
	protected  Promise<GameEvent> doApply(GameState state) {
		value=(state.guessesRemainingProperty().getValue());
		state.guessesRemainingProperty().setValue(0);
		//System.out.println(value);
		//System.out.println(state);
		return Promise.finished(GameEvent.END_TURN);
	}

	@Override
	protected void doUndo(GameState state) {
		state.guessesRemainingProperty().set(value); 
	}

	
	
	
	
	
}
