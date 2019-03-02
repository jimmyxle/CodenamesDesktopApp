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
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import static java.nio.file.StandardOpenOption.*;

public class Codenames extends Application {

    private GameState      game;
    private GameController controller;
    
	@Override
	public void start(Stage stage) throws IOException {
		// configure game
	    //--------------------------------------------------------------------------------------------------------------
		List<CodenameWord> codenameWords = Card.createRandomCodenameList(Paths.get("res/words.txt"));
		//List<CodenameWord> codenameWords = Card.createRandomCodenameList(Paths.get("res/25wordswithcommonassociatedwords.txt"));
		List<Keycard> keycards = Keycard.createRandomKeycards(Keycard.NUMBER_OF_KEYCARDS);

		// create game state & controller
		game = new GameState(new Board(codenameWords, keycards.get(new Random().nextInt(keycards.size()))), System.out);
		controller = 
		        new GameController.Builder()
		        .setModel(game)
		        .setInitialTurn(Team.RED)
		        .setRedSpyMaster (new SpyMaster(Team.RED,  new SequentialSpyMasterStrategy()))
		        .setRedOperative (new Operative(Team.RED,  new SequentialOperativeStrategy()))
		        .setBlueSpyMaster(new SpyMaster(Team.BLUE, new SmartSpyMasterStrategy()))
		        .setBlueOperative(new Operative(Team.BLUE, new RandomOperativeStrategy()))
		        .create();
		

        // create interface
        //--------------------------------------------------------------------------------------------------------------
        final StackPane root  = new StackPane();
        final Scene     scene = new Scene(root, 512, 512, true);
        
        scene.getStylesheets().add(stylesheet("res/style.css"));
		root.getChildren().add(new GameView(game, controller));

		// configure the window & display our interface
		//--------------------------------------------------------------------------------------------------------------
		stage.setTitle("Codenames");
		stage.setScene(scene);
		stage.show();
	}
	
	@Override
	public void stop() throws IOException {
	    if (game==null) {
	        return;
	    }
	    
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
