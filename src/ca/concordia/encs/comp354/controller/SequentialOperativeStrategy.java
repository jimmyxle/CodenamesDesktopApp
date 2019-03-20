package ca.concordia.encs.comp354.controller;

import java.util.List;

import ca.concordia.encs.comp354.Promise;
import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.ReadOnlyGameState;

/**
 * With this strategy, an operative picks his next card sequentially, starting from the top-left corner of the board.
 * @author Alex Abrams
 * @author Mykyta Leonidov
 *
 */
public class SequentialOperativeStrategy extends AbstractPlayerStrategy implements Operative.AIStrategy {

    @Override
    public Promise<Coordinates> guessCard(Operative owner, ReadOnlyGameState state, Clue clue) {
        List<Coordinates> guesses = beginTurn(owner, state);
        return Promise.finished(guesses.isEmpty()? null : guesses.remove(0));
    }

    @Override
    protected boolean isValidGuess(Player owner, Board board, int x, int y) {
        return true;
    }

}
