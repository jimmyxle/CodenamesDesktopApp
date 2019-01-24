package ca.concordia.encs.comp354;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Codenames extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		// set up a simple interface
		//--------------------------------------------------------------------------------------------------------------
		final StackPane root  = new StackPane();
		final Scene     scene = new Scene(root, 1024, 768);
		
		root.getChildren().add(new Label("Hey team"));

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
