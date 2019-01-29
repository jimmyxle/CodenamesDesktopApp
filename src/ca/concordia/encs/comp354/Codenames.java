package ca.concordia.encs.comp354;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import ca.concordia.encs.comp354.controller.GameEvent;
import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Card;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Team;
import ca.concordia.encs.comp354.view.GameView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Codenames extends Application {

	@Override
	public void start(Stage stage) throws IOException {
		// set up a simple interface
		//--------------------------------------------------------------------------------------------------------------
		final StackPane root  = new StackPane();
		final Scene     scene = new Scene(root, 512, 512);
		
		scene.getStylesheets().add("file:///"+Paths.get("res/style.css").toAbsolutePath().toString().replace('\\', '/'));
	
		// replace with implementations
		List<Card> config = Card.generate25Cards(Paths.get("res/words.txt"));
		GameState game = new GameState(new Board(config), Team.RED);
		GameView.Controller testController = ()->GameEvent.NONE;
		
		root.getChildren().add(new GameView(game, testController));

		// configure the window & display our interface
		//--------------------------------------------------------------------------------------------------------------
		stage.setTitle("Codenames");
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}

}
