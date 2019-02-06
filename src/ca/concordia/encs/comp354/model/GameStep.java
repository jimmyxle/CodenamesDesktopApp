package ca.concordia.encs.comp354.model;

import ca.concordia.encs.comp354.controller.GameEvent;

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
            return String.format("Turn %02d: %s %s; red: %d, blue: %d (%s)", turn, action.getTeam(), action.getActionText(), redScore, blueScore, text);
        } else {
            return String.format("Turn %02d: %s %s; red: %d, blue: %d", turn, action.getTeam(), action.getActionText(), redScore, blueScore);
        }
    }
    
}
