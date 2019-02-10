package ca.concordia.encs.comp354.trash;

import ca.concordia.encs.comp354.model.Card;
import ca.concordia.encs.comp354.model.CardValue;
import ca.concordia.encs.comp354.model.CodenameWord;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CardTest {
    private static CodenameWord testCodenameWord;
    private static final List<CodenameWord.AssociatedWord> associatedWords = new ArrayList<>();
    private static Card testCard;

    @Before
    public void before() {
        CodenameWord.AssociatedWord associatedWord1 = new CodenameWord.AssociatedWord("word1", 100);
        CodenameWord.AssociatedWord associatedWord2 = new CodenameWord.AssociatedWord("word2", 90);
        CodenameWord.AssociatedWord associatedWord3 = new CodenameWord.AssociatedWord("word3", 80);

        associatedWords.add(associatedWord1);
        associatedWords.add(associatedWord2);
        associatedWords.add(associatedWord3);

        testCodenameWord = new CodenameWord("clueword", associatedWords);

        testCard = new Card(testCodenameWord, CardValue.RED);

    }

    @Test
    public void getsCorrectCardCodename() {
        assertEquals("clueword", testCard.getCodename());
    }

    @Test
    public void getsCorrectCardAssociatedWordsAndWeights() {
        assertEquals("word1", testCard.getAssociatedWords().get(0).getWord());
        assertEquals(100, testCodenameWord.getAssociatedWords().get(0).getWeight());

        assertEquals("word2", testCodenameWord.getAssociatedWords().get(1).getWord());
        assertEquals(90, testCodenameWord.getAssociatedWords().get(1).getWeight());

        assertEquals("word3", testCodenameWord.getAssociatedWords().get(2).getWord());
        assertEquals(80, testCodenameWord.getAssociatedWords().get(2).getWeight());
    }


//    @Test
//    void createRandomCodenameList() {
//    }
}