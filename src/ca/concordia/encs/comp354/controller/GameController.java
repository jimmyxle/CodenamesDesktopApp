package ca.concordia.encs.comp354.controller;

import ca.concordia.encs.comp354.controller.action.ChangeTurnAction;
import ca.concordia.encs.comp354.controller.GameAction;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Team;
import ca.concordia.encs.comp354.view.GameView;

import static java.util.Objects.requireNonNull;

/**
 * <p>The game controller coordinates {@link Player}s and generates {@link GameAction}s. It has a read-write view of the 
 * model (the {@link GameState}). The responsibility of the controller is to receive input from the user interface and
 * execute the game loop.
 * 
 * <p>The controller's logic is itself fairly minimal; {@link GameAction}s represent units of game logic - commands - 
 * which may be applied, undone, or inspected to reveal the game history. The {@link GameState} serves as the command
 * manager.
 * 
 * @author Mykyta Leonidov
 * @author Christopher Idah
 */
public class GameController implements GameView.Controller {

	// these fields should be immutable once set by the Builder
	//------------------------------------------------------------------------------------------------------------------
	private GameState model;
	private SpyMaster redSpyMaster;
	private Operative redOperative;
	private SpyMaster blueSpyMaster;
	private Operative blueOperative;

	public Team initialTurn;

	public static final class Builder {
		private GameState model;
		private SpyMaster redSpyMaster  = new SpyMaster(Team.RED,  new SequentialSpyMasterStrategy());
		private Operative redOperative  = new Operative(Team.RED,  new SequentialOperativeStrategy());
		private SpyMaster blueSpyMaster = new SpyMaster(Team.BLUE, new SequentialSpyMasterStrategy());
		private Operative blueOperative = new Operative(Team.BLUE, new SequentialOperativeStrategy());

		private Team initialTurn = Team.RED;

		public Builder setInitialTurn(Team team) {
			initialTurn  = requireNonNull(team);
			return this;
		}

		public Builder setModel(GameState value) {
			model = value;
			return this;
		}

		public Builder setRedSpyMaster(SpyMaster value) {
			requireTeam(value, Team.RED);
			redSpyMaster = value;
			return this;
		}

		public Builder setRedOperative(Operative value) {
			requireTeam(value, Team.RED);
			redOperative = value;
			return this;
		}

		public Builder setBlueSpyMaster(SpyMaster value) {
			requireTeam(value, Team.BLUE);
			blueSpyMaster = value;
			return this;
		}

		public Builder setBlueOperative(Operative value) {
			requireTeam(value, Team.BLUE);
			blueOperative = value;
			return this;
		}

		public GameController create() {
			GameController ret = new GameController();

			ret.model         = requireNonNull(model,         "model");
			ret.redSpyMaster  = requireNonNull(redSpyMaster,  "redSpyMaster");
			ret.redOperative  = requireNonNull(redOperative,  "redOperative");
			ret.blueSpyMaster = requireNonNull(blueSpyMaster, "blueSpyMaster");
			ret.blueOperative = requireNonNull(blueOperative, "blueOperative");
			ret.initialTurn   = initialTurn;

			ret.initialize();

			return ret;
		}

		private static void requireTeam(Player player, Team team) {
			if (player.getTeam()!=team) {
				throw new IllegalArgumentException("player's team must be "+team);
			}
		}
	}

	private GameController() {
	}

	private void initialize() {
		model.pushAction(new ChangeTurnAction(initialTurn));
	}

	@Override
	public void advanceTurn() {
		if (model.lastActionProperty().get()==null) {
			initialize();
			return;
		}

		if (model.getLastEvent().isTerminal()) {
			return;
		}

		final Team turn = model.getTurn();
		
        SpyMaster currentSpy = turn==Team.RED? redSpyMaster : blueSpyMaster; 
        Operative currentOp  = turn==Team.RED? redOperative : blueOperative;
        
		// if there's no clue, it's the current spymaster's turn
		if (model.lastClueProperty().get()==null) {
			model.pushAction(currentSpy.giveClue(model));
		} else {
			// advance to next turn iff guesses remain & last action did not end game
			if (!model.hasGuesses() && !model.getLastEvent().isTerminal()) {
				model.pushAction(new ChangeTurnAction(turn==Team.RED? Team.BLUE: Team.RED));
			} else {
				// otherwise, let the current operative make another guess
				currentOp.guessCard(model, model.lastClueProperty().get()).then(act->{
				    model.pushAction(act);
				    if (currentOp.isHuman()) {
				        advanceTurn(); // skip straight to the next action iff this player is human
				    }
				});
			}
		}	
	}

	@Override
	public boolean undoTurn() {
		return model.undoAction();
	}

	@Override
	public boolean redoTurn() {
	    return model.redoAction();
	}
  
	@Override
	public void restartGame() {
	    model.reset();
	}
}
