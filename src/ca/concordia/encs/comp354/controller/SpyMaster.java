//alexabrams

package ca.concordia.encs.comp354.controller;

/**
 * SpyMaster class extends Player. Adds functionality to return a clue.
 * @author Alex Abrams
 */

import java.util.Objects;

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
			
			
			
			
			
			
			
			
			
			
}
