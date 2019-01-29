package ca.concordia.encs.comp354.model;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableSet;

/**
 * A read-only view of a {@link GameState}.
 * @author Nikita Leonidov
 *
 */
public interface ReadOnlyGameState {

    /**
     * @return the active board configuration
     */
    public ReadOnlyObjectProperty<Board> boardProperty();

    /**
     * @return the team currently guessing
     */
    public ReadOnlyObjectProperty<Team> turnProperty();
    
    /**
     * @return the last action that took place in the game (e.g. spymaster giving a clue, players guessing)
     */
    public ReadOnlyObjectProperty<GameAction> lastActionProperty();
    
    /**
     * @return the number of red spaces that have been revealed so far
     */
    public ReadOnlyIntegerProperty redScoreProperty();
    
    /**
     * @return the number of blue spaces that have been revealed so far
     */
    public ReadOnlyIntegerProperty blueScoreProperty();
    
    /**
     * @return a read-only view of the card coordinates that have been revealed so far
     */
    public ObservableSet<Coordinates> getChosenCards();

    ReadOnlyIntegerProperty redObjectiveProperty();

    ReadOnlyIntegerProperty blueObjectiveProperty();
}
