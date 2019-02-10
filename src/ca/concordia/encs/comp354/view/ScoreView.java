package ca.concordia.encs.comp354.view;

import ca.concordia.encs.comp354.controller.Clue;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Duration;

/**
 * Displays team scores in a JavaFX node.
 * @author Mykyta Leonidov
 *
 */
public class ScoreView extends HBox {
    
    private static final String STYLE_CLASS = "score-view";
    private static final String SCORE_READOUT_STYLE_CLASS = "score-readout";
    private static final String CLUE_READOUT_STYLE_CLASS  = "clue-readout";

    private final Label clueLabel = new Label();
    private final Label redLabel  = new Label();
    private final Label blueLabel = new Label();
    
    private final Transition redAnim  = animate(redLabel);
    private final Transition blueAnim = animate(blueLabel);
    
    private final IntegerProperty redScore  = new SimpleIntegerProperty(this, "redScore",  0) {
        @Override protected void invalidated() {
            updateScores();
            redAnim.play();
        }
    };
    
    private final IntegerProperty blueScore = new SimpleIntegerProperty(this, "blueScore", 0) {
        @Override protected void invalidated() {
            updateScores();
            blueAnim.play();
        }
    };
    
    private final ObjectProperty<Clue> clue = new SimpleObjectProperty<Clue>(this, "clue", null) {
        @Override protected void invalidated() {
            Clue c = clue.get();
            clueLabel.setText(c==null?"":String.format("Clue: %s %d", c.getWord(), c.getGuesses()));
        }
    };
    
    public ScoreView() {
        getStyleClass().clear();
        getStyleClass().add(STYLE_CLASS);
        
        clueLabel.getStyleClass().clear();
        clueLabel.getStyleClass().add(CLUE_READOUT_STYLE_CLASS);
        
        redLabel.getStyleClass().clear();
        redLabel.getStyleClass().add(SCORE_READOUT_STYLE_CLASS);
        redLabel.pseudoClassStateChanged(PseudoClass.getPseudoClass("red"), true);
        
        blueLabel.getStyleClass().clear();
        blueLabel.getStyleClass().add(SCORE_READOUT_STYLE_CLASS);
        blueLabel.pseudoClassStateChanged(PseudoClass.getPseudoClass("blue"), true);
        
        getChildren().addAll(clueLabel, redLabel, blueLabel);
        
        clueLabel.minWidthProperty().bind(widthProperty().multiply(0.333));
        redLabel .minWidthProperty().bind(widthProperty().multiply(0.333));
        blueLabel.minWidthProperty().bind(widthProperty().multiply(0.333));

        HBox.setHgrow(clueLabel, Priority.ALWAYS);
        HBox.setHgrow(redLabel,  Priority.ALWAYS);
        HBox.setHgrow(blueLabel, Priority.ALWAYS);
        
        updateScores();
    }
    
    private Transition animate(Node node) {
        ScaleTransition scale = new ScaleTransition();
        
        final double x = node.getScaleX();
        final double y = node.getScaleY();
        
        scale.setFromX(x);
        scale.setFromY(y);
        scale.setToX(x * 1.2);
        scale.setToY(y * 1.2);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        scale.setDuration(Duration.millis(250));
        scale.setNode(node);
        scale.setOnFinished(e->scale.stop());
        
        return scale;
    }
    
    public void setRedScore(int value) {
        redScoreProperty().set(value);
    }
    
    public int getRedScore() {
        return redScoreProperty().get();
    }
    
    public IntegerProperty redScoreProperty() {
        return redScore;
    }
    
    public void setBlueScore(int value) {
        blueScoreProperty().set(value);
    }
    
    public int getBlueScore() {
        return blueScoreProperty().get();
    }
    
    public IntegerProperty blueScoreProperty() {
        return blueScore;
    }
    
    public ObjectProperty<Clue> clueProperty() {
        return clue;
    }
    
    private void updateScores() {
        redLabel.setText (Integer.toString(getRedScore()));
        blueLabel.setText(Integer.toString(getBlueScore()));
        
        boolean redLead  = getRedScore() > getBlueScore();
        boolean blueLead = getBlueScore() > getRedScore();
        
        redLabel.pseudoClassStateChanged(PseudoClass.getPseudoClass("lead"),  redLead);
        blueLabel.pseudoClassStateChanged(PseudoClass.getPseudoClass("lead"), blueLead);
        
    }
    
}
