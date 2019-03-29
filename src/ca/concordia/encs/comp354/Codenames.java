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
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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
				.setRedSpyMaster (new SpyMaster(Team.RED,  new SpyMasterWeightStrategy()))
				.setRedOperative (new Operative(Team.RED,  new WeightedOperativeStrategy()))
				.setBlueSpyMaster(new SpyMaster(Team.BLUE, new SpyMasterCountStrategy()))
				.setBlueOperative(new Operative(Team.BLUE, new IterativeOperativeStrategy()))
				.create();

		// create interface
		//--------------------------------------------------------------------------------------------------------------
		final StackPane root  = new StackPane();
		final Scene     scene = new Scene(root, 512, 512, true);

		scene.getStylesheets().add(stylesheet("res/style.css"));
		root.getChildren().add(new GameView(game, controller));

		// create menu
		//--------------------------------------------------------------------------------------------------------------
		Button button1, button2, button3, button4;
		GridPane layout1 = new GridPane();
		GridPane layout2 = new GridPane();
		Scene scene1 = new Scene(layout1, 300, 220); 
		Scene scene2 = new Scene(layout2, 300, 220);
		
		// Button 1 & 3
		Label label1 = new Label("Scene 1 - Choose what you want to do");
		GridPane.setConstraints(label1, 0, 1);

		button1 = new Button("Button 1 - Play");
		button1.setOnAction(e -> stage.setScene(scene2));
		GridPane.setConstraints(button1, 0, 5);

		button3 = new Button("Button 3 - Watch");
		button3.setOnAction(e -> stage.setScene(scene));
		GridPane.setConstraints(button3, 0, 9);

		// Layout 1
		layout1.setPadding(new Insets(10, 10, 10, 10));
		layout1.setVgap(8);
		layout1.setHgap(10);
		layout1.getChildren().addAll(label1, button1, button3);

		// Button 2 & 4
		Label label2 = new Label("Scene 2 - Choose your color");
		GridPane.setConstraints(label2, 0, 1);

		button2 = new Button("Button 2 - Blue");
		button2.setOnAction(e -> stage.setScene(scene));
		GridPane.setConstraints(button2, 0, 5);

		button4 = new Button("Button 4 - Red");
		button4.setOnAction(e -> stage.setScene(scene));
		GridPane.setConstraints(button4, 0, 9);

		// Layout 2
		layout2.setPadding(new Insets(10, 10, 10, 10));
		layout2.setVgap(8);
		layout2.setHgap(10);
		layout2.getChildren().addAll(label2, button2, button4);

		// configure the window & display our interface
		//--------------------------------------------------------------------------------------------------------------
		stage.setTitle("Codenames");
		stage.setScene(scene1); // changed from scene to scene1 (Menu)
		stage.show();
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
