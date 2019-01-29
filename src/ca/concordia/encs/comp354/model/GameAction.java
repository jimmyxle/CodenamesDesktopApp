package ca.concordia.encs.comp354.model;

public abstract class GameAction {

    public abstract Team getTeam();
    
    public abstract String getActionText();
    
    protected abstract void apply(GameState state);
}
