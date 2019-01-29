package ca.concordia.encs.comp354.controller;

/**
 * SpyMaster class extends Player. Adds functionality to return a clue.
 * @author Alex Abrams
 */

import ca.concordia.encs.comp354.model.ReadOnlyGameState;
import ca.concordia.encs.comp354.model.Team;

public class SpyMaster extends Player {
    
    public interface Strategy {
        String giveClue(SpyMaster owner, ReadOnlyGameState state);
    }

    private final Strategy strategy;
    
	//Constructor uses super
	public SpyMaster(Team team, Strategy strategy) {
		super(team);
		this.strategy = strategy;
	}
	
	public String giveClue(ReadOnlyGameState state) {
	    return strategy.giveClue(this, state);
	}
	

	/*
	public String giveClue(Board board) {
	    Card card = board.getCard(linearRow, linearCol);
		while (getTeam().getValue() != card.getValue()) {
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
	*/
	
}
