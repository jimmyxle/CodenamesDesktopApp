package ca.concordia.encs.comp354.controller;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Card;
import ca.concordia.encs.comp354.model.CardValue;

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
		String guess = board.getCard(teammate.getRow(), teammate.getCol()).getCodename();
		SpyMaster.nextCard();
		return guess;
	}
	
	public int getTeamMember() {
		return teamMember;
	}
	
	
	public static void main(String[] args) throws IOException {
	    List<Card> cardList = Card.generate25Cards(Paths.get("res/words.txt"));
	    Board gameBoard = new Board(cardList);
	    //====================
	    //--------TEST--------
	    //====================
	    //print out the card list, then return clue and guess for first 2 red cards
	    System.out.println("Printing out the game board: ");
	    System.out.println(gameBoard.toString());
	    
	    SpyMaster spy = new SpyMaster(CardValue.RED);
	    Operative op = new Operative(CardValue.RED, spy);
	    
	    String clue = spy.giveClue(gameBoard);
	    System.out.println("First clue: " + clue);
	    String guess = op.guessWord(gameBoard);
	    System.out.println("First guess: " + guess);
	    
	    clue = spy.giveClue(gameBoard);
	    System.out.println("Second clue: " + clue);
	    guess = op.guessWord(gameBoard);
	    System.out.println("Second guess: " + guess);   
	    
	}



}

