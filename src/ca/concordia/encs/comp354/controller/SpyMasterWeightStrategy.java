package ca.concordia.encs.comp354.controller;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.CodenameWord.AssociatedWord;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.ReadOnlyGameState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * With this strategy, a spymaster picks a clue for a random card on the board.
 * @author Alexandre Kang
 *
 */
public class SpyMasterWeightStrategy extends AbstractPlayerStrategy implements SpyMaster.Strategy {

    @Override
    //this method is the clue given by the spymaster
    public Clue giveClue(SpyMaster owner, ReadOnlyGameState state) {
        List<Coordinates> guesses = beginTurn(owner, state);
        Map<String, Integer> wordWeight = new HashMap<>();
        Board board = state.boardProperty().get();
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

        Map.Entry<String, Integer> highest = null;
        for (Map.Entry<String,Integer> e : wordWeight.entrySet()) {
            if (highest == null || highest.getValue()<e.getValue()) {
                highest = e;
            }
        }

        return highest == null ? null : new Clue(highest.getKey(),highest.getValue());

    }

    @Override
    protected boolean isValidGuess(Player owner, Board board, int x, int y) {
        // valid iff card belongs to our team
        return board.getCard(x, y).getValue() == owner.getTeam().getValue();
    }
}