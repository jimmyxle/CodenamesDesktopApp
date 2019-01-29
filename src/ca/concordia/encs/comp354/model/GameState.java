package ca.concordia.encs.comp354.model;

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
import javafx.collections.ObservableSet;

/**
 * Represents the game model. Specifies no behaviour, other than checking that mutator inputs are valid.
 * @author Nikita Leonidov
 *
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
    public ObjectProperty<Team> turnProperty() {
        return turn;
    }

    @Override
    public ReadOnlyObjectProperty<GameAction> lastActionProperty() {
        return action;
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
        action.set(value);
        event.set(value.apply(this));
        return event.get();
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
