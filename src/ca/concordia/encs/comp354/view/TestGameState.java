package ca.concordia.encs.comp354.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.GameAction;
import ca.concordia.encs.comp354.model.Team;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

/**
 * Dummy implementation of GameState to feed fake model data to the GUI.
 * 
 * @author Nikita Leonidov
 *
 */
public class TestGameState extends GameState {

    private final ObjectProperty<Board>      board  = new SimpleObjectProperty<>(this, "board", new TestBoard());
    private final ObjectProperty<Team>       turn   = new SimpleObjectProperty<>(this, "turn",  Team.RED);
    private final ObjectProperty<GameAction> action = new SimpleObjectProperty<>(this, "action");
    
    private final IntegerProperty redScore  = new SimpleIntegerProperty(this, "redScore", 1);
    private final IntegerProperty blueScore = new SimpleIntegerProperty(this, "redScore", 2);
    
    private List<TestAction> actions = Arrays.asList(
            new TestAction(Team.RED,  "spymaster is thinking of a hint"),
            new TestAction(Team.RED,  "spymaster hints: fizz 2"),
            new TestAction(Team.RED,  "is guessing"),
            new TestAction(Team.RED,  "guessed: foo, bar"),
            new TestAction(Team.BLUE, "spymaster is thinking of a hint"),
            new TestAction(Team.BLUE, "spymaster hints: buzz 3"),
            new TestAction(Team.BLUE, "is guessing"),
            new TestAction(Team.BLUE, "guessed: foo, bar, baz")
    );
    
    private int actionIndex = 0;
    
    private final ObservableSet<Coordinates> revealed = FXCollections.observableSet();
    private final List<Coordinates>          hidden   = new ArrayList<>();
    
    public TestGameState() {
        pushAction();
        
        // create a randomly ordered list of spaces to reveal
        for (int x=0; x<5; x++) {
            for (int y=0; y<5; y++) {
                hidden.add(new Coordinates(x, y));
            }
        }
        
        Collections.shuffle(hidden);
    }
    
    public void advance() {
        actionIndex = (actionIndex+1)%actions.size();
        
        // show a random space
        if (!hidden.isEmpty() && actionIndex!=0 && (actionIndex%3)==0) {
            revealed.add(hidden.remove(hidden.size()-1));
        }
        
        pushAction();
    }
    
    @Override
    public ReadOnlyObjectProperty<Board> boardProperty() {
        return board;
    }

    @Override
    public ReadOnlyObjectProperty<Team> turnProperty() {
        return turn;
    }

    @Override
    public ReadOnlyObjectProperty<GameAction> lastActionProperty() {
        return action;
    }

    @Override
    public ReadOnlyIntegerProperty redScoreProperty() {
        return redScore;
    }

    @Override
    public ReadOnlyIntegerProperty blueScoreProperty() {
        return blueScore;
    }

    @Override
    public ObservableSet<Coordinates> getChosenCards() {
        return revealed;
    }
    
    private void pushAction() {
        turn.set(actions.get(actionIndex).getTeam());
        action.set(actions.get(actionIndex));
    }

}
