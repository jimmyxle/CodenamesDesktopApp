//@alexabrams

package ca.concordia.encs.comp354.controller;

/**
 * Operative class extends Player. Keeps track of number of Operatives, takes a spymaster as teammate and can return a guess
 * @author Alex Abrams
 */

public class Operative extends Player {
	private static int numOps = 0; //keep track of number of operatives
	private int teamMember; //var for player number
	private SpyMaster teammate;
	
	
	//constructor
	public Operative(CardValue team, SpyMaster teammate) {
		super(team);
		this.teammate = teammate;
		numOps++;
		teamMember = numOps;			
	}
	
	 /**
     * Returns a guess based on the next untouched codename on the board
     * @param current Board object
     * @return the word associated with the last codename the Spymaster looked at. 
     */
	public String guessWord(Board board) {
		String guess = board.getWord(teammate.getRow(), teammate.getCol());
		return guess;
	}
	
}

