package ca.concordia.encs.comp354.controller;

import ca.concordia.encs.comp354.Promise;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.ReadOnlyGameState;

public class HumanOperativeStrategy implements Operative.Strategy {
    
    @Override
    public Promise<Coordinates> guessCard(Operative owner, ReadOnlyGameState state, Clue clue) {
        // TODO Auto-generated method stub
        return null;
    }

}
