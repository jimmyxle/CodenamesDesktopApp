package ca.concordia.encs.comp354;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

import ca.concordia.encs.comp354.controller.*;
import ca.concordia.encs.comp354.model.*;
import ca.concordia.encs.comp354.view.GameView;
import ca.concordia.encs.comp354.view.MenuView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import static java.nio.file.StandardOpenOption.*;

public class Codenames extends Application {

	private GameState game;

	@Override
	public void start(Stage stage) throws IOException {
		// configure game
		//--------------------------------------------------------------------------------------------------------------
		// create controller
		GameController.Builder builder =
				new GameController.Builder()
				.setInitialTurn(Team.RED)
				.setRedSpyMaster (new SpyMaster(Team.RED,  new SpyMasterWeightStrategy()))
				.setRedOperative (new Operative(Team.RED,  new WeightedOperativeStrategy()))
				.setBlueSpyMaster(new SpyMaster(Team.BLUE, new SpyMasterCountStrategy()))
				.setBlueOperative(new Operative(Team.BLUE, new IterativeOperativeStrategy()));

		// create game state
		List<CodenameWord> codenameWords = Card.createRandomCodenameList(Paths.get("res/words.txt"));
		List<Keycard> keycards = Keycard.createRandomKeycards(Keycard.NUMBER_OF_KEYCARDS);

		game = new GameState(new Board(codenameWords, keycards.get(new Random().nextInt(keycards.size()))), System.out);

		// create interface
		//--------------------------------------------------------------------------------------------------------------
		final StackPane root  = new StackPane();
		final Scene     scene = new Scene(root, 512, 512, true);

		scene.getStylesheets().add(stylesheet("res/style.css"));

		stage.setTitle("Codenames");
		stage.setScene(scene); 

		// create menu
		//--------------------------------------------------------------------------------------------------------------
		final Stage    menuStage = new Stage();
		final GridPane menu      = new GridPane();
		final Scene		 menuScene = new Scene(menu, 300, 230);

    menu.getChildren().add(new MenuView(builder, ()-> {
        root.getChildren().add(new GameView(game, builder.setModel(game).create()));
        stage.show();
        menuStage.close();
      }
    ));

    menuStage.setTitle("Codenames");
    menuStage.setScene(menuScene);

    menuStage.show();
  }

	@Override
	public void stop() throws IOException {
		if (game==null) {
			return;
		}

		// dump game history when the application terminates
		//--------------------------------------------------------------------------------------------------------------
		try (BufferedWriter out = Files.newBufferedWriter(Paths.get("log.txt"), CREATE, TRUNCATE_EXISTING)) {
			for (GameStep k : game.getHistory()) {
				out.append(k.getText()+"\n");
			}
		}
	}

	private static String stylesheet(String path) throws MalformedURLException {
		return Paths.get(path).toAbsolutePath().toUri().toURL().toExternalForm();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
