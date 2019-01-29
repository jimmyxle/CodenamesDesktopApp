package ca.concordia.encs.comp354.controller;

import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.view.GameView;

public class GameController implements GameView.Controller {

    private GameState model;

    public GameController(GameState model) {
        this.model = model;
    }
    
    @Override
    public GameEvent advanceTurn() {
        return GameEvent.NONE;
    }
}
