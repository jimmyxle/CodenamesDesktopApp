package ca.concordia.encs.comp354.model;

import java.util.Objects;

public final class GuessEvent implements HumanOperativeEvent {

    private final Coordinates coords;

    public GuessEvent(Coordinates coords) {
        this.coords = Objects.requireNonNull(coords);
    }
    
    public Coordinates getCoords() {
        return coords;
    }
    
    @Override
    public boolean isGuess() {
        return true;
    }

    @Override
    public GuessEvent asGuess() {
        return this;
    }

}
