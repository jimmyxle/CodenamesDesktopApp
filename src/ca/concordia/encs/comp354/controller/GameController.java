package ca.concordia.encs.comp354.controller;

import ca.concordia.encs.comp354.model.GameAction;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Team;
import ca.concordia.encs.comp354.view.GameView;

import static java.util.Objects.requireNonNull;

/**
 * The game controller coordinates {@link Player}s and generates {@link GameAction}s. It has
 * a read-write view of the model (the {@link GameState}).
 * @author Nikita Leonidov
 *
 */
public class GameController implements GameView.Controller {

    private GameState model;
    private SpyMaster redSpyMaster;
    private Operative redOperative;
    private SpyMaster blueSpyMaster;
    private Operative blueOperative;
    
    public static final class Builder {
        private GameState model;
        private SpyMaster redSpyMaster;
        private Operative redOperative;
        private SpyMaster blueSpyMaster;
        private Operative blueOperative;
        
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
            
            ret.model         = requireNonNull(model, "model");
            ret.redSpyMaster  = requireNonNull(redSpyMaster,  "redSpyMaster");
            ret.redOperative  = requireNonNull(redOperative,  "redOperative");
            ret.blueSpyMaster = requireNonNull(blueSpyMaster, "blueSpyMaster");
            ret.blueOperative = requireNonNull(blueOperative, "blueOperative");
            
            return ret;
        }
        
        private static void requireTeam(Player player, Team team) {
            if (player.getTeam()!=team) {
                throw new IllegalArgumentException("player's team must be "+team);
            }
        }
    }

    private GameController() {}
    
    @Override
    public void advanceTurn() {
        // TODO generate a game action depending on the current team and player
        // NB: read the current team from the model! the initial turn is set in its constructor
    }
}
