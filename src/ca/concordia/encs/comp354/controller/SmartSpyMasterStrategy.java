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
    public Clue giveClue(SpyMaster owner, ReadOnlyGameState state) {
        List<Coordinates> guesses = beginTurn(owner, state);
        Path databaseFile = Paths.get("res/25wordswithcommonassociatedwords.txt");
        List<CodenameWord> listCodenameWord = listOfCodenameWord (state.boardProperty().get(), owner, state, databaseFile);
        List<CodenameWord.CountFrequencyAssociatedWords> countFrequencyAssociatedWordsList = countFrequencyAssociatedWordsList(listCodenameWord);
        //System.out.println("Random spymaster strategy coordinates of cards to be picked "+ guesses.toString());
        //System.out.println("Number of Guesses to succeed winning the game: "+ guesses.size());
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

    //this method is to collect all associated words and its count to all cluewords  of team using smart spymaster strategy in an arraylist
    private List<CodenameWord.CountFrequencyAssociatedWords> countFrequencyAssociatedWordsList (List<CodenameWord> listOfCodenameword) {
        List<CodenameWord.CountFrequencyAssociatedWords> countFrequencyAssociatedWordsList = new ArrayList<CodenameWord.CountFrequencyAssociatedWords> ();
        final int size = listOfCodenameword.size()*listOfCodenameword.get(0).getAssociatedWords().size();

//        for (CodenameWord c: listOfCodenameword
//        ) {
//            System.out.println(c);
//        }

        // to initialize the arraylist
        for(int k = 0; k < size ;k++){
            CodenameWord.CountFrequencyAssociatedWords init = new CodenameWord.CountFrequencyAssociatedWords("",0);
            countFrequencyAssociatedWordsList.add(init);
        }


        System.out.println(listOfCodenameword.size());
        System.out.println(listOfCodenameword.get(0).getAssociatedWords().size());
        for(int i = 0; i<listOfCodenameword.size();i++){
            for (int k = 0; k<listOfCodenameword.get(i).getAssociatedWords().size(); k++){
                String associatedWord = listOfCodenameword.get(i).getAssociatedWords().get(k).getWord();
                int count = countFrequencyAssociatedWordsList.get(((i)*10)+k).getCount();
                System.out.println("Associated words: i = " + i + " k = " + k + " " + associatedWord);
                System.out.println("Count : " + count);

                //check if associated word is already in countFrequencyAssociatedWordsList
                // if it is already in countFrequencyAssociatedWordsList, increment by 1 the count
                //System.out.println("For k = " + k + ", Boolean value: " + countFrequencyAssociatedWordsList.get(k).getWord().contains(associatedWord));
                System.out.println("String: " + countFrequencyAssociatedWordsList.get(((i-1)*10)+k).getWord());
                if (countFrequencyAssociatedWordsList.contains(associatedWord)) {
                    CodenameWord.CountFrequencyAssociatedWords cfaw = new CodenameWord.CountFrequencyAssociatedWords(associatedWord, ++count);
                    int indexOfSameAssociatedWord = countFrequencyAssociatedWordsList.indexOf(associatedWord);
                    System.out.println("indexOfSameAssociatedWord : " + indexOfSameAssociatedWord);
                    countFrequencyAssociatedWordsList.set(indexOfSameAssociatedWord, cfaw);
                    }
                else {
                    CodenameWord.CountFrequencyAssociatedWords cfaw = new CodenameWord.CountFrequencyAssociatedWords(associatedWord, 1);
                    countFrequencyAssociatedWordsList.set((i*10)+k,cfaw);
                    }
                }

        }

//        for (CodenameWord.CountFrequencyAssociatedWords c: countFrequencyAssociatedWordsList
//             ) {
//            System.out.println(c);
//        }
        return countFrequencyAssociatedWordsList;
    }

    private Clue getAssociatedWord(Board board, Coordinates coords, SpyMaster owner, ReadOnlyGameState state) {
        Card card = board.getCard(coords);
        //System.out.println("First top card: " + board.getCard(0,0));
        //System.out.println("board = " + board);
        //System.out.println("(coords) = " + coords);
        //System.out.println("Board.getCard(coords) = " + card);

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
