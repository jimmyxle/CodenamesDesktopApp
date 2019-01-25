package ca.concordia.encs.comp354.view;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.css.PseudoClass;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Displays team scores in a JavaFX node.
 * @author Nikita
 *
 */
public class ScoreView extends AnchorPane {
    
    private static final String STYLE_CLASS = "score-view";
    private static final String SCORE_READOUT_STYLE_CLASS = "score-readout";

    private final Label redLabel  = new Label();
    private final Label blueLabel = new Label();
    
    private final IntegerProperty redScore  = new SimpleIntegerProperty(this, "redScore",  0) {
        @Override protected void invalidated() {
            updateScores();
        }
    };
    
    private final IntegerProperty blueScore = new SimpleIntegerProperty(this, "blueScore", 0) {
        @Override protected void invalidated() {
            updateScores();
        }
    };
    
    public ScoreView() {
        getStyleClass().clear();
        getStyleClass().add(STYLE_CLASS);
        
        final HBox root = new HBox();
        
        redLabel.getStyleClass().clear();
        redLabel.getStyleClass().add(SCORE_READOUT_STYLE_CLASS);
        redLabel.pseudoClassStateChanged(PseudoClass.getPseudoClass("red"), true);
        
        blueLabel.getStyleClass().clear();
        blueLabel.getStyleClass().add(SCORE_READOUT_STYLE_CLASS);
        blueLabel.pseudoClassStateChanged(PseudoClass.getPseudoClass("blue"), true);
        
        root.getChildren().addAll(redLabel, blueLabel);
        getChildren().add(root);
        
        redLabel.minWidthProperty().bind(widthProperty().multiply(0.5));
        blueLabel.minWidthProperty().bind(widthProperty().multiply(0.5));
        
        HBox.setHgrow(redLabel,  Priority.ALWAYS);
        HBox.setHgrow(blueLabel, Priority.ALWAYS);
        
        updateScores();
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
    
    private void updateScores() {
        redLabel.setText (Integer.toString(getRedScore()));
        blueLabel.setText(Integer.toString(getBlueScore()));
    }
    
}
