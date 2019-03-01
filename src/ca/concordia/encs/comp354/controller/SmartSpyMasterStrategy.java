package ca.concordia.encs.comp354.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ca.concordia.encs.comp354.model.*;
import ca.concordia.encs.comp354.model.CodenameWord.AssociatedWord;


/**
 * With this strategy, a spymaster picks a clue for a random card on the board.
 * @author Nikita Leonidov
 *
 */
public class SmartSpyMasterStrategy extends AbstractPlayerStrategy implements SpyMaster.Strategy {

    private final Random random = new Random();

    @Override
    public Clue giveClue(SpyMaster owner, ReadOnlyGameState state) {
        List<Coordinates> guesses = beginTurn(owner, state);
        System.out.println("Random spymaster strategy coordinates of cards to be picked "+ guesses.toString());
        System.out.println("Number of Guesses to succeed winning the game: "+ guesses.size());
        return guesses.isEmpty()? null : getAssociatedWord(state.boardProperty().get(), guesses.get(random.nextInt(guesses.size())), owner, state);

    }

/*    private Clue getAssociatedWord(Board board, Coordinates coords) {
        Card card = board.getCard(coords);
        List<AssociatedWord> words = card.getAssociatedWords();
        System.out.println("One of the associated words of " + card.getCodename() + " is " + words.get(random.nextInt(words.size())).getWord());
        return new Clue(words.get(random.nextInt(words.size())).getWord(), 1);
    }*/

    // this method is to collect all cluewords of team using smart spymaster strategy.
    private List<String> listOfClueword (Board board, SpyMaster owner, ReadOnlyGameState state) {
       List<String> listOfClueword = new ArrayList<String>();
       List<Coordinates> guesses = beginTurn(owner, state);
       for(int i = 0; i< guesses.size();i++) {
           char x;
           x = guesses.get(i).toString().charAt(15);
           int intx = Character.getNumericValue(x);
           char y;
           y = guesses.get(i).toString().charAt(20);
           int inty = Character.getNumericValue(y);
           listOfClueword.add(i,board.getCard(intx,inty).getCodename());
           System.out.println("Element " + i + ": " + listOfClueword.get(i));
       }
       return listOfClueword;
    }

    //this method is to collect all associated words and its count to all cluewords  of team using smart spymaster strategy in an arraylist
    private List<CodenameWord.CountFrequencyAssociatedWords> countFrequencyAssociatedWordsList (List<String> listOfClueword, List<AssociatedWord> listOfAssociatedWords) {
        List<CodenameWord.CountFrequencyAssociatedWords> countFrequencyAssociatedWordsList = new ArrayList<CodenameWord.CountFrequencyAssociatedWords> ();

        // to initialize the arraylist
        for(int k = 0; k < listOfClueword.size()*listOfAssociatedWords.size();k++){
            CodenameWord.CountFrequencyAssociatedWords init = new CodenameWord.CountFrequencyAssociatedWords("",0);
            countFrequencyAssociatedWordsList.add(k,init);
        }

        for(int i = 0; i<listOfClueword.size();i++){
            for(int j = 0; j<listOfAssociatedWords.size();j++) {
                int count = countFrequencyAssociatedWordsList.get(j).getCount();
                String associatedWord = listOfAssociatedWords.get(j).getWord();
                System.out.println("Associated word: " + associatedWord);
                //if associated word is already in countFrequencyAssociatedWordsList
                if (countFrequencyAssociatedWordsList.get(j).getWord().contains(associatedWord)) {
                    CodenameWord.CountFrequencyAssociatedWords cfaw = new CodenameWord.CountFrequencyAssociatedWords(associatedWord, ++count);
                    int indexOfSameAssociatedWord = countFrequencyAssociatedWordsList.indexOf(associatedWord);
                    System.out.println("indexOfSameAssociatedWord : " + indexOfSameAssociatedWord);
                    countFrequencyAssociatedWordsList.set(indexOfSameAssociatedWord, cfaw);
                    System.out.println("One of the associated word is " + countFrequencyAssociatedWordsList.get(j) + " and its count is: " + countFrequencyAssociatedWordsList.get(j).getCount());
                }
                //else add (beware that "i" can be greater than 0, so it will not overwrite index [0-9])
                else if(i!=0) {
                    CodenameWord.CountFrequencyAssociatedWords cfaw = new CodenameWord.CountFrequencyAssociatedWords(associatedWord, 1);
                    countFrequencyAssociatedWordsList.add((i*10)+j, cfaw);
                    System.out.println("One of the associated word is " + countFrequencyAssociatedWordsList.get(j).getCount());
                }
                else {
                        CodenameWord.CountFrequencyAssociatedWords cfaw = new CodenameWord.CountFrequencyAssociatedWords(associatedWord, 1);
                        countFrequencyAssociatedWordsList.add(j, cfaw);
                        System.out.println("One of the associated word is " + countFrequencyAssociatedWordsList.get(j).getCount());
                    }
                }
            }
        return countFrequencyAssociatedWordsList;
    }

    private Clue getAssociatedWord(Board board, Coordinates coords, SpyMaster owner, ReadOnlyGameState state) {
        Card card = board.getCard(coords);
        System.out.println("First top card: " + board.getCard(0,0));
        System.out.println("board = " + board);
        System.out.println("(coords) = " + coords);
        System.out.println("Board.getCard(coords) = " + card);


        List<String> listOfClueword = new ArrayList<>();
        listOfClueword = listOfClueword(state.boardProperty().get(),owner,state);

        List<AssociatedWord> words = card.getAssociatedWords();
        List<CodenameWord.CountFrequencyAssociatedWords> countFrequencyAssociatedWordsList = countFrequencyAssociatedWordsList(listOfClueword,words);
        //List<CodenameWord.CountFrequencyAssociatedWords> countFrequencyAssociatedWordsList = countFrequencyAssociatedWordsList(associatedWordsList);
        System.out.println("One of the associated words of " + card.getCodename() + " is " + countFrequencyAssociatedWordsList.get(0).getWord());
        return new Clue(words.get(random.nextInt(words.size())).getWord(), 1);
        //return new Clue(countFrequencyAssociatedWordsList.get(0).getWord(), countFrequencyAssociatedWordsList.get(0).getCount());
    }

    @Override
    protected boolean isValidGuess(Player owner, Board board, int x, int y) {
        // valid iff card belongs to our team
        return board.getCard(x, y).getValue() == owner.getTeam().getValue();
    }
}
