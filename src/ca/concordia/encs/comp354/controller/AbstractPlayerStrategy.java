package ca.concordia.encs.comp354.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.ReadOnlyGameState;

/**
 * Handy base class for {@link Player} subclass strategies.
 * @author Mykyta Leonidov
 *
 */
abstract class AbstractPlayerStrategy {
    
    private List<Coordinates> guesses;
    private Board             lastBoard;
    
    /**
     * Returns a list of valid guesses for the strategy's owning player, rebuilding the list if necessary. This method
     * uses the {@link #isValidGuess(Player, Board, int, int)} method as a predicate to determine which coordinates 
     * contain valid cards. The returned list will never contain already-revealed cards.
     * 
     * @param owner  the strategy's owning player
     * @param state  the current game state
     * @return a list of card coordinates that represent valid guesses for this strategy
     */
    protected List<Coordinates> beginTurn(Player owner, ReadOnlyGameState state) {
        Board board = state.boardProperty().get();
        Set<Coordinates> marked = state.getChosenCards();
        
        // board has changed; reset guesses
        if (board!=lastBoard) {
            rebuildGuesses(owner, board);
            lastBoard = board;
        }
        
        // clear invalid guesses (have to do this each turn, since other team may have guessed one of our cards)
        for (int i=guesses.size()-1; i>=0; i--) {
            if (marked.contains(guesses.get(i))) {
                guesses.remove(i);
            }
        }
        
        return guesses;
    }
    
    /**
     * Determines whether the card at the given coordinates is a valid option for this strategy. This is useful if, for
     * example, you want a {@link SpyMaster.Strategy} to filter out cards with values that do not correspond to that 
     * <tt>SpyMaster</tt>'s team. As operatives may not see the keycard, their strategies would typically implement this
     * method simply by returning <tt>true</tt>.
     * 
     * @param owner  the strategy's owning player
     * @param board  the current game board
     * @param x      the horizontal coordinate of the card being tested
     * @param y      the vertical coordinate of the card being tested
     * @return <tt>true</tt> iff the player may select this card
     */
    protected abstract boolean isValidGuess(Player owner, Board board, int x, int y);

    private void rebuildGuesses(Player owner, Board board) {
        guesses = new ArrayList<>();
        for (int y=0; y<board.getWidth(); y++) {
            for (int x=0; x<board.getLength(); x++) {
                if (isValidGuess(owner, board, x, y)) {
                    guesses.add(new Coordinates(x, y));
                }
            }
        }
    }
}
