package ca.concordia.encs.comp354.controller;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.CodenameWord.AssociatedWord;
import ca.concordia.encs.comp354.model.CodenameWord;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.ReadOnlyGameState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * With this strategy, a spymaster gives a clue containing the most frequent {@link AssociatedWord} having a weight
 * closest (but less than) 100 and the number of times the {@link AssociatedWord} are associated with
 * {@link CodenameWord} on the {@link Board}.
 *
 * @author Alexandre Kang
 */
public class SpyMasterWeightStrategy extends AbstractPlayerStrategy implements SpyMaster.Strategy {

    @Override
    //this method is the clue given by the spymaster
    public Clue giveClue(SpyMaster owner, ReadOnlyGameState state) {
        // variable guesses has the coordinate of all cards of team using
        // the SpyMasterCountStrategy
        List<Coordinates> guesses = beginTurn(owner, state);
        Map<String, Integer> wordWeight = new HashMap<>();
        Board board = state.boardProperty().get();
        // take all associatedWord of all clueWord (word on the board) having a weight
        // equals to 100 of team using the SpyMasterCountStrategy.
        // Increment the count if associatedword repeats.
        for (Coordinates coord : guesses) {
            for (AssociatedWord word : board.getCard(coord).getAssociatedWords()) {
                if (word.getWeight() == 100) {
                    if (wordWeight.containsKey(word)) {
                        Integer freq = wordWeight.get(word);
                        ++freq;
                        wordWeight.put(word.getWord(), freq);
                    } else {
                        wordWeight.put(word.getWord(), 1);
                    }
                }
            }
        }

        //sort the map from the highest count to the lowest count
        Map.Entry<String, Integer> highest = null;
        for (Map.Entry<String,Integer> e : wordWeight.entrySet()) {
            if (highest == null || highest.getValue()<e.getValue()) {
                highest = e;
            }
        }

        //return the clue (associatedWord) having the highest count with its count
        return highest == null ? null : new Clue(highest.getKey(),highest.getValue());

    }

    @Override
    protected boolean isValidGuess(Player owner, Board board, int x, int y) {
        // valid iff card belongs to our team
        return board.getCard(x, y).getValue() == owner.getTeam().getValue();
    }
}