package ca.concordia.encs.comp354.view;

import java.util.Objects;

import ca.concordia.encs.comp354.controller.GameController;
import ca.concordia.encs.comp354.controller.HumanOperativeStrategy;
import ca.concordia.encs.comp354.controller.Operative;
import ca.concordia.encs.comp354.model.Team;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MenuView extends StackPane	{
	
	public MenuView(GameController.Builder builder, Runnable run)	{
		
		Objects.requireNonNull(builder, "controller builder");
        
		// create menu view
		//--------------------------------------------------------------------------------------------------------------
		VBox menu = new VBox();
		getChildren().add(menu);
		
		Button playRed, playBlue, watch;
		menu.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
		menu.getStyleClass().clear();
		menu.getStyleClass().add("menu-view");
		
        Label label1 = new Label("Codenames");
        label1.getStyleClass().add("title");

		// create buttons
        playRed = new Button("Play as Red");
        playRed.getStyleClass().add("red");
        playRed.setOnAction(event -> {
            builder.setRedOperative(new Operative(Team.RED, new HumanOperativeStrategy()));
            run.run();
        });

		playBlue = new Button("Play as Blue");
		playBlue.getStyleClass().add("blue");
		playBlue.setOnAction(event -> {
			builder.setBlueOperative(new Operative(Team.BLUE, new HumanOperativeStrategy()));
			run.run();
		});

		watch = new Button("Watch");
		watch.setOnAction(event -> {
			run.run();
		});

		// create layout of menu
		menu.getChildren().addAll(label1, playRed, playBlue, watch);
		
		// set children to fill the menu's width
		for (Node k : menu.getChildren()) {
		    ((Region)k).setPrefWidth(Double.MAX_VALUE);
		}
	}
}
