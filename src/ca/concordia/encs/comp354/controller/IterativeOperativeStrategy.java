package ca.concordia.encs.comp354.controller;

import java.util.List;

import ca.concordia.encs.comp354.Promise;
import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.ReadOnlyGameState;
import ca.concordia.encs.comp354.model.CodenameWord.AssociatedWord;

/**
 * With this strategy, an operative picks a strategy by iterative through the list of his team's cards
 * @author Jimmy Le

 */
public class IterativeOperativeStrategy extends AbstractPlayerStrategy implements Operative.AIStrategy {

   //Iterate through the list of associated words for each codename. Return coordinates of the found word.
	@Override
    public Promise<Coordinates> guessCard(Operative owner, ReadOnlyGameState state, Clue clue) {
        List<Coordinates> guesses = beginTurn(owner, state);
        Board board = state.boardProperty().get();
        /*
         * for each card on the board
         * 		get the coord
         * 		for each associated word:
         * 			check if equals the clue
         * 				if true: return coords
         */
        for(Coordinates coords: guesses) {
            List<AssociatedWord> test = board.getCard(coords).getAssociatedWords();
            for(AssociatedWord assoc : test) {
                String word = assoc.getWord();
                if (word.equalsIgnoreCase(clue.getWord())) {
                	return Promise.finished(coords);
                }
            }
        }
        
        return Promise.finished(guesses.isEmpty()? null : guesses.remove(0));
    }

    @Override
    protected boolean isValidGuess(Player owner, Board board, int x, int y) {
        return true;
    }

}
