package ca.concordia.encs.comp354.Model;


import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.CardValue;
import ca.concordia.encs.comp354.model.CodenameWord;
import ca.concordia.encs.comp354.model.CodenameWord.AssociatedWord;

import ca.concordia.encs.comp354.model.Keycard;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class BoardTest {
    private static final List<AssociatedWord> associatedWords = new ArrayList<>();


    private static Board testBoard;


    @Before
    public void before() {
        List<CardValue> cardValues = Keycard.createOrderedKeycard();

        Keycard testKeycard = new Keycard(cardValues);

        AssociatedWord associatedWord1 = new AssociatedWord("word1", 100);
        AssociatedWord associatedWord2 = new AssociatedWord("word2", 90);
        AssociatedWord associatedWord3 = new AssociatedWord("word3", 80);

        associatedWords.add(associatedWord1);
        associatedWords.add(associatedWord2);
        associatedWords.add(associatedWord3);

        List<CodenameWord> codenameWords = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            codenameWords.add(new CodenameWord(Integer.toString(i), associatedWords));
        }

        testBoard = new Board(codenameWords, testKeycard);
    }

    @Test
    public void getsCorrectCardXY() {
        assertEquals("0", testBoard.getCard(0, 0).getCodename());
        assertEquals("1", testBoard.getCard(1, 0).getCodename());
        assertEquals("2", testBoard.getCard(2, 0).getCodename());
        assertEquals("3", testBoard.getCard(3, 0).getCodename());
        assertEquals("4", testBoard.getCard(4, 0).getCodename());
        assertEquals("5", testBoard.getCard(0, 1).getCodename());



        assertEquals("19", testBoard.getCard(4, 3).getCodename());
        assertEquals("20", testBoard.getCard(0, 4).getCodename());
        assertEquals("21", testBoard.getCard(1, 4).getCodename());
        assertEquals("22", testBoard.getCard(2, 4).getCodename());
        assertEquals("23", testBoard.getCard(3, 4).getCodename());
        assertEquals("24", testBoard.getCard(4, 4).getCodename());
    }

    @Test
    public void getsCorrectWidth() {
        assertEquals(5, testBoard.getWidth());
    }

    @Test
    public void getsCorrectLength() {
        assertEquals(5, testBoard.getLength());
    }
}