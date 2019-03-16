package ca.concordia.encs.comp354.controller;

import java.util.List;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Card;
import ca.concordia.encs.comp354.model.CodenameWord;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.ReadOnlyGameState;
import ca.concordia.encs.comp354.model.CodenameWord.AssociatedWord;

/**
 * With this strategy, an operative picks a strategy by iterative through the list of his team's cards
 * @author Elie Khoury
 *
 *
 */
public class WeightedOperativeStrategy extends AbstractPlayerStrategy implements Operative.Strategy {

    //Iterate through the list of associated words for each codename. Return coordinates of the found word.
    @Override
    public Coordinates guessCard(Operative owner, ReadOnlyGameState state, Clue clue) {
        List<Coordinates> guesses = beginTurn(owner, state);
        Board board = state.boardProperty().get();

        // Fetches the given SpyMaster hint, and stores it in a temporary string value.
        String spymasterHint = clue.getWord();

        // Creates a Coordinates object that will be returned once updated completely.
        Coordinates maxCoordinates = null;

        // Initializes a variable called max_weight that helps sort through the weights to find the highest one.
        int max_weight = 0;
        // Goes through the 25 given Codenames.
        for (Coordinates coords: guesses)
        {
            // Fetches the list of AssociatedWord of a single Codename card.
            List<AssociatedWord> test = board.getCard(coords).getAssociatedWords() ;

            // Iterates through the list above to see if the SpyMaster's hint matches one of the Codename's AssociatedWords.
            for (AssociatedWord assoc : test)
            {
                String word = assoc.getWord();
                if (spymasterHint.equalsIgnoreCase(word))
                {
                    // If the weight is equal to 0, set it to the weight of the first AssociatedWord it finds.
                    if (max_weight <= 0)
                    {
                        max_weight = assoc.getWeight();
                        maxCoordinates = coords;
                    }
                    // If the current weight is less than the next AssociatedWord's weight, change it to the new weight.
                    else if (max_weight < assoc.getWeight())
                    {
                        max_weight = assoc.getWeight();
                        maxCoordinates = coords;
                    }
                }
            }
        }

        // Returns the Coordinate object.
        if (maxCoordinates != null)
        	return maxCoordinates;
        else
            return guesses.isEmpty()? null : guesses.remove(0);

    }

    @Override
    protected boolean isValidGuess(Player owner, Board board, int x, int y) {
        return true;
    }

}