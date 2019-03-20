package ca.concordia.encs.comp354.controller;

import java.util.List;

import ca.concordia.encs.comp354.Promise;
import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.ReadOnlyGameState;
import ca.concordia.encs.comp354.model.CodenameWord.AssociatedWord;

/**
 * With this strategy, an operative picks a strategy by iterating through the list of his team's cards
 * 
 * @author Elie Khoury
 *
 */
public class WeightedOperativeStrategy extends AbstractPlayerStrategy implements Operative.Strategy {

    //Iterate through the list of associated words for each codename. Return coordinates of the found word.
    @Override
    public Promise<Coordinates> guessCard(Operative owner, ReadOnlyGameState state, Clue clue) {
        List<Coordinates> guesses = beginTurn(owner, state);
        Board board = state.boardProperty().get();

        // Fetches the given SpyMaster hint, and stores it in a temporary string value.
        String spymasterHint = clue.getWord();

        // Creates a Coordinates object that will be returned once updated completely.
        Coordinates maxCoordinates = null;

        // Initializes a variable called max_weight that helps sort through the weights to find the highest one.
        int maxWeight = 0;
        // Goes through the 25 given Codenames.
        for (Coordinates coords : guesses) {
            // Fetches the list of AssociatedWord of a single Codename card.
            List<AssociatedWord> test = board.getCard(coords).getAssociatedWords();

            // Iterates through the list above to see if the SpyMaster's hint matches one of the Codename's AssociatedWords.
            for (AssociatedWord assoc : test) {
                String word = assoc.getWord();
                if (spymasterHint.equalsIgnoreCase(word)) {
                    // If the weight is equal to 0, set it to the weight of the first AssociatedWord it finds.
                    if (maxWeight <= 0) {
                        maxWeight = assoc.getWeight();
                        maxCoordinates = coords;
                    } else if (maxWeight < assoc.getWeight()) {
                        // If the current weight is less than the next AssociatedWord's weight, change it to the new weight.
                        maxWeight = assoc.getWeight();
                        maxCoordinates = coords;
                    }
                }
            }
        }
        
        // Returns the Coordinate object.
        if (maxCoordinates != null) {
        	return Promise.finished(maxCoordinates);
        } else {
            return Promise.finished(guesses.isEmpty()? null : guesses.remove(0));
        }

    }

    @Override
    protected boolean isValidGuess(Player owner, Board board, int x, int y) {
        return true;
    }

}