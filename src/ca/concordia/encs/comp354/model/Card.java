package ca.concordia.encs.comp354.model;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

/**
 * Represents a card - one of the twenty five placed on the board for the game. Words & an associated word list are
 * parsed from a database text file.
 * @author Zachary Hynes
 */
public class Card {
    //============================
    //---------VARIABLES---------
    //============================
    final private String codename;
    final private List<CodenameWord.AssociatedWord> associatedWords;
    final private CardValue typeOfCard;


    //============================
    //--------CONSTRUCTORS--------
    //============================
    private Card(String codename, List<CodenameWord.AssociatedWord> associatedWords, CardValue typeOfCard) {
        this.codename = codename;
        this.associatedWords = Collections.unmodifiableList(new ArrayList<>(associatedWords));
        this.typeOfCard = typeOfCard;
    }


    //============================
    //----------METHODS----------
    //============================
    /**
     * Returns a list of 25 Codename Cards to be used to populate the board. Teams are assigned based off
     * of a random KeyCard picked from a list of 10 randomly created KeyCards.
     * @param databaseFile path to the database file from which to read codenames
     * @return a list containing 25 Codename Card
     */
    public static List<Card> generate25Cards(Path databaseFile) throws IOException {
        List<CodenameWord> wordList = generateRandomCodenameList(databaseFile);
        List<Card> cardList = new ArrayList<>();

        List<Keycard> keyCards = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Keycard keyCard = Keycard.generateKeyCard();
            keyCards.add(keyCard);
        }

        int random = (int) (Math.random() * 10);
        Keycard randomKeycard = keyCards.get(random);


        int i = 0;
        for (CodenameWord word : wordList) {
            Card newCard = new Card(word.getClueWord(), word.getAssociatedWords(), randomKeycard.getCardValue(i));
            cardList.add(newCard);
            i++;
        }

        //make the cardList unmodifiable
        cardList = Collections.unmodifiableList(cardList);

        return cardList;

    }//END OF generate25Cards()

    public String getCodename() {
        return codename;
    }

    public List<CodenameWord.AssociatedWord> getAssociatedWords() {
        return associatedWords;
    }

    public CardValue getValue() {
        return typeOfCard;
    }

    @Override
    public String toString() {
        return "Card{" +
                "codename='" + codename + '\'' +
                ", associatedWords=" + associatedWords +
                ", typeOfCard=" + typeOfCard +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(codename, card.codename) &&
                Objects.equals(associatedWords, card.associatedWords) &&
                typeOfCard == card.typeOfCard;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codename, associatedWords, typeOfCard);
    }

    /**
     * Returns a list of 25 Codename Words to be used to create cards
     * @return a list containing 25 Codename Words
     */
    private static List<CodenameWord> generateRandomCodenameList(Path databaseFile) throws IOException {
        //generate 25 random numbers between 0 and 400
//        generateRandomNumber();

        //parse database for 25 words
        String[] words = parseDatabaseFile(databaseFile);

        //turn the 25 words into 25 CodenameWord Objects
        return generateCodenameWordList(words);
    }//END OF generateRandomCodenameList()

    private static String[] parseDatabaseFile(Path databaseFile) throws IOException {
        //====================
        //--PARSING DATABASE--
        //====================
        String[] words = new String[25];

        try (Scanner inputFromDatabase = new Scanner(Files.newBufferedReader(databaseFile))) {
            //use the 25 parsed lines to create 25 AssociatedWords Lists
            for (int i = 0; i < 25; i++) {
                words[i] = inputFromDatabase.nextLine();
            }
        } catch (NoSuchElementException e) {
            throw new IOException("database file must have at least 25 elements", e);
        }

        //====================
        //--------TEST--------
        //====================
//        int i = 0;
//        for (String w : words) {
//            System.out.println("Word: " + i + " - " + w);
//            i++;
//        }

        return words;
    }//END OF parseDatabaseFile()

    private static List<CodenameWord> generateCodenameWordList(String[] words) {
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


    //====================
    //--------TEST--------
    //====================
//    public static void main(String[] args) {
//        List<CodenameWord> codenameWordList = generateRandomCodenameList();
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
////        CardValue[] values = generateKeyCard();
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