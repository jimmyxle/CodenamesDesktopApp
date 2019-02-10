package ca.concordia.encs.comp354.model;

import ca.concordia.encs.comp354.controller.GameEvent;

/**
 * An entry in the model's {@link GameState#getHistory() history}.
 * @author Nikita Leonidov
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
    
}
