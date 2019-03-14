package ca.concordia.encs.comp354.model;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


/**
 * Represents a card - one of the twenty five placed on the board for the game. Words & an associated word list are
 * parsed from a database text file.
 *
 * Strategy Implemented: a simple factory method for creating a card.
 *
 * @author Zachary Hynes
 * @author Nikita Leonidov
 * @author Alexandre Kang
 *
 */
public class Card {
    //============================
    //---------VARIABLES---------
    //============================
    final private CodenameWord codename;
    final private CardValue    value;


    //============================
    //--------CONSTRUCTORS--------
    //============================
    public Card(CodenameWord codename, CardValue value) {
        this.codename = codename;
        this.value    = value;
    }


    //============================
    //----------METHODS----------
    //============================

    public String getCodename() {
        return codename.getClueWord();
    }

    public List<CodenameWord.AssociatedWord> getAssociatedWords() {
        return codename.getAssociatedWords();
    }

    public CardValue getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Card{" +
                "codename='" + codename + '\'' +
                ", associatedWords=" + getAssociatedWords() +
                ", typeOfCard=" + getValue() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(codename, card.codename) &&
                Objects.equals(value, card.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codename, value);
    }

    /**
     * Returns a list of 25 Codename Words to be used to create cards
     * @return a list containing 25 Codename Words
     */
    public static List<CodenameWord> createRandomCodenameList(Path databaseFile) throws IOException {
        //parse database for 25 words
        List <String> words = parseDatabaseFile(databaseFile);

        //turn the 25 words into 25 CodenameWord Objects
        return generateCodenameWordList(words);
    }//END OF createRandomCodenameList()

    private static List<String> parseDatabaseFile(Path databaseFile) throws IOException {
        //====================
        //--PARSING DATABASE--
        //====================

        List <String> lines = new ArrayList<>(Files.readAllLines(databaseFile));
        Collections.shuffle(lines);
        return lines.subList(0,25);
    }//END OF parseDatabaseFile()
    

    private static List<CodenameWord> generateCodenameWordList(List<String> words) {
        List<CodenameWord> codenameWordList = new ArrayList<>();

        //for each word, create a codenameWord and insert it into the codenameWordList
        for (String word : words) {
            //get the associated word list for the CodeName
            CodenameWord newCodenameWord = parseCodenameObject(word);

            //add it to the CodenameWordList
            codenameWordList.add(newCodenameWord);
        }

        return codenameWordList;
    }//END OF generateCodenameWordList(String[] words)

    private static CodenameWord parseCodenameObject(String str) {
        //get the codeName from the string
        int startIndex = str.indexOf("'")+1;
        int endIndex = str.indexOf("',");
        String codeName = str.substring(startIndex, endIndex);

        //====================
        //--------TEST--------
        //====================
//        //print out the codeName to see it's value
//        System.out.println("codeName value: " + codeName);

        //split the remaining string into associated words
        String[] associatedWords = str.split("associatedWord='");

        //====================
        //--------TEST--------
        //====================
//        //print out the string array
//        for (String s : associatedWords) {
//            System.out.println("split string value: " + s);
//        }

        //create an AssociatedWordList
        List<CodenameWord.AssociatedWord> associatedWordList = new ArrayList<>();

        //put each associated word into an AssociatedWord object
        for (int i = 1; i < associatedWords.length; i++) {
            //parse the associatedWord from the string
            String associatedWord = associatedWords[i];
            int indexEndOfWord = associatedWord.indexOf("',");
            String capturedWord = associatedWord.substring(0, indexEndOfWord) ;

            //parse the associatedWord Weight from the string
            int indexStartOfWeight = associatedWord.indexOf("=")+1;
            int indexEndOfWeight = associatedWord.indexOf("}");
            String weight = associatedWord.substring(indexStartOfWeight, indexEndOfWeight);
            int associatedWordWeight = Integer.parseInt(weight);

            //create a new AssociatedWord object
            CodenameWord.AssociatedWord newWord = new CodenameWord.AssociatedWord(capturedWord, associatedWordWeight);

            //add the AssociatedWord Object to the AssociatedWordList
            associatedWordList.add(newWord);
        }

        //====================
        //--------TEST--------
        //====================
//        //print out the Associated Word List
//        int count = 0;
//        System.out.println("Associated Word List:");
//        for (CodenameWord.AssociatedWord s : associatedWordList) {
//            System.out.println(count + " : " + s);
//            count++;
//        }

        return new CodenameWord(codeName, associatedWordList);
    }//END OF parseCodenameObject(String str)

    public static List<CodenameWord> createNonRandomCodenameList(Path databaseFile) throws IOException {
        //parse database for 25 words
        List <String> words = parseDatabaseFileNonRandom(databaseFile);

        //turn the 25 words into 25 CodenameWord Objects
        return generateCodenameWordList(words);
    }//END OF createRandomCodenameList()
    
    private static List<String> parseDatabaseFileNonRandom(Path databaseFile) throws IOException {
        //====================
        //--PARSING DATABASE--
        //====================

        List <String> lines = new ArrayList<>(Files.readAllLines(databaseFile));
        return lines.subList(0,25);
    }//END OF parseDatabaseFile()

    //====================
    //--------TEST--------
    //====================
//    public static void main(String[] args) {
//        List<CodenameWord> codenameWordList = createRandomCodenameList();
//
//        //====================
//        //--------TEST--------
//        //====================
////        //print out the codenameWordList
////        System.out.println("CodeName Word List:");
////        int count = 1;
////        for (CodenameWord codeNameWord: codenameWordList) {
////            System.out.println("Word " + count + ": " + codeNameWord);
////            count++;
////        }
//
//
//        //====================
//        //--------TEST--------
//        //====================
////        //print ouf the list of Random Card Types for the key card
////        CardValue[] values = createRandomKeycard();
////        for (CardValue value : values) {
////            System.out.println(value.toString());
////        }
//
//
//        //====================
//        //--------TEST--------
//        //====================
////        //print out the card list
////        System.out.println("Card List:");
////
////        List<Card> cardList = generate25Cards();
////
////        int count = 1;
////        for (Card card: cardList) {
////            System.out.println("Card " + count + ": " + card.getCodename() + ", " + card.getTypeOfCard() + ", " + card.isGuessed());
////            count++;
////        }
//
//    }//END OF main(String[] args)

}//END OF Card CLASS