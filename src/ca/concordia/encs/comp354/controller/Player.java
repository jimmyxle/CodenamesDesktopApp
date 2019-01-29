package ca.concordia.encs.comp354.controller;

import ca.concordia.encs.comp354.model.CardValue;

/**
 * Base class for the players. Keeps track of where the spymaster/operative is picking linearly.
 * @author Alex Abrams
 */
public class Player {

	protected CardValue team;
	
	//Base constructor, initializes the team of the player
	public Player(CardValue color) {
		team = color;
	}

}
