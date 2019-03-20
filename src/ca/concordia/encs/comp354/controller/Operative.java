package ca.concordia.encs.comp354.controller;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import ca.concordia.encs.comp354.Promise;
import ca.concordia.encs.comp354.model.*;

/**
 * Operatives are Players that produce guesses from {@link SpyMaster}s' clues.
 * @author Alex Abrams
 * @author Mykyta Leonidov
 */

public class Operative extends Player {
	
    private interface Strategy {
        
        default boolean isUser() {
            return false;
        }
        
        default UserStrategy asUser() {
            throw new UnsupportedOperationException();
        }
        
        default boolean isAI() {
            return false;
        }
        
        default AIStrategy asAI() {
            throw new UnsupportedOperationException();
        }    
    }
    
    /**
     * Interface for strategies that may delegate decisions to the view.
     * @author Mykyta Leonidov
     *
     */
    public interface UserStrategy extends Strategy {
        /**
         * Requests a guess from this strategy, given the current game state.
         * @param owner  the owning player object
         * @param state  a read-only view of the current game state
         * @param clue   the last clue produced by this player's spymaster
         * @return a Promise containing the guess
         */
        Promise<Coordinates> guessCard(Operative owner, HumanGameState state, Clue clue);
        
        @Override
        default boolean isUser() {
            return true;
        }
        
        default UserStrategy asUser() {
            return this;
        }
    }
    
    /**
     * Interface for strategies that must make decisions based on the model alone.
     * @author Mykyta Leonidov
     *
     */
	public interface AIStrategy extends Strategy {
	    /**
	     * Requests a guess from this strategy, given the current game state.
	     * @param owner  the owning player object
	     * @param state  a read-only view of the current game state
	     * @param clue   the last clue produced by this player's spymaster
	     * @return a Promise containing the guess
	     */
	    Promise<Coordinates> guessCard(Operative owner, ReadOnlyGameState state, Clue clue);
        
	    @Override
        default boolean isAI() {
            return true;
        }
        
        default AIStrategy asAI() {
            return this;
        }
	}
	
	private final Strategy strategy;
	
	public Operative(Team team, AIStrategy strategy) {
	    this(team, (Strategy)strategy);
	}
    
    public Operative(Team team, UserStrategy strategy) {
        this(team, (Strategy)strategy);
    }
	
	//constructor
	private Operative(Team team, Strategy strategy) {
		super(team);
		this.strategy = Objects.requireNonNull(strategy, "strategy");
	}
	
	Promise<Coordinates> guessCard(GameState state, Clue clue) {
		return 
	        (strategy.isUser()
		        ? strategy.asUser().guessCard(this, state, clue)
                : strategy.asAI()  .guessCard(this, state, clue)
            ).then(coords->{
                if (coords==null) {
                    throw new IllegalStateException("cannot produce another guess");
                }
            });
	}
	
	
	
	//====================
    //--------TEST--------
    //====================
	public static void main(String[] args) throws IOException {
		List<CodenameWord> codenameWords = Card.createRandomCodenameList(Paths.get("res/25wordswithcommonassociatedwords.txt"));
		Keycard keycard = Keycard.createRandomKeycard();
	    GameState state = new GameState(new Board(codenameWords, keycard));
	    Board board = state.boardProperty().get();
	 
	    //print out the card list, then return clue and guess for first 2 red cards
	    System.out.println("Printing out the game board: ");
	    System.out.println(state.boardProperty().get().toString());
	    
	    SpyMaster spy = new SpyMaster(Team.RED, new RandomSpyMasterStrategy());
	    Operative op = new Operative(Team.RED, new IterativeOperativeStrategy());
	    
	    Clue clue = spy.giveClue(state);
	    System.out.println("First clue: " + clue);
	    Promise<Coordinates> guess = op.guessCard(state, clue);
	    System.out.println("First guess: " + board.getCard(guess.get().getX(), guess.get().getY()).getCodename());
        
        clue = spy.giveClue(state);
        System.out.println("Second clue: " + clue);
        guess = op.guessCard(state, clue);
        System.out.println("Second guess: " + board.getCard(guess.get().getX(), guess.get().getY()).getCodename());
	    
	}



}


