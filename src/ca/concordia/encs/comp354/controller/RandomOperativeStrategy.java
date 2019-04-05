package ca.concordia.encs.comp354.controller;

import java.util.List;
import java.util.Random;

import ca.concordia.encs.comp354.Promise;
import ca.concordia.encs.comp354.controller.action.GuessCardAction;
import ca.concordia.encs.comp354.controller.action.OperativeAction;
import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.GameState;

/**
 * With this strategy, an operative picks his next card at random.
 * @author Mykyta Leonidov
 *
 */
public class RandomOperativeStrategy extends AbstractPlayerStrategy implements Operative.Strategy {
    
    private final Random random = new Random();
    
    @Override
    public Promise<OperativeAction> guessCard(Operative owner, GameState state, Clue clue) {
        List<Coordinates> guesses = beginTurn(owner, state);
        return guesses.isEmpty()
            ? null 
            : Promise.of(new GuessCardAction(owner, guesses.remove(random.nextInt(guesses.size()))));
    }

    @Override
    protected boolean isValidGuess(Player owner, Board board, int x, int y) {
        return true;
    }
}
