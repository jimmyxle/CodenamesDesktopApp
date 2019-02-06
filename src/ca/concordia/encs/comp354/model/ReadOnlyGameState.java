package ca.concordia.encs.comp354.model;

import ca.concordia.encs.comp354.controller.Clue;
import ca.concordia.encs.comp354.controller.SpyMaster;
import ca.concordia.encs.comp354.controller.GameEvent;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
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
    ReadOnlyObjectProperty<Board> boardProperty();
    
    /**
     * Equivalent to <tt>{@link #turnProperty()}.get()</tt>
     * @return an integer value representing the current team
     */
    Team getTurn();
    
    /**
     * @return the team currently guessing
     */
    ReadOnlyObjectProperty<Team> turnProperty();
    
    /**
     * @return the last action that took place in the game (e.g. spymaster giving a clue, players guessing)
     */
    ReadOnlyObjectProperty<GameAction> lastActionProperty();
    
    /**
     * Equivalent to <tt>{@link #lastEventProperty()}.get()</tt>
     * @return the last game event that took place (turn ended, game over)
     */
    GameEvent getLastEvent();
    
    /**
     * @return the last game event that took place (turn ended, game over)
     */
    ReadOnlyObjectProperty<GameEvent> lastEventProperty();

    /**
     * @return a read-only view of the actions taken in the game so far and their outcomes
     */
    ObservableList<GameStep> getHistory();

    /**
     * @return the most recent element in {@link #getHistory()}
     */
    ReadOnlyObjectProperty<GameStep> lastStepProperty();

    /**
     * @return the most recent clue provided by a {@link SpyMaster}
     */
    ReadOnlyObjectProperty<Clue> lastClueProperty();
    
    /**
     * @return the number of guesses allowed for the current team's operatives
     */
    ReadOnlyIntegerProperty guessesRemainingProperty();

    /**
     * @return <tt>true</tt> when <tt>{@code guessesRemainingProperty().get()>0}</tt>
     */
    boolean hasGuesses();
    
    /**
     * Equivalent to <tt>{@link #redScoreProperty()}.get()</tt>
     * @return an integer value representing the current red score
     */
    int getRedScore();
    
    /**
     * @return the number of red spaces that have been revealed so far
     */
    ReadOnlyIntegerProperty redScoreProperty();

    /**
     * Equivalent to <tt>{@link #blueScoreProperty()}.get()</tt>
     * @return an integer value representing the current blue score
     */
    int getBlueScore();
    
    /**
     * @return the number of blue spaces that have been revealed so far
     */
    ReadOnlyIntegerProperty blueScoreProperty();

    /**
     * @return the number of red cards that must be revealed for a red victory
     */
    ReadOnlyIntegerProperty redObjectiveProperty();

    /**
     * @return the number of blue cards that must be revealed for a blue victory
     */
    ReadOnlyIntegerProperty blueObjectiveProperty();
    
    /**
     * @return a read-only view of the card coordinates that have been revealed so far
     */
    ObservableSet<Coordinates> getChosenCards();
}