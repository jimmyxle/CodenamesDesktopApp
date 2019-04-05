package ca.concordia.encs.comp354.controller;

import java.util.List;

import ca.concordia.encs.comp354.Promise;
import ca.concordia.encs.comp354.controller.action.GuessCardAction;
import ca.concordia.encs.comp354.controller.action.OperativeAction;
import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.CodenameWord.AssociatedWord;

/**
 * With this strategy, an operative picks a strategy by iterative through the list of his team's cards
 * @author Jimmy Le

 */
public class IterativeOperativeStrategy extends AbstractPlayerStrategy implements Operative.Strategy {

   //Iterate through the list of associated words for each codename. Return coordinates of the found word.
	@Override
    public Promise<OperativeAction> guessCard(Operative owner, GameState state, Clue clue) {
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
                	return Promise.of(new GuessCardAction(owner, coords));
                }
            }
        }
        
        return guesses.isEmpty()? null : Promise.of(new GuessCardAction(owner, guesses.remove(0)));
    }

    @Override
    protected boolean isValidGuess(Player owner, Board board, int x, int y) {
        return true;
    }

}
