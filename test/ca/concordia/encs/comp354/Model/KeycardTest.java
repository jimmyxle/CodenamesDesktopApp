package ca.concordia.encs.comp354.Model;

import ca.concordia.encs.comp354.model.CardValue;
import ca.concordia.encs.comp354.model.Keycard;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.List;



public class KeycardTest {
    static private Keycard testKeycard;

    @Before
    public void before() {
        List<CardValue> cardValues = Keycard.createOrderedKeycard();

        testKeycard = new Keycard(cardValues);
    }



    @Test
    public void getsCorrectKeycardValueXY() {
        /*
            Ordered Keycard:
                         0 1 2 3 4
                       0 R R B B N
                       1 R R B B N
                       2 R R B N N
                       3 R R B N N
                       4 R B B N A
         */

        assertEquals(CardValue.RED, testKeycard.getCardValue(0,0));
        assertEquals(CardValue.RED, testKeycard.getCardValue(3,1));
        assertEquals(CardValue.BLUE, testKeycard.getCardValue(4,1));
        assertEquals(CardValue.BLUE, testKeycard.getCardValue(1,3));
        assertEquals(CardValue.NEUTRAL, testKeycard.getCardValue(2,3));
        assertEquals(CardValue.NEUTRAL, testKeycard.getCardValue(3,4));
        assertEquals(CardValue.ASSASSIN, testKeycard.getCardValue(4,4));
    }

    @Test
    public void createsRandomKeycard() {
        Keycard randomKeycard = Keycard.createRandomKeycard();

        //test will fail every 1/25! times....
        //noinspection SimplifiableJUnitAssertion
        assertFalse(testKeycard.equals(randomKeycard));
    }

    @Test
    public void createsRandomKeycardList() {
        int NUMBER_OF_RANDOM_KEYCARDS = 25;
        List<Keycard> randomKeycards = Keycard.createRandomKeycards(NUMBER_OF_RANDOM_KEYCARDS);

        for (int i = 0; i < NUMBER_OF_RANDOM_KEYCARDS; i++) {
            Keycard cardThatComparesToAllOthers = randomKeycards.get(i);

            for (int j = 0; j < NUMBER_OF_RANDOM_KEYCARDS; j++) {
                if (i != j) {
                    //noinspection SimplifiableJUnitAssertion
                    assertFalse(cardThatComparesToAllOthers.equals(randomKeycards.get(j)));
                }
            }
        }

    }
}