package ca.concordia.encs.comp354.model;

import ca.concordia.encs.comp354.controller.GameEvent;

/**
 * An entry in the model's {@link GameState#getHistory() history}.
 * @author Mykyta Leonidov
 *
 */
public class GameStep {

    private final GameAction action;
    private final GameEvent  event;
    
    private final int redScore;
    private final int blueScore;
    
    private final int turn;
    
    GameStep(GameAction action, GameEvent event, int red, int blue, int turn) {
        this.action = action;
        this.event = event;
        
        this.redScore = red;
        this.blueScore = blue;
        
        this.turn = turn;
    }
    
    public GameAction getAction() {
        return action;
    }
    
    public GameEvent getEvent() {
        return event;
    }
    
    public int getRedScore() {
        return redScore;
    }
    
    public int getBlueScore() {
        return blueScore;
    }
    
    public int getTurn() {
        return turn;
    }
    
    public String getText() {
        String text = event.getEventText();
        if (!text.isEmpty()) {
            if (event.isTerminal()) {
                text = "game over: "+text;
            }
            
            if (event==GameEvent.GAME_OVER_ASSASSIN) {
                text += "; "+(action.getTeam()==Team.RED?GameEvent.GAME_OVER_BLUE_WON:GameEvent.GAME_OVER_RED_WON).getEventText();
            }
            return String.format("%02d: %s; red: %d, blue: %d (%s)", turn, action.getActionText(), redScore, blueScore, text);
        } else {
            return String.format("%02d: %s; red: %d, blue: %d", turn, action.getActionText(), redScore, blueScore);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((action == null) ? 0 : action.hashCode());
        result = prime * result + blueScore;
        result = prime * result + ((event == null) ? 0 : event.hashCode());
        result = prime * result + redScore;
        result = prime * result + turn;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GameStep other = (GameStep) obj;
        if (action == null) {
            if (other.action != null)
                return false;
        } else if (!action.equals(other.action))
            return false;
        if (blueScore != other.blueScore)
            return false;
        if (event != other.event)
            return false;
        if (redScore != other.redScore)
            return false;
        if (turn != other.turn)
            return false;
        return true;
    }
    
    
}
