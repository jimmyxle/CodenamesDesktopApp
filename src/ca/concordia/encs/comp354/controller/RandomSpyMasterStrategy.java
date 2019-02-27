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
public class RandomSpyMasterStrategy extends AbstractPlayerStrategy implements SpyMaster.Strategy {

    private final Random random = new Random();

    @Override
    public Clue giveClue(SpyMaster owner, ReadOnlyGameState state) {
        List<Coordinates> guesses = beginTurn(owner, state);
        System.out.println("Random spymaster strategy coordinates of cards to be picked "+ guesses.toString());
        System.out.println("Number of Guesses to succeed winning the game: "+ guesses.size());
        for(int i = 0; i< guesses.size();i++){
            System.out.println(guesses.get(i));
        }

        return guesses.isEmpty()? null : getAssociatedWord(state.boardProperty().get(), guesses.get(random.nextInt(guesses.size())));

    }

/*    private Clue getAssociatedWord(Board board, Coordinates coords) {
        Card card = board.getCard(coords);
        List<AssociatedWord> words = card.getAssociatedWords();
        System.out.println("One of the associated words of " + card.getCodename() + " is " + words.get(random.nextInt(words.size())).getWord());
        return new Clue(words.get(random.nextInt(words.size())).getWord(), 1);
    }*/

    private Clue getAssociatedWord(Board board, Coordinates coords) {
        Card card = board.getCard(coords);
        System.out.println("board = " + board);
        System.out.println("(coords) = " + coords);
        System.out.println("Board.getCard(coords) = " + card);

        List<AssociatedWord> words = card.getAssociatedWords();
        List<CodenameWord.CountFrequencyAssociatedWords> countFrequencyAssociatedWordsList = countFrequencyAssociatedWordsList(words);
        //List<CodenameWord.CountFrequencyAssociatedWords> countFrequencyAssociatedWordsList = countFrequencyAssociatedWordsList(associatedWordsList);
        System.out.println("One of the associated words of " + card.getCodename() + " is " + countFrequencyAssociatedWordsList.get(0).getWord());
        return new Clue(words.get(random.nextInt(words.size())).getWord(), 1);
        //return new Clue(countFrequencyAssociatedWordsList.get(0).getWord(), countFrequencyAssociatedWordsList.get(0).getCount());
    }

    //this method is to collect all associated words in one clueword of team blue using randomspymaster strategy in an arraylist
    private List<CodenameWord.CountFrequencyAssociatedWords> countFrequencyAssociatedWordsList (List<AssociatedWord> codenameWords) {
        int size = codenameWords.size();
        List<CodenameWord.CountFrequencyAssociatedWords> countFrequencyAssociatedWordsList = new ArrayList<CodenameWord.CountFrequencyAssociatedWords> ();
        int count = 0;
        for(int i = 0; i<size;i++){
            String associatedWord = codenameWords.get(i).getWord();
            CodenameWord.CountFrequencyAssociatedWords init = new CodenameWord.CountFrequencyAssociatedWords("",0);
            countFrequencyAssociatedWordsList.add(i,init);
            if (countFrequencyAssociatedWordsList.get(i).getWord().contains(associatedWord)){
                count++;
                CodenameWord.CountFrequencyAssociatedWords cfaw = new CodenameWord.CountFrequencyAssociatedWords(associatedWord,count);
                int indexOfSameAssociatedWord = countFrequencyAssociatedWordsList.indexOf(associatedWord);
                countFrequencyAssociatedWordsList.set(indexOfSameAssociatedWord,cfaw);
                System.out.println("One of the associated word is " + countFrequencyAssociatedWordsList.get(i).getCount());

            }
            else {
                CodenameWord.CountFrequencyAssociatedWords cfaw = new CodenameWord.CountFrequencyAssociatedWords(associatedWord,1);
                countFrequencyAssociatedWordsList.add(i,cfaw);
                System.out.println("One of the associated word is " + countFrequencyAssociatedWordsList.get(i).getCount());
            }
        }
        return countFrequencyAssociatedWordsList;
    }

 /*   private List<String> extractAllClueword (Board board, Coordinates coords){

    }*/

    //this method is to collect all associated words of team blue using randomspymaster strategy in an arraylist
/*    private List<CodenameWord.CountFrequencyAssociatedWords> countFrequencyAssociatedWordsList (List<String> associatedWordsList){
        List<CodenameWord.CountFrequencyAssociatedWords> countFrequencyAssociatedWords = null;
        for(int i = 0; i<associatedWordsList.size(); i++){
            int counter = 0;
            String associatedWordToCompare = associatedWordsList.get(i);
            while(associatedWordToCompare.contains(associatedWordsList.get(i))){
                counter++;
            };
            CodenameWord.CountFrequencyAssociatedWords cfaw = new CodenameWord.CountFrequencyAssociatedWords(associatedWordsList.get(i),counter);
            countFrequencyAssociatedWords.add(i,cfaw);
            System.out.println(countFrequencyAssociatedWords.get(i));
        }
        return countFrequencyAssociatedWords;
    }*/
    @Override
    protected boolean isValidGuess(Player owner, Board board, int x, int y) {
        // valid iff card belongs to our team
        return board.getCard(x, y).getValue() == owner.getTeam().getValue();
    }
}
