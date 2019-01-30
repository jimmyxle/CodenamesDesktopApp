package ca.concordia.encs.comp354.controller;

/**
 * SpyMaster class extends Player. Adds functionality to return a clue.
 * @author Alex Abrams
 */

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.CardValue;

public class SpyMaster extends Player {
	static int linearCol = 0;
	static int linearRow = 0;
	
	//Constructor uses super
	public SpyMaster(CardValue team) {
		super(team);
	}
	

    /**
     * Returns a clue based on the first unused codename that matches the objects team color
     * @param current Board object
     * @return the codename for the first untouched coordinate
     */
	public String giveClue(Board board) {
		CardValue color = board.getValue(linearRow, linearCol);
		while (team != color) {
			nextCard();
			color = board.getValue(linearRow, linearCol);
		} 
		String codename = board.getWord(linearRow, linearCol);
		String clue = board.getAssociatedWord(linearRow, linearCol, 0);
		return clue;		
	}
	
	public int getRow() {
		return linearRow;
	}
	
	public int getCol() {
		return linearCol;
	}
	
	public static void nextCard() {
		if (linearCol < 4 && linearRow < 4) {
			linearCol++;
		}
		else if(linearCol == 4) {
			linearCol = 0;
			linearRow++;
		}
	}
	
}
