package ca.concordia.encs.comp354.view;

import java.util.Locale;

import ca.concordia.encs.comp354.controller.GameAction;
import ca.concordia.encs.comp354.model.Team;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Displays game actions and turns in a JavaFX node.
 * @author Mykyta Leonidov
 *
 */
public class StateView extends StackPane {
    
    private static final String STYLE_CLASS = "state-view";
    
    private final ObjectProperty<GameAction> action = new SimpleObjectProperty<GameAction>(this, "action") {
        @Override protected void invalidated() {
            updateState();
        }
    };
    
    private final Label label = new Label();
    
    public StateView() {
        getStyleClass().clear();
        getStyleClass().add(STYLE_CLASS);
        
        getChildren().add(label);
    }
    
    public void setAction(GameAction value) {
        actionProperty().set(value);
    }
    
    public GameAction getAction() {
        return actionProperty().get();
    }
    
    public ObjectProperty<GameAction> actionProperty() {
        return action;
    }

    protected void updateState() {
        
        GameAction action = getAction();
        Team       turn   = action==null? Team.RED : action.getTeam();
        
        String actText = action==null? "???" : action.getActionText();
        label.setText(actText);
        
        for (Team k : Team.values()) {
            pseudoClassStateChanged(PseudoClass.getPseudoClass(k.name().toLowerCase(Locale.ENGLISH)), false);
        }
        
        pseudoClassStateChanged(PseudoClass.getPseudoClass(turn.name().toLowerCase(Locale.ENGLISH)), true);
    }
    
}
