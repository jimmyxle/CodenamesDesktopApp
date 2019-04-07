package ca.concordia.encs.comp354.model;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Supplier;

import ca.concordia.encs.comp354.CompletablePromise;
import ca.concordia.encs.comp354.Promise;
import ca.concordia.encs.comp354.controller.Clue;
import ca.concordia.encs.comp354.controller.GameAction;
import ca.concordia.encs.comp354.controller.GameEvent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

/**
 * <p> Represents the game model. Specifies no behaviour, other than checking that mutator inputs are valid. The view 
 * should only receive instances of this class as {@link ReadOnlyGameState}s, ensuring that only the controller has 
 * authority to mutate the object.
 * 
 * <p> The view can watch for changes to a property of this class by attaching listeners, or binding its own properties
 * to it. This vastly simplifies the construction of both controller and GUI code.
 * 
 * <p> This class also serves as a command manager. Apply {@link GameAction}s using {@link #pushAction(GameAction)};
 * undo and redo with {@link #undoAction()} and {@link #redoAction()}, respectively. Read-only views of the game history 
 * and the undo queue may be accessed with {@link #getHistory()} and {@link #getUndone()}.
 * 
 * @author Mykyta Leonidov
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

    private final PrintStream log;
    private final Supplier<Board> boardFunc;
    
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
    
    // user input
    //------------------------------------------------------------------------------------------------------------------
    private final BooleanProperty actionInProgress = new SimpleBooleanProperty(this, "actionInProgress", false);
    
    private final ObjectProperty<CompletablePromise<OperativeEvent>> requestedEvent = 
            new SimpleObjectProperty<>(this, "requestedEvent", null);
    
    // history ("command queue")
    //------------------------------------------------------------------------------------------------------------------
    // closest javafx offers to observable deques is lists; use this class's pop(), peek() convenience methods to modify
    private final ObservableList<GameStep> history         = FXCollections.observableList(new ArrayList<>());
    private final ObservableList<GameStep> readOnlyHistory = FXCollections.unmodifiableObservableList(history);
    private final ObservableList<GameStep> undone          = FXCollections.observableList(new ArrayList<>());
    private final ObservableList<GameStep> readOnlyUndone  = FXCollections.unmodifiableObservableList(undone);
    
    // values at the top of history stack; these make it easier for the view to watch for changes in state
    private final ObjectProperty<GameStep>   step    = new SimpleObjectProperty<>(this, "step",    null);
    private final ObjectProperty<GameAction> action  = new SimpleObjectProperty<>(this, "action",  null);
    private final ObjectProperty<GameEvent>  event   = new SimpleObjectProperty<>(this, "event",   GameEvent.NONE);
    
    public GameState(Board board) {
    	this(()->board);
    }
    
    public GameState(Supplier<Board> boardFunc) {
        this(boardFunc, null);
    }
    
    public GameState(Supplier<Board> boardFunc, PrintStream logOut) {
    	this.boardFunc = Objects.requireNonNull(boardFunc, "board supplier");
        generateBoard();
        
        this.log = logOut;
        
        // count red, blue cards on board to determine objectives
        int redCount  = 0;
        int blueCount = 0;
        
        for (int x=0; x<getBoard().getWidth(); x++) {
            for (int y=0; y<getBoard().getLength(); y++) {
                switch (getBoard().getCard(x, y).getValue()) {
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
        getHistory().addListener((Change<?> c)->recordStep(peek(getHistory())));
    }
    
    private void generateBoard() {
    	board.set(Objects.requireNonNull(boardFunc.get()));
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
    
    public GameEvent pushAction(GameAction value) {
        cancelInput();
        applyAction(value);
        // clear undo history
        undone.clear();
        return event.get();
    }

    public boolean undoAction() {
    	if (history.isEmpty()) {
    		return false;
    	}
        cancelInput();
    	
        // undo most recent action
    	GameStep top = peek(history); // update history after successful undo -- just peek, modify collection later
        top.getAction().undo(this);
        pop(history);
        undone.add(top);
        
        // lastly, return the undone action
        return true;
    }
    
    public boolean redoAction() {
    	if (undone.isEmpty()) {
    		return false;
    	}

        cancelInput();
    	applyAction(pop(undone).getAction());
    	
    	return true;
    }
    
    @Override
    public ObservableList<GameStep> getHistory() {
        return readOnlyHistory;
    }

    @Override
    public ReadOnlyBooleanProperty actionInProgressProperty() {
        return actionInProgress;
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
            throw new IllegalArgumentException(
                    "illegal x coordinate "+coords.getX()+
                    "; must lie in [0, "+getBoard().getWidth()+")");
        }
        
        if (coords.getY() < 0 || coords.getY() > getBoard().getLength()) {
            throw new IllegalArgumentException(
                    "illegal y coordinate "+coords.getY()+
                    "; must lie in [0, "+getBoard().getLength()+")");
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
    	
    	if (actionInProgress.get()) {
    	    throw new IllegalStateException("an action may not be applied with another action already in progress");
    	}
        
        // add action to model, execute action, record step
    	actionInProgress.set(true);
    	// value.apply() returns a Promise!
    	value.apply(this).then(event->{
        	GameStep step = new GameStep(value, event, redScore.get(), blueScore.get(), history.size());
            history.add(step);
            actionInProgress.set(false);
    	});
    }
    
    /**
     * Notifies the view that a new step has appeared at the top of the history stack.
     * @param k  the history element at the top of the history stack
     * @return <tt>k</tt>
     */
    private GameStep recordStep(GameStep k) {
        if (k==null) {
            action.set(null);
            event.set(GameEvent.NONE);
            step.set(null);
        } else {
            action.set(k.getAction());
            event.set(k.getEvent());
            step.set(k);
            if (log!=null) {
            	log.println(k.getText());
            }
        }
        return k;
    }
    
    /**
     * Requests operative input from the view. The event representing the user's input will be placed in the given promise.
     * @return the destination promise for the input event
     */
    public Promise<OperativeEvent> requestOperativeInput() {
        if (requestedEvent.get()!=null) {
            throw new IllegalStateException("guess already in progress");
        }
        CompletablePromise<OperativeEvent> ret = new CompletablePromise<>();
        requestedEvent.set(ret);
        return ret.then(v->requestedEvent.set(null));
    }
    
    private void cancelInput() {
        CompletablePromise<OperativeEvent> p = requestedEvent.get();
        if (p!=null) {
            requestedEvent.set(null);
            p.cancel();
        }
    }
    
    @Override
    public ReadOnlyObjectProperty<CompletablePromise<OperativeEvent>> operativeInputProperty() {
        return requestedEvent;
    }
        
    /*
     * Reset the game state. All properties are being reset to the original values.
     * 
     */
    public void reset() {
    		   	
    	//reset the board	
    	generateBoard();
    	
    	//reset all values to the original properties    
    	turn.set(null);
    	clue.set(null);
    	redScore.set(0);
    	blueScore.set(0);
    	guesses.set(0);
    	
    	chosen.clear();

    	//clear history and undo
    	history.clear();
    	undone.clear();
    
        requestedEvent.set(null);
    }
}