package ca.concordia.encs.comp354.controller;

import java.util.*;


import ca.concordia.encs.comp354.model.*;
import ca.concordia.encs.comp354.model.CodenameWord.AssociatedWord;

/**
 * With this strategy, a spymaster gives a clue
 * containing the most frequent associatedWord and its count.
 * @author Alexandre Kang
 *
 */
public class SpyMasterCountStrategy extends AbstractPlayerStrategy implements SpyMaster.Strategy {

    @Override
    //this method is the clue given by the spymaster
    public Clue giveClue(SpyMaster owner, ReadOnlyGameState state) {
        // variable guesses has the coordinate of all cards of team using
        // the SpyMasterCountStrategy
        List<Coordinates> guesses = beginTurn(owner, state);
        Map<String,Integer> wordFrequencies = new HashMap<>();
        Board board = state.boardProperty().get();
        // take all associatedWord of all clueWord (word on the board) of team using
        // the SpyMasterCountStrategy. Increment the count if associatedword repeats.
        for (Coordinates coord: guesses) {
            for(AssociatedWord word: board.getCard(coord).getAssociatedWords()){
                wordFrequencies.put(word.getWord(), wordFrequencies.getOrDefault(word.getWord(),0)+1);
            }
        }

        //sort the map from the highest count to the lowest count
        Map.Entry<String, Integer> highest = null;
        for (Map.Entry<String,Integer> e : wordFrequencies.entrySet()) {
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