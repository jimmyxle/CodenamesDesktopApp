package ca.concordia.encs.comp354.view;

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

import static ca.concordia.encs.comp354.model.Team.*;

import java.util.Locale;

/**
 * Displays "turn changed" events.
 * @author Mykyta Leonidov
 *
 */
public class TurnView extends StackPane {
    
    private final ObjectProperty<Team> turn = new SimpleObjectProperty<>(this, "turn", null);
    
    private final Label redTurn, blueTurn;

    private Animation lastAnim = null;
    
    private static final Duration FADE_DURATION = Duration.millis(300);
    
    TurnView() {
        redTurn  = endLabel(RED,  "Red's turn");
        blueTurn = endLabel(BLUE, "Blue's turn");
        
        getChildren().addAll(redTurn, blueTurn);
        turn.addListener(this::onTurn);
    }
    
    ObjectProperty<Team> turnProperty() {
        return turn;
    }
    
    private void onTurn(ObservableValue<? extends Team> val, Team prev, Team next) {
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
        
        switch (next) {
        case RED:
            show(redTurn);
            break;
        case BLUE:
            show(blueTurn);
            break;
        }
    }
    
    private void show(Label node) {
        node.setVisible(true);
        
        FadeTransition in = new FadeTransition();
        in.setFromValue(0);
        in.setToValue(1);
        in.setDuration(FADE_DURATION);
        
        PauseTransition pt = new PauseTransition();
        pt.setDuration(FADE_DURATION);
        
        FadeTransition out = new FadeTransition();
        out.setFromValue(1);
        out.setToValue(0);
        out.setDuration(FADE_DURATION);
        
        SequentialTransition seq = new SequentialTransition(in, pt, out);
        seq.setNode(node);
        
        seq.setOnFinished(e->node.setVisible(false));
        
        lastAnim = seq;
        lastAnim.play();
    }

    private Label endLabel(Team show, String label) {
        Label ret = new Label(label);
        
        ret.setPrefWidth(Double.MAX_VALUE);
        ret.setTextAlignment(TextAlignment.CENTER);
        ret.getStyleClass().add("next-turn");
        ret.pseudoClassStateChanged(PseudoClass.getPseudoClass(show.name().toLowerCase(Locale.ENGLISH)), true);
        
        // show only for specific game event
        ret.setVisible(false);
        
        return ret;
    }
}
