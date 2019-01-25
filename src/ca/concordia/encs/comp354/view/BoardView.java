package ca.concordia.encs.comp354.view;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.CardValue;
import ca.concordia.encs.comp354.model.Coordinates;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
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
    
    private static final String BOARD_VIEW_STYLE_CLASS      = "board-view";
    private static final String KEYCARD_REGION_STYLE_CLASS  = "keycard-region";
    private static final String CODENAME_REGION_STYLE_CLASS = "codename-region";
    private static final String OVERLAY_REGION_STYLE_CLASS   = "overlay";
    
    private final GridPane tiles = new GridPane();
    private final GridPane teams = new GridPane();
    
    private final Map<Coordinates, CodenameRegion> codenames = new HashMap<>();
    
    private final ObjectProperty<Board> board = new SimpleObjectProperty<Board>(this, "board") {
        @Override protected void invalidated() {
            refreshContent(true);
        }
    };
    
    private final ObjectProperty<ObservableSet<Coordinates>> revealed = new SimpleObjectProperty<ObservableSet<Coordinates>>(this, "revealed") {
        ObservableSet<Coordinates>     last     = null;
        SetChangeListener<Coordinates> listener = BoardView.this::updateCodenameMarked;
        
        @Override protected void invalidated() {
            refreshContent(false);
            
            // watch for changes
            if (last!=null) {
                last.removeListener(listener);
            }
            
            ObservableSet<Coordinates> val = get();
            if (val!=null) {
                val.addListener(listener);
            }
            
            last = val;
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
        return board.get();
    }
    
    public Property<Board> boardProperty() {
        return board;
    }
    
    public void setRevealed(ObservableSet<Coordinates> value) {
        revealedProperty().setValue(value);
    }
    
    public ObservableSet<Coordinates> getRevealed() {
        return revealedProperty().getValue();
    }
    
    public Property<ObservableSet<Coordinates>> revealedProperty() {
        return revealed;
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
    
    private final class CodenameRegion extends StackPane {
        
        private final CardValue value;
        
        private final Region markedRegion;
        
        private boolean marked = false;
        
        CodenameRegion(String text, CardValue value) {
            this.value = value;
            
            final Label label = new Label(text);
            markedRegion = new Region();
            markedRegion.getStyleClass().clear();
            markedRegion.getStyleClass().add(OVERLAY_REGION_STYLE_CLASS);
            markedRegion.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
            
            getStyleClass().clear();
            getStyleClass().add(CODENAME_REGION_STYLE_CLASS);
            getChildren().addAll(label, markedRegion);
        }
        
        void setMarked(boolean v) {
            marked = v;
            PseudoClass pseudoClass = PseudoClass.getPseudoClass(value.name().toLowerCase(Locale.ENGLISH));
            pseudoClassStateChanged(pseudoClass, marked);
        }
    }
    
    /**
     * A region that displays whether a given space on the keycard is red, blue, neutral, or the assassin.
     * 
     * @author Nikita Leonidov
     *
     */
    private final class KeycardRegion extends Region {

        KeycardRegion(CardValue value) {
            getStyleClass().clear();
            getStyleClass().add(KEYCARD_REGION_STYLE_CLASS);
            
            String pseudoClass = value.name().toLowerCase(Locale.ENGLISH);
            pseudoClassStateChanged(PseudoClass.getPseudoClass(pseudoClass), true);
            
            setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        }
    }
    
    private void updateCodenameMarked(SetChangeListener.Change<? extends Coordinates> change) {
        if (change.wasAdded()) {
            codenames.get(change.getElementAdded()).setMarked(true);
        }
        
        if (change.wasRemoved()) {
            codenames.get(change.getElementRemoved()).setMarked(false);
        }
    }

    private void refreshContent(boolean rebuildBoard) {
     // the value has changed; clear the board display
        if (rebuildBoard) {
            tiles.getChildren().clear();
            teams.getChildren().clear();
        }
        
        final Board val = board.get();
        
        // leave empty
        if (val==null) {
            return;
        }
        
        if (rebuildBoard) {
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
                    // create codename card
                    CodenameRegion reg = new CodenameRegion(val.getWord(x, y), val.getValue(x, y));
                    codenames.put(new Coordinates(x, y), reg);
                    tiles.add(reg, x, y);
                    
                    // create keycard element
                    teams.add(new KeycardRegion(val.getValue(x, y)), x, y);
                }
            }
        }
        
        // update overlay
        //--------------------------------------------------------------------------------------------------------------
        ObservableSet<Coordinates> markedNames = revealed.get();
        if (markedNames!=null) {
            for (Map.Entry<Coordinates, CodenameRegion> e : codenames.entrySet()) {
                e.getValue().setMarked(markedNames.contains(e.getKey()));
            }
        }
    }
}
