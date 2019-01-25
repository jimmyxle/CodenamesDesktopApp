package ca.concordia.encs.comp354.model;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableSet;

public abstract class GameState {

    /**
     * @return the active board configuration
     */
    public abstract ReadOnlyObjectProperty<Board> boardProperty();

    /**
     * @return the team currently guessing
     */
    public abstract ReadOnlyObjectProperty<Team> turnProperty();
    
    /**
     * @return the last action that took place in the game (e.g. spymaster giving a clue, players guessing)
     */
    public abstract ReadOnlyObjectProperty<GameAction> lastActionProperty();
    
    /**
     * @return the number of red spaces that have been revealed so far
     */
    public abstract ReadOnlyIntegerProperty redScoreProperty();
    
    /**
     * @return the number of blue spaces that have been revealed so far
     */
    public abstract ReadOnlyIntegerProperty blueScoreProperty();
    
    /**
     * @return a read-only view of the card coordinates that have been revealed so far
     */
    public abstract ObservableSet<Coordinates> getChosenCards();
}
