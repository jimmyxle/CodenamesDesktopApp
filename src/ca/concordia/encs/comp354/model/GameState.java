package ca.concordia.encs.comp354.model;

import java.util.ArrayList;
import java.util.Objects;

import ca.concordia.encs.comp354.controller.Clue;
import ca.concordia.encs.comp354.controller.GameEvent;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

/**
 * Represents the game model. Specifies no behaviour, other than checking that mutator inputs are valid.
 * The view should only receive instances of this class as {@link ReadOnlyGameState}s, ensuring that only
 * the controller has authority to mutate the object.
 * 
 * @author Nikita Leonidov
 *
 */
/*
 * NB: passing a GameState without upcasting to ReadOnlyGameState means that the receiving method will 
 * have a _mutable view_ of the model. If one of the property accessors defined in this class returns a 
 * read only property, there is a good reason for this, and suggests that its value must be modified by 
 * some other method.
 * 
 * Whenever you add a property to this class, make sure that a read-only way of accessing the property
 * and/or its value is also present in ReadOnlyGameState. Lastly, be sure to add the appropriate 
 * @Override annotations to the corresponding definitions in this class.
 */
public final class GameState implements ReadOnlyGameState {

    // game state
    //------------------------------------------------------------------------------------------------------------------
    private final ObjectProperty<Board>      board   = new SimpleObjectProperty<>(this, "board",   null);
    private final ObjectProperty<Team>       turn    = new SimpleObjectProperty<>(this, "turn",    null);
    private final ObjectProperty<Clue>       clue    = new SimpleObjectProperty<>(this, "clue",    null);
    private final IntegerProperty            guesses = new SimpleIntegerProperty (this, "guesses", 0);
    
    private final IntegerProperty redScore  = new SimpleIntegerProperty(this, "redScore", 0);
    private final IntegerProperty blueScore = new SimpleIntegerProperty(this, "redScore", 0);
    
    // the minimum number of successful guesses a team must make to win
    private final ReadOnlyIntegerProperty redObjective;
    private final ReadOnlyIntegerProperty blueObjective;
    
    // the set of revealed cards
    private final ObservableSet<Coordinates> chosen         = FXCollections.observableSet();
    private final ObservableSet<Coordinates> readOnlyChosen = FXCollections.unmodifiableObservableSet(chosen);
    
    // history ("command queue")
    //------------------------------------------------------------------------------------------------------------------
    // closest javafx offers to observable deques is lists; use pop() and peek() convenience methods to modify
    private final ObservableList<GameStep> history         = FXCollections.observableList(new ArrayList<>());
    private final ObservableList<GameStep> readOnlyHistory = FXCollections.unmodifiableObservableList(history);
    private final ObservableList<GameStep> undone          = FXCollections.observableList(new ArrayList<>());
    private final ObservableList<GameStep> readOnlyUndone  = FXCollections.unmodifiableObservableList(undone);
    
    // values at the top of history stack; these make it easier for the view to watch for changes in state
    private final ObjectProperty<GameStep>   step    = new SimpleObjectProperty<>(this, "step",    null);
    private final ObjectProperty<GameAction> action  = new SimpleObjectProperty<>(this, "action",  null);
    private final ObjectProperty<GameEvent>  event   = new SimpleObjectProperty<>(this, "event",   GameEvent.NONE);
    
    public GameState(Board board) {
        this.board.set(Objects.requireNonNull(board, "board"));
        
        // count red, blue cards on board to determine objectives
        //--------------------------------------------------------------------------------------------------------------
        int redCount  = 0;
        int blueCount = 0;
        
        for (int x=0; x<board.getWidth(); x++) {
            for (int y=0; y<board.getLength(); y++) {
                switch (board.getCard(x, y).getValue()) {
                case BLUE:
                    blueCount++;
                    break;
                case RED:
                    redCount++;
                    break;
                default:
                    break;
                }
            }
        }
        
        redObjective  = new SimpleIntegerProperty(this, "redObjective",  redCount);
        blueObjective = new SimpleIntegerProperty(this, "blueObjective", blueCount);
        
        // look for changes in history to update "top of stack" properties
        //--------------------------------------------------------------------------------------------------------------
        getHistory().addListener((Change<?> c)->recordStep(getHistory().isEmpty() ? null : peek(getHistory())));
    }
    
    public Board getBoard() {
        return boardProperty().get();
    }
    
    @Override
    public ReadOnlyObjectProperty<Board> boardProperty() {
        return board;
    }

    @Override
    public Team getTurn() {
        return turnProperty().get();
    }
    
    @Override
    public ObjectProperty<Team> turnProperty() {
        return turn;
    }

    @Override
    public ReadOnlyObjectProperty<GameAction> lastActionProperty() {
        return action;
    }
    
    @Override
    public GameEvent getLastEvent() {
    	return lastEventProperty().get();
    }
    
    @Override
    public ReadOnlyObjectProperty<GameEvent> lastEventProperty() {
        return event;
    }
    
    @Override
    public ObjectProperty<Clue> lastClueProperty() {
        return clue;
    }

    @Override
    public IntegerProperty guessesRemainingProperty() {
        return guesses;
    }

    @Override
    public boolean hasGuesses() {
        return guessesRemainingProperty().get() > 0;
    }
    
    /**
     * Executes the given action and records it in the model's history.
     * @param value  an action to execute
     * @return the action's return value
     */
    public GameEvent pushAction(GameAction value) {
        // clear undo history -- it's invalid now
        undone.clear();
        applyAction(value);
        return event.get();
    }

    /**
     * Undoes the most recent action and records it in the model's undo history.
     * @return <tt>true</tt> if there was an action available in the undo history
     */
    public boolean undoAction() {
    	if (history.isEmpty()) {
    		return false;
    	}
    	
        // undo most recent action
    	GameStep top = peek(history); // update history after successful undo -- just peek, modify collection later
        top.getAction().undo(this);
        pop(history);
        undone.add(top);
        
        // lastly, return the undone action
        return true;
    }
    
    /**
     * Re-executes the most recently undone action and records it in the model's history.
     * @return <tt>true</tt> if there was an action available in the undo history
     */
    public boolean redoAction() {
    	if (undone.isEmpty()) {
    		return false;
    	}
    	
    	applyAction(pop(undone).getAction());
    	
    	return true;
    }
    
    @Override
    public ObservableList<GameStep> getHistory() {
        return readOnlyHistory;
    }

    @Override
    public ObservableList<GameStep> getUndone() {
    	return readOnlyUndone;
    }
    
    @Override
    public ReadOnlyObjectProperty<GameStep> lastStepProperty() {
        return step;
    }
    
    @Override
    public int getRedScore() {
        return redScoreProperty().get();
    }
    
    @Override
    public IntegerProperty redScoreProperty() {
        return redScore;
    }

    @Override
    public int getBlueScore() {
        return blueScoreProperty().get();
    }
    
    @Override
    public IntegerProperty blueScoreProperty() {
        return blueScore;
    }
    
    @Override
    public ReadOnlyIntegerProperty redObjectiveProperty() {
        return redObjective;
    }
    
    @Override
    public ReadOnlyIntegerProperty blueObjectiveProperty() {
        return blueObjective;
    }

    @Override
    public ObservableSet<Coordinates> getChosenCards() {
        return readOnlyChosen;
    }
    
    /**
     * Reveals a card at the given coordinates.
     * @param coords  the coordinates of the yet-to-be-chosen card
     */
    public void chooseCard(Coordinates coords) {
    	requireValidCoords(coords);
        chosen.add(coords);
    }

    /**
     * Hides a previously revealed card at the given coordinates.
     * @param coords  the coordinates of the previously-chosen card
     */
	public void hideCard(Coordinates coords) {
		requireValidCoords(coords);
		chosen.remove(coords);
	}

	/**
	 * Helper function; ensures coordinates are non-null and fall within the board dimensions
	 * @param coords  coordinate object to test
	 */
	private void requireValidCoords(Coordinates coords) {
        Objects.requireNonNull(coords);
        
        if (coords.getX() < 0 || coords.getX() > getBoard().getWidth()) {
            throw new IllegalArgumentException("illegal x coordinate "+coords.getX()+"; must lie in [0, "+getBoard().getWidth()+")");
        }
        
        if (coords.getY() < 0 || coords.getY() > getBoard().getLength()) {
            throw new IllegalArgumentException("illegal y coordinate "+coords.getY()+"; must lie in [0, "+getBoard().getLength()+")");
        }
        
	}
    
	/**
	 * Helper function; removes the element at the end of a list and returns it
	 * @param col  list from which to remove the last element
	 * @return the removed element
	 */
    private static <T> T pop(java.util.List<T> col) {
    	return col.remove(col.size()-1);
    }
    
    /**
     * Helper function; returns the element at the end of a list without removing it
     * @param col  list from which to remove the last element
     * @return the element at the end of the list, or <tt>null</tt> if the list is empty
     */
    private static <T> T peek(java.util.List<T> col) {
    	return col.isEmpty()? null : col.get(col.size()-1);
    }
    
    /**
     * Executes the given action, and records a new step in the game history.
     * @param value the action to execute
     */
    private void applyAction(GameAction value) {
    	Objects.requireNonNull(value);
        
        // add action to model, execute action, record step
    	GameStep step = new GameStep(value, value.apply(this), redScore.get(), blueScore.get(), history.size());
        history.add(step);
    }
    
    /**
     * Notifies the view that a new step has appeared at the top of the history stack.
     * @param step  the history element at the top of the history stack
     * @return <tt>step</tt>
     */
    private GameStep recordStep(GameStep step) {
        action.set(step.getAction());
        event.set(step.getEvent());
        System.out.println(step.getText());
        return step;
    }
}
