package ca.concordia.encs.comp354.controller;

import ca.concordia.encs.comp354.model.CodenameWord;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SpyMasterWeightStrategyTest {

    @Test
    public void spymasterGetsWordOfGreatestWeight() {

        //Need a Map to keep track of frequencies of associatedWord
        //Need a list of associatedWord to test the increment of count if associatedWord repeats in the list
        Map<String,Integer> wordWeight = new HashMap<>();
        List<CodenameWord.AssociatedWord> associatedWordList= new ArrayList<>();

        //Create associatedWords object to put in the list
        CodenameWord.AssociatedWord a0 = new CodenameWord.AssociatedWord("class", 100);
        CodenameWord.AssociatedWord a1 = new CodenameWord.AssociatedWord("apple", 100);
        CodenameWord.AssociatedWord a2 = new CodenameWord.AssociatedWord("pencil", 85);
        CodenameWord.AssociatedWord a3 = new CodenameWord.AssociatedWord("peach", 83);
        CodenameWord.AssociatedWord a4 = new CodenameWord.AssociatedWord("class", 100);
        CodenameWord.AssociatedWord a5 = new CodenameWord.AssociatedWord("computer", 76);
        CodenameWord.AssociatedWord a6 = new CodenameWord.AssociatedWord("book", 67);
        CodenameWord.AssociatedWord a7 = new CodenameWord.AssociatedWord("apple", 100);
        CodenameWord.AssociatedWord a8 = new CodenameWord.AssociatedWord("grade", 88);
        CodenameWord.AssociatedWord a9 = new CodenameWord.AssociatedWord("class", 100);

        //Put the created object in the list
        associatedWordList.add(a0);
        associatedWordList.add(a1);
        associatedWordList.add(a2);
        associatedWordList.add(a3);
        associatedWordList.add(a4);
        associatedWordList.add(a5);
        associatedWordList.add(a6);
        associatedWordList.add(a7);
        associatedWordList.add(a8);
        associatedWordList.add(a9);

        // increment the count if associatedWord having a weight of 100 repeat in the list, else put the count to 1.
        for(int i = 0; i<associatedWordList.size();i++){
            if (associatedWordList.get(i).getWeight() == 100) {
                if (wordWeight.containsKey(associatedWordList.get(i).getWord())) {
                    Integer freq = wordWeight.get(associatedWordList.get(i).getWord());
                    ++freq;
                    wordWeight.put(associatedWordList.get(i).getWord(), freq);
                } else {
                    wordWeight.put(associatedWordList.get(i).getWord(), 1);
                }
            }

        }

        //Keep track the associatedWord having the highest count
        Map.Entry<String, Integer> highest = null;
        for (Map.Entry<String,Integer> e : wordWeight.entrySet()) {
            if (highest == null || highest.getValue()<e.getValue()) {
                highest = e;
            }
        }

        //assertEquals(highest.getKey(),"class");
        assertEquals(highest.getValue(), (Integer)3);
    }

    @Test(expected = AssertionError.class)
    public void spymasterFailsToGetWordOfGreatestWeight() {

            //Need a Map to keep track of frequencies of associatedWord
            //Need a list of associatedWord to test the increment of count if associatedWord repeats in the list
            Map<String,Integer> wordWeight = new HashMap<>();
            List<CodenameWord.AssociatedWord> associatedWordList= new ArrayList<>();

            //Create associatedWords object to put in the list
            CodenameWord.AssociatedWord a0 = new CodenameWord.AssociatedWord("class", 100);
            CodenameWord.AssociatedWord a1 = new CodenameWord.AssociatedWord("apple", 100);
            CodenameWord.AssociatedWord a2 = new CodenameWord.AssociatedWord("pencil", 85);
            CodenameWord.AssociatedWord a3 = new CodenameWord.AssociatedWord("peach", 83);
            CodenameWord.AssociatedWord a4 = new CodenameWord.AssociatedWord("class", 100);
            CodenameWord.AssociatedWord a5 = new CodenameWord.AssociatedWord("computer", 76);
            CodenameWord.AssociatedWord a6 = new CodenameWord.AssociatedWord("book", 67);
            CodenameWord.AssociatedWord a7 = new CodenameWord.AssociatedWord("apple", 100);
            CodenameWord.AssociatedWord a8 = new CodenameWord.AssociatedWord("grade", 88);
            CodenameWord.AssociatedWord a9 = new CodenameWord.AssociatedWord("class", 100);

            //Put the created object in the list
            associatedWordList.add(a0);
            associatedWordList.add(a1);
            associatedWordList.add(a2);
            associatedWordList.add(a3);
            associatedWordList.add(a4);
            associatedWordList.add(a5);
            associatedWordList.add(a6);
            associatedWordList.add(a7);
            associatedWordList.add(a8);
            associatedWordList.add(a9);

            // increment the count if associatedWord having a weight of 100 repeat in the list, else put the count to 1.
            for(int i = 0; i<associatedWordList.size();i++){
                if (associatedWordList.get(i).getWeight() == 100) {
                    if (wordWeight.containsKey(associatedWordList.get(i).getWord())) {
                        Integer freq = wordWeight.get(associatedWordList.get(i).getWord());
                        ++freq;
                        wordWeight.put(associatedWordList.get(i).getWord(), freq);
                    } else {
                        wordWeight.put(associatedWordList.get(i).getWord(), 1);
                    }
                }

            }

            //Keep track the associatedWord having the highest count
            Map.Entry<String, Integer> highest = null;
            for (Map.Entry<String,Integer> e : wordWeight.entrySet()) {
                if (highest == null || highest.getValue()<e.getValue()) {
                    highest = e;
                }
            }

            //test that does not work (we expect to fail)
            assertEquals(highest.getKey(),"apple");
        }
    }