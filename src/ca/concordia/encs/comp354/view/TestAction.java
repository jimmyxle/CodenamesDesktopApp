package ca.concordia.encs.comp354.view;

import ca.concordia.encs.comp354.model.GameAction;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Team;

/**
 * Dummy implementation of GameAction to feed fake model data to the GUI.
 * 
 * @author Nikita Leonidov
 *
 */
class TestAction extends GameAction {

    private final Team team;
    private final String text;

    TestAction(Team team, String text) {
        this.team = team;
        this.text = text;
    }
    
    @Override
    public Team getTeam() {
        return team;
    }

    @Override
    public String getActionText() {
        return text;
    }

    @Override
    protected void apply(GameState state) {
        // nada
    }

}
