package ca.concordia.encs.comp354.controller.action;

import ca.concordia.encs.comp354.Promise;
import ca.concordia.encs.comp354.controller.GameEvent;
import ca.concordia.encs.comp354.controller.Operative;
import ca.concordia.encs.comp354.model.GameState;

public class SkipTurnAction extends OperativeAction {
    
    private int value;

	public SkipTurnAction(Operative owner) {
		super(owner);
	}

	@Override
	public String getActionText() {
		return String.format("It is now %s's turn", getTeam());
	}

	@Override
	protected  Promise<GameEvent> doApply(GameState state) {
		value = state.guessesRemainingProperty().getValue();
		state.guessesRemainingProperty().setValue(0);
		return Promise.of(GameEvent.END_TURN);
	}

	@Override
	protected void doUndo(GameState state) {
		state.guessesRemainingProperty().set(value); 
	}
}
