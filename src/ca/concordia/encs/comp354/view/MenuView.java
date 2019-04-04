package ca.concordia.encs.comp354.view;

import java.util.Objects;

import ca.concordia.encs.comp354.controller.GameController;
import ca.concordia.encs.comp354.controller.HumanOperativeStrategy;
import ca.concordia.encs.comp354.controller.Operative;
import ca.concordia.encs.comp354.model.Team;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class MenuView extends GridPane	{
	
	public MenuView(GameController.Builder builder, Runnable run)	{
		
		Objects.requireNonNull(builder, "controller builder");
        
		// create menu view
		//--------------------------------------------------------------------------------------------------------------
		Button button1, button2, button3;
		GridPane layout1 = new GridPane();
		getChildren().add(layout1);

		// create buttons
		Label label1 = new Label("Choose what you want to do:");
		GridPane.setConstraints(label1, 0, 1);

		button1 = new Button("Play as Blue");
		button1.setOnAction(event -> {
			builder.setBlueOperative(new Operative(Team.BLUE, new HumanOperativeStrategy()));
			run.run();
		});
		GridPane.setConstraints(button1, 0, 4);
		
		button2 = new Button("Play as Red");
		button2.setOnAction(event ->	{
			
			builder.setRedOperative(new Operative(Team.RED, new HumanOperativeStrategy()));
			run.run();
		});
		GridPane.setConstraints(button2, 0, 7);

		button3 = new Button("Watch");
		button3.setOnAction(event ->	{
			run.run();
		});
		GridPane.setConstraints(button3, 0, 10);

		// create layout of menu
		layout1.setPadding(new Insets(10, 10, 10, 10));
		layout1.setVgap(8);
		layout1.setHgap(10);
		layout1.getChildren().addAll(label1, button1, button2, button3);
	}
}
