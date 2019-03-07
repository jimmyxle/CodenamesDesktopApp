package ca.concordia.encs.comp354.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import ca.concordia.encs.comp354.model.*;
import ca.concordia.encs.comp354.model.CodenameWord.AssociatedWord;

/**
 * With this strategy, a spymaster picks a clue for a random card on the board.
 * @author Alexandre Kang
 *
 */
public class SmartSpyMasterStrategy extends AbstractPlayerStrategy implements SpyMaster.Strategy {

    private final Random random = new Random();

    @Override
    //this method is the clue given by the spymaster
    public Clue giveClue(SpyMaster owner, ReadOnlyGameState state) {
        List<Coordinates> guesses = beginTurn(owner, state);
        Map<String,Integer> wordFrequencies = new HashMap<>();
        Board board = state.boardProperty().get();
        for (Coordinates coord: guesses) {
            for(AssociatedWord word: board.getCard(coord).getAssociatedWords()){
/*                if(wordFrequencies.containsKey(word)){
                    Integer freq = wordFrequencies.get(word);
                    ++freq;
                    wordFrequencies.put(word,freq);
                }
                else {
                    wordFrequencies.put(word,1);
                }*/
                wordFrequencies.put(word.getWord(), wordFrequencies.getOrDefault(word.getWord(),0)+1);
//                wordFrequencies.compute(word, (k,v) -> v==null? 1 : v+1);
//                wordFrequencies.compute(word, new BiFunction<AssociatedWord, Integer, Integer>() {
//                    @Override
//                    public Integer apply(AssociatedWord k, Integer v) {
//                        return v==null?1:v+1;
//                    }
//                });
            }
        }

        Map.Entry<String, Integer> highest = null;
        for (Map.Entry<String,Integer> e : wordFrequencies.entrySet()) {
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