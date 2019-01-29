package ca.concordia.encs.comp354.controller;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Card;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.ReadOnlyGameState;
import ca.concordia.encs.comp354.model.Team;

/**
 * Operatives are Players that produce guesses from {@link SpyMaster}s' clues.
 * @author Alex Abrams
 * @author Nikita Leonidov
 */

public class Operative extends Player {
	
	public interface Strategy {
	    /**
	     * Requests a guess from this strategy, given the current game state.
	     * @param owner  the owning player object
	     * @param state  a read-only view of the current game state
	     * @param clue   the last clue produced by this player's spymaster
	     * @return a guess, or <tt>null</tt> if no more guesses are possible
	     */
	    Coordinates guessCard(Operative owner, ReadOnlyGameState state, String clue);
	}
	
	private final Strategy strategy;
	
	//constructor
	public Operative(Team team, Strategy strategy) {
		super(team);
		this.strategy = Objects.requireNonNull(strategy, "strategy");
	}
	
	public Coordinates guessCard(ReadOnlyGameState state, String clue) {
		Coordinates ret = strategy.guessCard(this, state, clue);
		if (ret==null) {
		    throw new IllegalStateException("cannot produce another guess");
		}
		return ret;
	}
	
	
	public static void main(String[] args) throws IOException {
	    List<Card> cardList = Card.generate25Cards(Paths.get("res/words.txt"));
	    GameState state = new GameState(new Board(cardList), Team.RED);
	    Board board = state.boardProperty().get();
	    //====================
	    //--------TEST--------
	    //====================
	    //print out the card list, then return clue and guess for first 2 red cards
	    System.out.println("Printing out the game board: ");
	    System.out.println(state.boardProperty().get().toString());
	    
	    SpyMaster spy = new SpyMaster(Team.RED, new SequentialSpyMasterStrategy());
	    Operative op = new Operative(Team.RED, new SequentialOperativeStrategy());
	    
	    String clue = spy.giveClue(state);
	    System.out.println("First clue: " + clue);
	    Coordinates guess = op.guessCard(state, clue);
	    System.out.println("First guess: " + board.getCard(guess.getX(), guess.getY()).getCodename());

        clue = spy.giveClue(state);
        System.out.println("Second clue: " + clue);
        guess = op.guessCard(state, clue);
        System.out.println("Second guess: " + board.getCard(guess.getX(), guess.getY()).getCodename());
	    
	}



}

