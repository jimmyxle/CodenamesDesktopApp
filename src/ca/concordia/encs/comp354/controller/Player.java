//@alexabrams

package ca.concordia.encs.comp354.controller;

/**
 * Base class for the players. Keeps track of where the spymaster/operative is picking linearly.
 * @author Alex Abrams
 */
public class Player {

	private CardValue team;
	
	//Base constructor, initializes the team of the player
	public Player(CardValue color) {
		team = color;
	}

}
