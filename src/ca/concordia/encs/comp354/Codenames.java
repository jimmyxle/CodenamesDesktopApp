package ca.concordia.encs.comp354;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import ca.concordia.encs.comp354.controller.*;
import ca.concordia.encs.comp354.model.*;
import ca.concordia.encs.comp354.view.GameView;
import ca.concordia.encs.comp354.view.MenuView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import static java.nio.file.StandardOpenOption.*;

public class Codenames extends Application {

	private GameState game;

    public static void main(String[] args) {
        Application.launch(args);
    }
	
	@Override
	public void start(Stage stage) throws IOException {
		// configure game
		//==============================================================================================================
		// create controller
		GameController.Builder builder =
				new GameController.Builder()
				.setInitialTurn(Team.RED)
				.setRedSpyMaster (new SpyMaster(Team.RED,  new WeightedSpyMasterStrategy()))
				.setRedOperative (new Operative(Team.RED,  new WeightedOperativeStrategy()))
				.setBlueSpyMaster(new SpyMaster(Team.BLUE, new CountSpyMasterStrategy()))
				.setBlueOperative(new Operative(Team.BLUE, new IterativeOperativeStrategy()));

		// create game state
		game = new GameState(Codenames::generateBoard, System.out);
		

		// create interface
		//==============================================================================================================
        final String css = stylesheet("res/style.css");
        
        // game view
        //--------------------------------------------------------------------------------------------------------------
        // just configure the primary stage here; the actual GameView is initialized by the MenuView when the user
        // makes a selection
		final StackPane root  = new StackPane();
		final Scene     scene = new Scene(root, 768, 768, true);

		scene.getStylesheets().add(css);

		stage.setTitle("Codenames");
		stage.setScene(scene); 
	    
		// setup menu
		//--------------------------------------------------------------------------------------------------------------
		final Stage menuStage = new Stage();

        final MenuView menu = new MenuView(builder, ()-> {
            root.getChildren().add(new GameView(game, builder.setModel(game).create()));
            stage.show();
            menuStage.close();
        });
        
        final Scene menuScene = new Scene(menu, 320, 256);
        menuScene.getStylesheets().add(css);

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

    private static Board generateBoard() {
        try {
            List<CodenameWord> codenameWords = Card.createRandomCodenameList(Paths.get("res/words.txt"));
            List<Keycard> keycards = Keycard.createRandomKeycards(Keycard.NUMBER_OF_KEYCARDS);
            return new Board(codenameWords, keycards.get(0));
        } catch (IOException e) {
            throw new Error(e);
        }
    }

}
