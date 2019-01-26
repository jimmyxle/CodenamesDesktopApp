package ca.concordia.encs.comp354.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.CardValue;

/**
 * Dummy implementation of Board to feed fake model data to the GUI.
 * 
 * @author Nikita Leonidov
 *
 */
public class TestBoard extends Board {

    private final List<String>    words;
    private final List<CardValue> values;
    
    public TestBoard() {
        words = new ArrayList<>();
        for (int i=0; i<25; i+=3) {
            words.add("foo");
            words.add("bar");
            words.add("baz");
        }
        
        Collections.shuffle(words);
        
        values = new ArrayList<>();
        values.add(CardValue.ASSASSIN);
        for (int i=0; i<7; i++) {
            values.add(CardValue.RED);
        }
        
        for (int i=0; i<8; i++) {
            values.add(CardValue.BLUE);
        }
        
        for (int i=values.size(); i<25; i++) {
            values.add(CardValue.NEUTRAL);
        }
        
        Collections.shuffle(values);
    }
    
    @Override
    public String getWord(int x, int y) {
        return words.get(y*5 + x);
    }

    @Override
    public CardValue getValue(int x, int y) {
        return values.get(y*5 + x);
    }

    @Override
    public int getWidth() {
        return 5;
    }

    @Override
    public int getLength() {
        return 5;
    }

}
