package ca.concordia.encs.comp354.controller;

import java.util.List;
import java.util.Random;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Card;
import ca.concordia.encs.comp354.model.CodenameWord.AssociatedWord;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.ReadOnlyGameState;

/**
 * With this strategy, a spymaster picks a clue for a sequentially chosen card, starting from the top-left corner
 * @author Alex Abrams
 * @author Nikita Leonidov
 *
 */
public class SequentialSpyMasterStrategy extends AbstractPlayerStrategy implements SpyMaster.Strategy {
    
    private final Random random = new Random();

    @Override
    public Clue giveClue(SpyMaster owner, ReadOnlyGameState state) {
        List<Coordinates> guesses = beginTurn(owner, state);
        //System.out.println("Sequential spymaster strategy coordinates of cards to be picked "+ guesses.toString());
        return guesses.isEmpty()? null : getAssociatedWord(state.boardProperty().get(), guesses.get(0));
    }

    private Clue getAssociatedWord(Board board, Coordinates coords) {
        Card card = board.getCard(coords);
        List<AssociatedWord> words = card.getAssociatedWords();
        //System.out.println("Associated words of " + words.get(random.nextInt(words.size())).getWord());
        return new Clue(words.get(random.nextInt(words.size())).getWord(), 1);
    }

    @Override
    protected boolean isValidGuess(Player owner, Board board, int x, int y) {
        // valid iff card belongs to our team
        return board.getCard(x, y).getValue() == owner.getTeam().getValue();
    }
}
