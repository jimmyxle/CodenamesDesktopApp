package ca.concordia.encs.comp354.controller;

import java.util.List;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.ReadOnlyGameState;

/**
 * With this strategy, an operative picks a strategy by iterative through the list of his team's cards
 * @author Jimmy Le
 * 
 *
 */
public class IterativeOperativeStrategy extends AbstractPlayerStrategy implements Operative.Strategy {

    @Override
    public Coordinates guessCard(Operative owner, ReadOnlyGameState state, Clue clue) {
    	//use the given Clue
        List<Coordinates> guesses = beginTurn(owner, state);
        return guesses.isEmpty()? null : guesses.remove(0);
    }

    @Override
    protected boolean isValidGuess(Player owner, Board board, int x, int y) {
        return true;
    }

}
