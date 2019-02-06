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

    private final ObjectProperty<Board>      board  = new SimpleObjectProperty<>(this, "board");
    private final ObjectProperty<Team>       turn   = new SimpleObjectProperty<>(this, "turn",  Team.RED);
    private final ObjectProperty<GameAction> action = new SimpleObjectProperty<>(this, "action");
    private final ObjectProperty<GameEvent>  event  = new SimpleObjectProperty<>(this, "event", GameEvent.NONE);
    private final ObjectProperty<Clue>       clue   = new SimpleObjectProperty<>(this, "clue",  null);
    
    private final IntegerProperty redScore  = new SimpleIntegerProperty(this, "redScore", 0);
    private final IntegerProperty blueScore = new SimpleIntegerProperty(this, "redScore", 0);
    
    private final ReadOnlyIntegerProperty redObjective;
    private final ReadOnlyIntegerProperty blueObjective;
    
    private final ObservableSet<Coordinates> chosen         = FXCollections.observableSet();
    private final ObservableSet<Coordinates> readOnlyChosen = FXCollections.unmodifiableObservableSet(chosen);
    
    private final ObservableList<GameStep> history         = FXCollections.observableList(new ArrayList<>());
    private final ObservableList<GameStep> readOnlyHistory = FXCollections.unmodifiableObservableList(history);
    
    public GameState(Board board, Team startingTurn) {
        this.board.set(Objects.requireNonNull(board, "board"));
        turnProperty().set(Objects.requireNonNull(startingTurn, "startingTurn"));
        
        // count red, blue cards on board to determine objectives
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
    
    public GameEvent pushAction(GameAction value) {
        Objects.requireNonNull(value);
        
        // add action to model, execute action, add event to model
        action.set(value);
        event.set(value.apply(this));
        
        // log game step
        GameStep step = new GameStep(action.get(), event.get(), redScore.get(), blueScore.get(), history.size());
        history.add(step);
        System.out.println(step.getText());
        
        return event.get();
    }

    public GameAction popAction() {
        // undo most recent action
        GameAction ret = history.remove(history.size()-1).getAction();
        ret.undo(this);
        
        // re-apply previous action
        if (!history.isEmpty()) {
            GameAction next = history.remove(history.size()-1).getAction();
            pushAction(next);
        }
        
        // lastly, return the undone action
        return ret;
    }
    
    @Override
    public ObservableList<GameStep> getHistory() {
        return readOnlyHistory;
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
    
    public void chooseCard(Coordinates coords) {
        Objects.requireNonNull(coords, "coordinates");
        
        if (coords.getX() < 0 || coords.getX() > getBoard().getWidth()) {
            throw new IllegalArgumentException("illegal x coordinate "+coords.getX()+"; must lie in [0, "+getBoard().getWidth()+")");
        }
        
        if (coords.getY() < 0 || coords.getY() > getBoard().getLength()) {
            throw new IllegalArgumentException("illegal y coordinate "+coords.getY()+"; must lie in [0, "+getBoard().getLength()+")");
        }
        
        chosen.add(coords);
    }

}
