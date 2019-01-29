package ca.concordia.encs.comp354.controller;

import ca.concordia.encs.comp354.model.Team;

/**
 * Base class for the players.
 * @author Alex Abrams
 */
public class Player {

	private final Team team;
	
	//Base constructor, initializes the team of the player
	public Player(Team team) {
		this.team = team;
	}
	
	protected Team getTeam() {
	    return team;
	}

}
