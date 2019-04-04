package ca.concordia.encs.comp354.controller;

import ca.concordia.encs.comp354.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SpyMasterCountStrategyTest {
    @Test
    public void spymasterGetsMostFrequentAssociatedWord() {
        //Need a Map to keep track of frequencies of associatedWord
        //Need a list of associatedWord to test the increment of count if associatedWord repeats in the list
        Map<String,Integer> wordFrequencies = new HashMap<>();
        List<CodenameWord.AssociatedWord> associatedWordList= new ArrayList<>();

        //Create associatedWords object to put in the list
        CodenameWord.AssociatedWord a0 = new CodenameWord.AssociatedWord("class", 100);
        CodenameWord.AssociatedWord a1 = new CodenameWord.AssociatedWord("apple", 96);
        CodenameWord.AssociatedWord a2 = new CodenameWord.AssociatedWord("pencil", 85);
        CodenameWord.AssociatedWord a3 = new CodenameWord.AssociatedWord("peach", 83);
        CodenameWord.AssociatedWord a4 = new CodenameWord.AssociatedWord("class", 99);
        CodenameWord.AssociatedWord a5 = new CodenameWord.AssociatedWord("computer", 76);
        CodenameWord.AssociatedWord a6 = new CodenameWord.AssociatedWord("book", 67);
        CodenameWord.AssociatedWord a7 = new CodenameWord.AssociatedWord("library", 59);
        CodenameWord.AssociatedWord a8 = new CodenameWord.AssociatedWord("grade", 88);
        CodenameWord.AssociatedWord a9 = new CodenameWord.AssociatedWord("class", 86);

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

        // increment the count if associatedWord repeat in the list, else put the count to 1.
        for(int i = 0; i<associatedWordList.size();i++){
            wordFrequencies.put(associatedWordList.get(i).getWord(), wordFrequencies.getOrDefault(associatedWordList.get(i).getWord(),0)+1);
        }

        //Keep track the associatedWord having the highest count
        Map.Entry<String, Integer> highest = null;
        for (Map.Entry<String,Integer> e : wordFrequencies.entrySet()) {
            if (highest == null || highest.getValue()<e.getValue()) {
                highest = e;
            }
        }

        assertEquals(highest.getKey(),"class");
    }

    @Test(expected = AssertionError.class)
    public void spyMasterFailsToGetsHighestCount() {
        //Need a Map to keep track of frequencies of associatedWord
        //Need a list of associatedWord to test the increment of count if associatedWord repeats in the list
        Map<String,Integer> wordFrequencies = new HashMap<>();
        List<CodenameWord.AssociatedWord> associatedWordList= new ArrayList<>();

        //Create associatedWords object to put in the list
        CodenameWord.AssociatedWord a0 = new CodenameWord.AssociatedWord("class", 100);
        CodenameWord.AssociatedWord a1 = new CodenameWord.AssociatedWord("apple", 96);
        CodenameWord.AssociatedWord a2 = new CodenameWord.AssociatedWord("pencil", 85);
        CodenameWord.AssociatedWord a3 = new CodenameWord.AssociatedWord("peach", 83);
        CodenameWord.AssociatedWord a4 = new CodenameWord.AssociatedWord("class", 99);
        CodenameWord.AssociatedWord a5 = new CodenameWord.AssociatedWord("computer", 76);
        CodenameWord.AssociatedWord a6 = new CodenameWord.AssociatedWord("book", 67);
        CodenameWord.AssociatedWord a7 = new CodenameWord.AssociatedWord("library", 59);
        CodenameWord.AssociatedWord a8 = new CodenameWord.AssociatedWord("grade", 88);
        CodenameWord.AssociatedWord a9 = new CodenameWord.AssociatedWord("class", 86);

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

        // increment the count if associatedWord repeat in the list, else put the count to 1.
        for(int i = 0; i<associatedWordList.size();i++){
            wordFrequencies.put(associatedWordList.get(i).getWord(), wordFrequencies.getOrDefault(associatedWordList.get(i).getWord(),0)+1);
        }

        //Keep track the associatedWord having the highest count
        Map.Entry<String, Integer> highest = null;
        for (Map.Entry<String,Integer> e : wordFrequencies.entrySet()) {
            if (highest == null || highest.getValue()<e.getValue()) {
                highest = e;
            }
        }

        assertEquals(highest.getValue(), (Integer)1);
    }
}