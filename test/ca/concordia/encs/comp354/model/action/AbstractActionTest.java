package ca.concordia.encs.comp354.model.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.CardValue;
import ca.concordia.encs.comp354.model.CodenameWord;
import ca.concordia.encs.comp354.model.GameAction;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Keycard;
import ca.concordia.encs.comp354.model.CodenameWord.AssociatedWord;

/**
 * Initializes model state for {@link GameAction} tests.
 * @author Mykyta Leonidov
 *
 */
public class AbstractActionTest {

    protected GameState model;
    
    protected AbstractActionTest() {
        this(Keycard.createRandomKeycard());
    }
    
    AbstractActionTest(CardValue...keycardValues) {
        this(new Keycard(Arrays.asList(keycardValues)));
    }
    
    AbstractActionTest(Keycard keycard) {
        List<CodenameWord> words = new ArrayList<>();
        
        for (int i=0; i<25; i++) {
            CodenameWord w = new CodenameWord("foo", Arrays.asList(new AssociatedWord("bar", 1)));
            words.add(w);
        }
        
        model = new GameState(new Board(words, keycard));
    }
}
