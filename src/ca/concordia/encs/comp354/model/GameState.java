package ca.concordia.encs.comp354.model;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;

public abstract class GameState {

    public abstract ReadOnlyObjectProperty<Board> boardProperty();
    
    public abstract ReadOnlyObjectProperty<Team> turnProperty();
    
    public abstract ReadOnlyObjectProperty<GameAction> lastActionProperty();
    
    public abstract ReadOnlyIntegerProperty redScoreProperty();
    
    public abstract ReadOnlyIntegerProperty blueScoreProperty();
}
