package ca.concordia.encs.comp354.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.stream.Stream;

import ca.concordia.encs.comp354.model.*;
import ca.concordia.encs.comp354.model.CodenameWord.AssociatedWord;

import static java.lang.Integer.*;


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

    // this method is to collect all cluewords with its associated word and their weight of team using smart spymaster strategy.
    private List<CodenameWord> listOfCodenameWord (Board board, SpyMaster owner, ReadOnlyGameState state, Path databaseFile) {
       ArrayList<CodenameWord> listOfCodenameWord = new ArrayList<CodenameWord>();
       ArrayList<AssociatedWord> listOfAssociatedWord = new ArrayList<>();
       List<Coordinates> guesses = beginTurn(owner, state);
       ArrayList<String> lineContent = new ArrayList<>();
       String[] arrayClueword = new String[guesses.size()];

       //put all database in an arraylist of string named lineContent
        File file = new File(databaseFile.toString());
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNextLine()){
            lineContent.add(scanner.nextLine());
        }

//        for (String s: lineContent){
//            System.out.println("Linecontent: "+ s);
//        }

        // get only the clueword that the team using smart spymaster strategy pattern and
        // put them in an array named arrayClueword
        for(int i = 0; i< guesses.size();i++) {
            char x;
            x = guesses.get(i).toString().charAt(15);
            int intx = Character.getNumericValue(x);
            char y;
            y = guesses.get(i).toString().charAt(20);
            int inty = Character.getNumericValue(y);
            String clueword = board.getCard(intx, inty).getCodename();
            arrayClueword[i] = clueword;
            System.out.println("Clueword[" + i + "] is: " + arrayClueword[i]);
        }

        //look if arrayClueword[i] is in lineContent
        for (int i = 0; i < arrayClueword.length;i++){
            for(int k = 0; k < lineContent.size();k++){
                //System.out.println("arrayClueword of " + i + " : " + arrayClueword[i]);
                //System.out.println("LineContent of " + k + " : " + lineContent.get(k));
                // if arrayClueword[i] is in lineContent, create a list of associated word for this arrayClueword[i]
                if(lineContent.get(k).indexOf(arrayClueword[i])!=-1){
                    try (Stream<String> lines = Files.lines(Paths.get(databaseFile.toString()))) {
                        String s = lines.skip(k).findFirst().get();
                        Scanner scannerRemoveClueword = new Scanner(s).useDelimiter("\\s*associatedWord\\s*");
                        scannerRemoveClueword.next();
                        String extractAssociatedWord = scannerRemoveClueword.next();
                        String associatedWord = extractAssociatedWord.substring(2, extractAssociatedWord.indexOf("',"));
                        String stringWeight = extractAssociatedWord.substring(extractAssociatedWord.indexOf("ht=") + 3, extractAssociatedWord.indexOf("}"));
                        int weight = Integer.parseInt(stringWeight);
                        AssociatedWord associatedWordAndWeight = new AssociatedWord(associatedWord, weight);
                        System.out.println("1: " + associatedWordAndWeight.toString());
                        listOfAssociatedWord.add(associatedWordAndWeight);
                        System.out.println("1 - List of associated words: " + listOfAssociatedWord);
                        while (scannerRemoveClueword.hasNextLine()) {
                            extractAssociatedWord = scannerRemoveClueword.next();
                            associatedWord = extractAssociatedWord.substring(2, extractAssociatedWord.indexOf("',"));
                            stringWeight = extractAssociatedWord.substring(extractAssociatedWord.indexOf("ht=") + 3, extractAssociatedWord.indexOf("}"));
                            weight = Integer.parseInt(stringWeight);
                            associatedWordAndWeight = new AssociatedWord(associatedWord, weight);
                            System.out.println("In the while loop: " + associatedWordAndWeight.toString());
                            listOfAssociatedWord.add(associatedWordAndWeight);
                            System.out.println("In the while loop - List of associated words: " + listOfAssociatedWord);
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    //System.out.println("Element " + i + ": " + listOfCodenameWord.get(i));
                    CodenameWord codenameWord = new CodenameWord(arrayClueword[i],listOfAssociatedWord);
                    listOfCodenameWord.add(i,codenameWord);
                }
            }

//            for (CodenameWord c:listOfCodenameWord
//                 ) { System.out.println(c);
//            }
        }
       return listOfCodenameWord;
    }

    //this method is to collect all associated words and its count to all cluewords  of team using smart spymaster strategy in an arraylist
    private List<CodenameWord.CountFrequencyAssociatedWords> countFrequencyAssociatedWordsList (List<CodenameWord> listOfCodenameword, List<AssociatedWord> listOfAssociatedWords) {
        List<CodenameWord.CountFrequencyAssociatedWords> countFrequencyAssociatedWordsList = new ArrayList<CodenameWord.CountFrequencyAssociatedWords> ();

        // to initialize the arraylist
        for(int k = 0; k < listOfCodenameword.size()*listOfCodenameword.get(k).getAssociatedWords().size();k++){
        //for(int k = 0; k < listOfClueword.size()*listOfAssociatedWords.size();k++){
            CodenameWord.CountFrequencyAssociatedWords init = new CodenameWord.CountFrequencyAssociatedWords("",0);
            countFrequencyAssociatedWordsList.add(k,init);
        }

        for(int i = 0; i<listOfCodenameword.size(); i++) {
            System.out.println("i : " + i);
            for(int j = 0; j<listOfAssociatedWords.size();j++) {
                int count = countFrequencyAssociatedWordsList.get(j).getCount();
                System.out.println(count);
                String associatedWord = listOfCodenameword.get(i).getAssociatedWords().get(j).getWord();
                //String associatedWord = listOfAssociatedWords.get(j).getWord();
                System.out.println("Word in countFrequencyAssociatedWordsList at " + j + " " + countFrequencyAssociatedWordsList.get(j).getWord());
                System.out.println("Associated word: " + associatedWord);
                System.out.println("Boolean result: "+ countFrequencyAssociatedWordsList.get(j).getWord().contains(associatedWord));

                if(i!=0) {
                    associatedWord = listOfCodenameword.get(i).getAssociatedWords().get(i).getWord();
                    System.out.println("Clueword # " + i + " " + associatedWord);
                }

                //if associated word is already in countFrequencyAssociatedWordsList
                if (countFrequencyAssociatedWordsList.get(j).getWord().contains(associatedWord)) {
                    CodenameWord.CountFrequencyAssociatedWords cfaw = new CodenameWord.CountFrequencyAssociatedWords(associatedWord, ++count);
                    int indexOfSameAssociatedWord = countFrequencyAssociatedWordsList.indexOf(associatedWord);
                    System.out.println("indexOfSameAssociatedWord : " + indexOfSameAssociatedWord);
                    countFrequencyAssociatedWordsList.set(indexOfSameAssociatedWord, cfaw);
                    System.out.println("One of the associated word is " + countFrequencyAssociatedWordsList.get(j).getWord() + " and its count is: " + countFrequencyAssociatedWordsList.get(j).getCount());
                }
                //else add (beware that "i" can be greater than 0, so it will not overwrite index [0-9])
                else if(i!=0) {
                    //TODO: BIG PROBLEM : need to reach the second clueword and its associative words
                    //associatedWord = listOfClueword.get(i);
                    //System.out.println("Clueword # " + i + " " + associatedWord);
                    CodenameWord.CountFrequencyAssociatedWords cfaw = new CodenameWord.CountFrequencyAssociatedWords(associatedWord, 1);
                    countFrequencyAssociatedWordsList.add((i*10)+j, cfaw);
                    System.out.println("One of the associated word is " + countFrequencyAssociatedWordsList.get((i*10)+j).getWord() + " and its count is: " + countFrequencyAssociatedWordsList.get((i*10)+j).getCount());
                }
                else {
                    CodenameWord.CountFrequencyAssociatedWords cfaw = new CodenameWord.CountFrequencyAssociatedWords(associatedWord, 1);
                    countFrequencyAssociatedWordsList.add(j, cfaw);
                    System.out.println("One of the associated word is " + countFrequencyAssociatedWordsList.get(j).getWord() + " and its count is: " + countFrequencyAssociatedWordsList.get(j).getCount());
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

        Path databaseFile = Paths.get("res/25wordswithcommonassociatedwords.txt");
        List<CodenameWord>listOfCodenameWord = listOfCodenameWord(state.boardProperty().get(),owner,state,databaseFile);
        List<String> listOfClueword = new ArrayList<>();
        // listOfClueword = listOfClueword(state.boardProperty().get(),owner,state);

        List<AssociatedWord> words = card.getAssociatedWords();
        //List<CodenameWord.CountFrequencyAssociatedWords> countFrequencyAssociatedWordsList = countFrequencyAssociatedWordsList(listOfClueword,words);
        //List<CodenameWord.CountFrequencyAssociatedWords> countFrequencyAssociatedWordsList = countFrequencyAssociatedWordsList(associatedWordsList);
        //System.out.println("One of the associated words of " + card.getCodename() + " is " + countFrequencyAssociatedWordsList.get(0).getWord());
        return new Clue(words.get(random.nextInt(words.size())).getWord(), 1);
        //return new Clue(countFrequencyAssociatedWordsList.get(0).getWord(), countFrequencyAssociatedWordsList.get(0).getCount());
    }

    @Override
    protected boolean isValidGuess(Player owner, Board board, int x, int y) {
        // valid iff card belongs to our team
        return board.getCard(x, y).getValue() == owner.getTeam().getValue();
    }
}
