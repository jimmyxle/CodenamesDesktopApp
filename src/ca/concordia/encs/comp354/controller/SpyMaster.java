package ca.concordia.encs.comp354.controller;

/**
 * SpyMaster class extends Player. Adds functionality to return a clue.
 * @author Alex Abrams
 */

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Card;
import ca.concordia.encs.comp354.model.CardValue;
import ca.concordia.encs.comp354.model.CodenameWord.AssociatedWord;

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
	    Card card = board.getCard(linearRow, linearCol);
		while (team != card.getValue()) {
			nextCard();
			card = board.getCard(linearRow, linearCol);
		}
		AssociatedWord clue = card.getAssociatedWords().get(0);
		return clue.getAssociatedWord();		
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
