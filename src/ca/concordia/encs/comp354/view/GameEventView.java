package ca.concordia.encs.comp354.view;

import ca.concordia.encs.comp354.controller.GameEvent;
import ca.concordia.encs.comp354.model.GameStep;
import ca.concordia.encs.comp354.model.Team;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import static ca.concordia.encs.comp354.controller.GameEvent.*;

/**
 * Displays "game over" event overlays.
 * @author Nikita Leonidov
 *
 */
public class GameEventView extends StackPane {
    
    private final ObjectProperty<GameStep> event = new SimpleObjectProperty<>(this, "event", null);
    
    private final Label redWins, blueWins, assassin;

    private Animation lastAnim = null;
    
    private static final Duration FADE_DURATION  = Duration.millis(500);
    private static final Duration PAUSE_DURATION = Duration.millis(500);
    
    GameEventView() {
        redWins  = endLabel(GAME_OVER_RED_WON,  "Red wins!",  "red");
        blueWins = endLabel(GAME_OVER_BLUE_WON, "Blue wins!", "blue");
        assassin = endLabel(GAME_OVER_ASSASSIN, "Assassin!",  "assassin");
        
        getChildren().addAll(redWins, blueWins, assassin);
        event.addListener(this::onGameEvent);
    }
    
    ObjectProperty<GameStep> stepProperty() {
        return event;
    }
    
    private void onGameEvent(ObservableValue<? extends GameStep> val, GameStep prev, GameStep next) {
        if (lastAnim!=null) {
            lastAnim.stop();
            lastAnim = null;
        }
        
        for (Node k : getChildren()) {
            k.setVisible(false);
        }
        
        if (next==null) {
        	return;
        }
        
        switch (next.getEvent()) {
        case GAME_OVER_ASSASSIN:
            show(next.getAction().getTeam()==Team.RED?blueWins:redWins, true);
            break;
        case GAME_OVER_BLUE_WON:
            show(blueWins, false);
            break;
        case GAME_OVER_RED_WON:
            show(redWins, false);
            break;
        case END_TURN: // fallthrough
        case NONE:
            break;
        
        }
    }
    
    private void show(Label node, boolean showAssassin) {
        node.setVisible(true);
        node.setOpacity(0);
        
        FadeTransition ft = new FadeTransition();
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setDuration(FADE_DURATION);
        ft.setNode(node);
        ft.setAutoReverse(true);
        ft.setCycleCount(1);
        
        if (showAssassin) {
            assassin.setVisible(true);
            
            FadeTransition in = new FadeTransition();
            in.setFromValue(0);
            in.setToValue(1);
            in.setDuration(FADE_DURATION);
            in.setNode(assassin);
            
            FadeTransition out = new FadeTransition();
            out.setFromValue(1);
            out.setToValue(0);
            out.setDuration(FADE_DURATION);
            out.setNode(assassin);
            
            out.setOnFinished(e->assassin.setVisible(false));
            
            lastAnim = new SequentialTransition(in, new PauseTransition(PAUSE_DURATION), out, ft);
        } else {
            lastAnim = ft;
        }
        
        lastAnim.play();
    }

    private Label endLabel(GameEvent show, String label, String pseudoClass) {
        Label ret = new Label(label);
        
        ret.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        ret.setTextAlignment(TextAlignment.CENTER);
        ret.getStyleClass().add("game-over");
        ret.pseudoClassStateChanged(PseudoClass.getPseudoClass(pseudoClass), true);
        
        // show only for specific game event
        ret.setVisible(false);
        
        return ret;
    }
}
