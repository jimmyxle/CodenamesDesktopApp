package ca.concordia.encs.comp354.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Card;
import ca.concordia.encs.comp354.model.CardValue;
import ca.concordia.encs.comp354.model.CodenameWord;
import ca.concordia.encs.comp354.model.CodenameWord.AssociatedWord;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Keycard;

import static org.junit.Assert.*;

public abstract class AbstractPlayerTest {
    
    private final Random random = new Random(75424590L);
    
    private static final int TEAM_CARD_COUNT = 8;
    
    void givesAllClues(SpyMaster player) {
        GameState state    = new GameState(generateBoard(player, false));
        Set<Clue> expected = generateClues(player, state.getBoard());
        Set<Clue> actual   = new HashSet<>();
        
        // TODO make player give clues
        // when the player generates a clue, test it with assertTrue(actual.add(clue))! don't want duplicates
        
        assertEquals(expected, actual);
    }
    
    Board generateBoard(Player player, boolean shuffle) {
        List<CodenameWord> words  = new ArrayList<>();
        List<CardValue>    values = new ArrayList<>();
        
        CardValue first = player.getTeam().getValue();
        CardValue other = first==CardValue.RED?CardValue.BLUE:CardValue.RED;
        
        addCardValues(first,             TEAM_CARD_COUNT,  values);
        addCardValues(other,             TEAM_CARD_COUNT,  values);
        addCardValues(CardValue.NEUTRAL, 25-values.size(), values);
        
        char c = 'a';
        for (int i=0; i<25; i++) {
            String w = ""+(c++);
            words.add(new CodenameWord("foo", Arrays.asList(new AssociatedWord(w, 1))));
        }
        
        if (shuffle) {
            Collections.shuffle(words,  random);
            Collections.shuffle(values, random);
        }
        
        return new Board(words, new Keycard(values));
    }
    
    /**
     * Generates clues for all elements in the board matching the player's team
     * @param player 
     * @param board
     * @return a set of clues for all cards matching the player's team
     */
    Set<Clue> generateClues(Player player, Board board) {
        CardValue team = player.getTeam().getValue();
        Set<Clue> ret  = new HashSet<>();
        
        for (int x=0; x<board.getWidth(); x++) {
            for (int y=0; y<board.getLength(); y++) {
                Card card = board.getCard(x, y);
                if (card.getValue()!=team) {
                    continue;
                }
                
                String word = card.getAssociatedWords().get(0).getWord();
                ret.add(new Clue(word, 1));
            }
        }
        
        return ret;
    }
    
    void addCardValues(CardValue value, int count, List<CardValue> dst) {
        for (int i=0; i<count; i++) {
            dst.add(value);
        }
    }
}
