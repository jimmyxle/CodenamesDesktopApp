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
import javafx.geometry.Pos;

/**
 * A graphical interface for an active game.
 * @author Mykyta Leonidov
 *
 */
public class GameView extends StackPane {
    
    /**
     * Specifies the actions the GUI must be able to issue to the controller as a result of user input.
     * @author Mykyta Leonidov
     *
     */
    public interface Controller {
        /**
         * Advances the game to the next action.
         */
        void advanceTurn();
        /**
         * Reverses the most recently executed action.
         * @return <tt>true</tt> iff a previously executed action was available for this operation
         */
        boolean undoTurn();
        /**
         * Re-applies the most recently undone action.
         * @return <tt>true</tt> iff a previously undone action was available for this operation
         */
        boolean redoTurn();
        
        /**
         * Skips the turn of whoever's turn it currently is, and proceeds with the next action. 
         */
        boolean skipTurn(); 
    }
    
    private final ReadOnlyGameState game;

    private final BorderPane    root;
    private final BoardView     boardView;
    private final ScoreView     scoreView;
    private final StateView     stateView;
    private final GameEventView gameEventView;
    private final TurnView      turnView;
    private final Button        advance;
    
    private final InvalidationListener advanceFreeze = this::onAdvanceChanged;

    public GameView(ReadOnlyGameState game, Controller controller) {
        Objects.requireNonNull(controller, "controller");
        this.game = Objects.requireNonNull(game, "game state");
        
        root = new BorderPane();
        getChildren().add(root);
        root.getStyleClass().clear();
        root.getStyleClass().add("game-view");
        
        // create game view elements
        //--------------------------------------------------------------------------------------------------------------
        // centre: board, game over
        StackPane centre = new StackPane();
        boardView = new BoardView();
        boardView.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        
        gameEventView = new GameEventView();
        turnView = new TurnView();
        
        centre.getChildren().addAll(boardView, turnView, gameEventView);
        
        root.setCenter(centre);
        
        // top: score, show overlay
        final HBox top = new HBox();
        scoreView = new ScoreView();
        
        final CheckBox showOverlay = new CheckBox("Show Keycard");
        top.getChildren().addAll(scoreView, showOverlay);
        
        HBox.setHgrow(scoreView, Priority.ALWAYS);
        top.setAlignment(Pos.CENTER);
        
        root.setTop(top);
        
        // bottom: status, advance turn
        final HBox bottom = new HBox();
        final HBox history = new HBox();
        stateView = new StateView();
        
        advance = new Button("Advance");
        final Button undo = new Button("Undo");
        final Button redo = new Button("Redo");
        final Button skipTurn= new Button("Skip Turn"); 
        
        //this makes the undo, redo, advance, and skip turn buttons visible in the GUI
        history.getChildren().addAll(undo, redo, advance, skipTurn); 
        bottom.getChildren().addAll(stateView, history);
        
        HBox.setHgrow(stateView, Priority.ALWAYS);
        
        root.setBottom(bottom);
        
        /** COBY
         * Bind your button's disableProperty() to be true iff a human player is active 
         * (check this using ReadOnlyGameState.requestedGuessProperty() -- 
         * if its value is non-null, there's a human playing);
         */
        
        // bind elements to model, controller
        //--------------------------------------------------------------------------------------------------------------
        boardView.boardProperty().bind(game.boardProperty());
        boardView.setRevealed(game.getChosenCards());
        boardView.keycardVisibleProperty().bind(showOverlay.selectedProperty());
        
        gameEventView.stepProperty().bind(game.lastStepProperty());
        turnView.turnProperty().bind(game.turnProperty());
        
        scoreView.clueProperty().bind(game.lastClueProperty());
        scoreView.redScoreProperty().bind(game.redScoreProperty());
        scoreView.blueScoreProperty().bind(game.blueScoreProperty());
        
        
        
        stateView.actionProperty().bind(game.lastActionProperty());
        
        game.redScoreProperty()     .addListener(advanceFreeze);
        game.redObjectiveProperty() .addListener(advanceFreeze);
        game.blueScoreProperty()    .addListener(advanceFreeze);
        game.blueObjectiveProperty().addListener(advanceFreeze);
        game.lastEventProperty()    .addListener(advanceFreeze);
        
        
        
        advance.setOnAction(event->controller.advanceTurn());
        undo   .setOnAction(event->controller.undoTurn());
        redo   .setOnAction(event->controller.redoTurn());
      
      if (game.requestedGuessProperty() != null){
    	  skipTurn.setDisable(true);
      }
     
        
        redo.setDisable(game.getUndone().isEmpty());
        undo.setDisable(game.getHistory().isEmpty());
        
        
        history.disableProperty().bind(game.actionInProgressProperty());
        game.getUndone() .addListener((Observable o)->redo.setDisable(game.getUndone().isEmpty()));
        game.getHistory().addListener((Observable o)->undo.setDisable(game.getHistory().isEmpty()));
    }
    
    private void onAdvanceChanged(Observable ignore) {
    	advance.setDisable(
			game.getRedScore()  >= game.redObjectiveProperty().get() ||
			game.getBlueScore() >= game.blueObjectiveProperty().get() ||
			game.getLastEvent().isTerminal()
		);
    }
    
}