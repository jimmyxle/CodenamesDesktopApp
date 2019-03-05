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

/**
 * With this strategy, a spymaster picks a clue for a random card on the board.
 * @author Alexandre Kang
 *
 */
public class SmartSpyMasterStrategy extends AbstractPlayerStrategy implements SpyMaster.Strategy {

    private final Random random = new Random();

    @Override
    //this method is the clue given by the spymaster
    public Clue giveClue(SpyMaster owner, ReadOnlyGameState state) {
        List<Coordinates> guesses = beginTurn(owner, state);
//        System.out.println("State property : " + state.boardProperty().get());
//        System.out.println("Guesses size : " + guesses.size());
//        System.out.println("Guesses random int : " + guesses.get(random.nextInt(guesses.size())));
        System.out.println("Owner : " + owner.toString());
        System.out.println("State : " + state.toString());
        return guesses.isEmpty()? null : getAssociatedWord(state.boardProperty().get(), guesses.get(random.nextInt(guesses.size())), owner, state);

    }

    //this method is to generate a clue containing an associated word and the count corresponding this associated word
    // by the spymaster
    private Clue getAssociatedWord(Board board, Coordinates coords, SpyMaster owner, ReadOnlyGameState state) {
        Card card = board.getCard(coords);
        Path databaseFile = Paths.get("res/25wordswithcommonassociatedwords.txt");
        //Path databaseFile = Paths.get("res/words.txt");
        List<CodenameWord> listOfCodenameWord = listOfCodenameWord(state.boardProperty().get(),owner,state,databaseFile);
        List<CodenameWord.CountFrequencyAssociatedWords> countFrequencyAssociatedWordsList = countFrequencyAssociatedWordsList(listOfCodenameWord);
        int index = random.nextInt(countFrequencyAssociatedWordsList.size());
        System.out.println("Index : " + index);
        System.out.println("Word : " + countFrequencyAssociatedWordsList.get(index).getWord());
        System.out.println("Count : " + countFrequencyAssociatedWordsList.get(index).getCount());
        return new Clue(countFrequencyAssociatedWordsList.get(index).getWord(), countFrequencyAssociatedWordsList.get(index).getCount());
    }

    // this method is to collect all cluewords with its associated word and their weight of team using smart spymaster
    // strategy.
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
        scanner.close();

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
        }

        //look if arrayClueword[i] is in lineContent
        for (int i = 0; i < arrayClueword.length;i++){
            for(int k = 0; k < lineContent.size();k++){
                // if arrayClueword[i] is in lineContent, create a list of associated word named
                // listOfAssociatedWord for this arrayClueword[i]
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
                        listOfAssociatedWord.add(associatedWordAndWeight);

                        // read all others associatedwords for each arrayClueword[i] and put them in listOfAssociatedWord
                        for(int j = 0; j < 9; j++){
                            extractAssociatedWord = scannerRemoveClueword.next();
                            associatedWord = extractAssociatedWord.substring(2, extractAssociatedWord.indexOf("',"));
                            stringWeight = extractAssociatedWord.substring(extractAssociatedWord.indexOf("ht=") + 3, extractAssociatedWord.indexOf("}"));
                            weight = Integer.parseInt(stringWeight);
                            associatedWordAndWeight = new AssociatedWord(associatedWord, weight);
                            listOfAssociatedWord.add(associatedWordAndWeight);
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    //skip all k once found arrayClueword[i] in listContent
                    k=lineContent.size();
                }
            }
        }

        // Create CodenameWord object using arrayClueword[i] and listOfAssociatedWord
        for (int c = 0; c < guesses.size();c++){
            CodenameWord codenameWord = new CodenameWord(arrayClueword[c],listOfAssociatedWord.subList(c*10,((c*10)+9)));
            listOfCodenameWord.add(codenameWord);
        }
       return listOfCodenameWord;
    }

    //this method is to collect all associated words and its count to all cluewords of team using smart spymaster
    // strategy.
    private List<CodenameWord.CountFrequencyAssociatedWords> countFrequencyAssociatedWordsList (List<CodenameWord> listOfCodenameword) {
        List<CodenameWord.CountFrequencyAssociatedWords> countFrequencyAssociatedWordsList = new ArrayList<CodenameWord.CountFrequencyAssociatedWords> ();
        int codenameWordSize = listOfCodenameword.size();
        int associatedWordSize = listOfCodenameword.get(0).getAssociatedWords().size();
        int totalSize = (codenameWordSize*10+associatedWordSize-codenameWordSize);

        // to initialize the arraylist
            for(int i = 0; i<totalSize;i++){
                    CodenameWord.CountFrequencyAssociatedWords init = new CodenameWord.CountFrequencyAssociatedWords("",0);
                    countFrequencyAssociatedWordsList.add(init);
                }

        //for each codenameword
        for(int i = 0; i<listOfCodenameword.size();i++){
            String clueword = listOfCodenameword.get(i).getClueWord();
            System.out.println("listOfCodenameword.get(" + i + ").getClueWord() : " + clueword);
            //read each associated word in for each codenameword
            for (int k = 0; k<listOfCodenameword.get(i).getAssociatedWords().size(); k++) {
                String associatedWord = listOfCodenameword.get(i).getAssociatedWords().get(k).getWord();
                System.out.println("listOfCodenameword.get(" + i + ").getAssociatedWords().get(" + k + ").getWord() : " + associatedWord);
                CodenameWord.CountFrequencyAssociatedWords cfaw = new CodenameWord.CountFrequencyAssociatedWords(associatedWord, 1);
                countFrequencyAssociatedWordsList.set((i * 10 + k - i), cfaw);
            }
        } //result: countFrequencyAssociatedWordsList contains all associated word and a count of 1. This list contains duplicates associated word

        for (CodenameWord.CountFrequencyAssociatedWords c: countFrequencyAssociatedWordsList
             ) {
            System.out.println("Origin : " + c);
        }

        // array of frequencies of associated word
        int [] c = getFrequency(countFrequencyAssociatedWordsList);

        for(int i = 0; i<listOfCodenameword.size();i++){
            //read each associated word in each codenameword
            for (int k = 0; k<listOfCodenameword.get(i).getAssociatedWords().size(); k++) {
                String associatedWord = listOfCodenameword.get(i).getAssociatedWords().get(k).getWord();
                if(c[i * 10 + k - i] == 1){
                    CodenameWord.CountFrequencyAssociatedWords cfaw = new CodenameWord.CountFrequencyAssociatedWords(associatedWord, 1);
                    countFrequencyAssociatedWordsList.set((i * 10 + k - i), cfaw);
                }
                else {
                    CodenameWord.CountFrequencyAssociatedWords cfaw = new CodenameWord.CountFrequencyAssociatedWords(associatedWord, c[i * 10 + k - i]);
                    countFrequencyAssociatedWordsList.set((i * 10 + k - i), cfaw);
                }
            }
        }

        // to remove duplicata of countFrequencyAssociatedWordsList.get(i).getWord(), but keep the total count
        for(int i = 0; i<countFrequencyAssociatedWordsList.size();i++) {
            String elementToRemove = countFrequencyAssociatedWordsList.get(i).getWord();
            for(int j = 0; j< countFrequencyAssociatedWordsList.size();j++)
            if (i == j) {
                continue;
            }
            else if (elementToRemove.equals(countFrequencyAssociatedWordsList.get(j).getWord()) || countFrequencyAssociatedWordsList.get(j).getWord().equals("")) {
                countFrequencyAssociatedWordsList.remove(j);
            }
        }

        for (CodenameWord.CountFrequencyAssociatedWords a: countFrequencyAssociatedWordsList
        ) {
            System.out.println("After : " + a);
        }
        return countFrequencyAssociatedWordsList;
    }

    //this method is to count frequencies of all associated words of all cluewords of team using smart spymaster
    // strategy in an array of integer
    // countFrequencyAssociatedWordsList = associatedWord (i), counter [i];
    private int [] getFrequency(List<CodenameWord.CountFrequencyAssociatedWords> countFrequencyAssociatedWordsList) {
        int[] counter = new int[countFrequencyAssociatedWordsList.size()];
        // initialize array counter since we know all countFrequencyAssociatedWordsList contains at least 1 frequency
        for(int i = 0; i<counter.length;i++){
            counter[i] = 1;
        }
        for (int i = 0; i < countFrequencyAssociatedWordsList.size(); i++) {
            String associatedWord = countFrequencyAssociatedWordsList.get(i).getWord();
            for (int j = 0; j < countFrequencyAssociatedWordsList.size(); j++) {
                if (i == j || associatedWord.equals("")) {
                    continue;
                } else {
                    if (associatedWord.equals(countFrequencyAssociatedWordsList.get(j).getWord())) {
                        ++counter[i];
                    }
                }
            }
        }
        return counter;
    }

    @Override
    protected boolean isValidGuess(Player owner, Board board, int x, int y) {
        // valid iff card belongs to our team
        return board.getCard(x, y).getValue() == owner.getTeam().getValue();
    }
}