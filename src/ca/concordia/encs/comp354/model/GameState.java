package ca.concordia.encs.comp354.model;

import java.util.Objects;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

public class GameState implements ReadOnlyGameState {

    private final ObjectProperty<Board>      board  = new SimpleObjectProperty<>(this, "board");
    private final ObjectProperty<Team>       turn   = new SimpleObjectProperty<>(this, "turn",  Team.RED);
    private final ObjectProperty<GameAction> action = new SimpleObjectProperty<>(this, "action");
    
    private final IntegerProperty redScore  = new SimpleIntegerProperty(this, "redScore", 1);
    private final IntegerProperty blueScore = new SimpleIntegerProperty(this, "redScore", 2);
    
    private final ObservableSet<Coordinates> chosen         = FXCollections.observableSet();
    private final ObservableSet<Coordinates> readOnlyChosen = FXCollections.unmodifiableObservableSet(chosen);
    
    public GameState(Board board, Team startingTurn) {
        this.board.set(Objects.requireNonNull(board, "board"));
        turnProperty().set(Objects.requireNonNull(startingTurn, "startingTurn"));
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
    
    public void pushAction(GameAction value) {
        Objects.requireNonNull(value);
        action.set(value);
        value.apply(this);
    }

    @Override
    public IntegerProperty redScoreProperty() {
        return redScore;
    }

    @Override
    public IntegerProperty blueScoreProperty() {
        return blueScore;
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
