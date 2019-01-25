package ca.concordia.encs.comp354.view;

import java.util.Locale;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.CardValue;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

/**
 * JavaFX node for the display of a board's state.
 * 
 * @author Nikita Leonidov
 * 
 */
public class BoardView extends StackPane {
    
    private static final String BOARD_VIEW_STYLE_CLASS  = "board-view";
    private static final String TEAM_REGION_STYLE_CLASS = "team-region";
    
    private final GridPane tiles = new GridPane();
    private final GridPane teams = new GridPane();
    
    private final ObjectProperty<Board> board = new SimpleObjectProperty<Board>(this, "board") {
        @Override protected void invalidated() {
            // the value has changed; clear the board display
            tiles.getChildren().clear();
            final Board val = get();
            
            // leave empty
            if (val==null) {
                return;
            }
            
            // else populate grid panes
            //----------------------------------------------------------------------------------------------------------
            // column constraints
            for (int i=0; i<val.getWidth(); i++) {
                ColumnConstraints col = new ColumnConstraints();
                col.setFillWidth(true);
                col.setHalignment(HPos.CENTER);
                col.setPercentWidth(100.0/val.getWidth());
                tiles.getColumnConstraints().add(col);
                teams.getColumnConstraints().add(col);
            }
            // row constraints
            for (int i=0; i<val.getLength(); i++) {
                RowConstraints row = new RowConstraints();
                row.setFillHeight(true);
                row.setValignment(VPos.CENTER);
                row.setPercentHeight(100.0/val.getLength());
                tiles.getRowConstraints().add(row);
                teams.getRowConstraints().add(row);
            }
            // child nodes
            for (int x=0; x<val.getWidth(); x++) {
                for (int y=0; y<val.getLength(); y++) {
                    tiles.add(new Label(val.getWord(x, y)), x, y);
                    teams.add(new TeamRegion(val.getValue(x, y)), x, y);
                }
            }
        }
    };
    
    public BoardView() {
        getStyleClass().clear();
        getStyleClass().add(BOARD_VIEW_STYLE_CLASS);
        
        this.getChildren().add(tiles);
        this.getChildren().add(teams);
    }
    
    public void setBoard(Board value) {
        boardProperty().setValue(value);
    }
    
    public Board getBoard() {
        return board==null? null : board.get();
    }
    
    public Property<Board> boardProperty() {
        return board;
    }
    
    public void setKeycardVisible(boolean value) {
        teams.setVisible(value);
    }
    
    public boolean isKeycardVisible() {
        return teams.isVisible();
    }
    
    public BooleanProperty keycardVisibleProperty() {
        return teams.visibleProperty();
    }
    
    /**
     * A region that displays whether a given space on the keycard is red, blue, neutral, or the assassin.
     * 
     * @author Nikita Leonidov
     *
     */
    private final class TeamRegion extends Region {

        public TeamRegion(CardValue value) {
            getStyleClass().clear();
            getStyleClass().add(TEAM_REGION_STYLE_CLASS);
            
            String pseudoClass = value.name().toLowerCase(Locale.ENGLISH);
            pseudoClassStateChanged(PseudoClass.getPseudoClass(pseudoClass), true);
            
            setPrefWidth(Double.MAX_VALUE);
            setPrefHeight(Double.MAX_VALUE);
        }
        
    }
}
