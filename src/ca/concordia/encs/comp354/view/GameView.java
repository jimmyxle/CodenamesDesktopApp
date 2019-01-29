package ca.concordia.encs.comp354.view;

import java.util.Objects;

import ca.concordia.encs.comp354.model.ReadOnlyGameState;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

/**
 * A graphical interface for an active game.
 * @author Nikita
 *
 */
public class GameView extends StackPane {
    
    public interface Controller {
        void advanceTurn();
    }

    private final BorderPane root;
    private final BoardView  boardView;
    private final ScoreView  scoreView;
    private final StateView  stateView;
    
    public GameView(ReadOnlyGameState game, Controller controller) {
        Objects.requireNonNull(controller, "controller");
        
        root = new BorderPane();
        getChildren().add(root);
        
        // create game view elements
        //--------------------------------------------------------------------------------------------------------------
        // centre: board
        boardView = new BoardView();
        boardView.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        root.setCenter(boardView);
        
        // top: score, show overlay
        final HBox top = new HBox();
        scoreView = new ScoreView();
        
        final CheckBox showOverlay = new CheckBox("Show Keycard");
        top.getChildren().addAll(scoreView, showOverlay);
        
        HBox.setHgrow(scoreView, Priority.ALWAYS);
        
        root.setTop(top);
        
        // bottom: status, advance turn
        final HBox bottom = new HBox();
        stateView = new StateView();
        
        final Button advance = new Button("Advance");
        bottom.getChildren().addAll(stateView, advance);
        
        HBox.setHgrow(stateView, Priority.ALWAYS);
        
        root.setBottom(bottom);
        
        // bind elements to model, controller
        //--------------------------------------------------------------------------------------------------------------
        boardView.boardProperty().bind(game.boardProperty());
        boardView.setRevealed(game.getChosenCards());
        boardView.keycardVisibleProperty().bind(showOverlay.selectedProperty());
        
        scoreView.redScoreProperty().bind(game.redScoreProperty());
        scoreView.blueScoreProperty().bind(game.blueScoreProperty());
        
        stateView.turnProperty().bind(game.turnProperty());
        stateView.actionProperty().bind(game.lastActionProperty());
        
        advance.setOnAction(event->controller.advanceTurn());
        advance.disableProperty().bind(
            game.redScoreProperty().greaterThanOrEqualTo(game.redObjectiveProperty())
            .or(game.blueScoreProperty().greaterThanOrEqualTo(game.blueObjectiveProperty()))
        );
        
    }
    
}
