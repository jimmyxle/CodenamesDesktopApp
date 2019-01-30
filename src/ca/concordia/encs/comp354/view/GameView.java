package ca.concordia.encs.comp354.view;

import java.util.Objects;

import ca.concordia.encs.comp354.model.ReadOnlyGameState;
import javafx.beans.InvalidationListener;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.beans.Observable;

/**
 * A graphical interface for an active game.
 * @author Nikita
 *
 */
public class GameView extends StackPane {
    
    public interface Controller {
        void advanceTurn();
    }
    
    private final ReadOnlyGameState game;

    private final BorderPane root;
    private final BoardView  boardView;
    private final ScoreView  scoreView;
    private final StateView  stateView;
    private final Button     advance;
    
    private final InvalidationListener advanceFreeze = this::onAdvanceChanged;
    
    public GameView(ReadOnlyGameState game, Controller controller) {
        Objects.requireNonNull(controller, "controller");
        this.game = Objects.requireNonNull(game, "game state");
        
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
        
        advance = new Button("Advance");
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
        
        stateView.actionProperty().bind(game.lastActionProperty());
        
        game.redScoreProperty()     .addListener(advanceFreeze);
        game.redObjectiveProperty() .addListener(advanceFreeze);
        game.blueScoreProperty()    .addListener(advanceFreeze);
        game.blueObjectiveProperty().addListener(advanceFreeze);
        game.lastEventProperty()    .addListener(advanceFreeze);
        
        advance.setOnAction(event->controller.advanceTurn());
    }
    
    private void onAdvanceChanged(Observable ignore) {
    	advance.setDisable(
			game.getRedScore()  >= game.redObjectiveProperty().get() ||
			game.getBlueScore() >= game.blueObjectiveProperty().get() ||
			game.getLastEvent().isTerminal()
		);
    }
    
}
