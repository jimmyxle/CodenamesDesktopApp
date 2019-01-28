//@alexabrams

package ca.concordia.encs.comp354.controller;

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
	
	
	public String guessWord(Board board) {
		CardValue color = board.getValue(linearRow, linearCol);
		while (color != team) {
			if (color != team) {
				if (linearCol <= 4) {
					linearCol++;
				}
				else {
					linearCol = 0;
					linearRow++;
				}
			}
			color = board.getValue(linearRow, linearCol);
		} 
		
		String guess = board.getWord(linearRow, linearCol);
		return guess;
	}
	
}
