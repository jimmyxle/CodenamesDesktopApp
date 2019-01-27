package ca.concordia.encs.comp354.model;


import com.sun.tools.javac.jvm.Code;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Represents a card - one of the twenty five placed on the board for the game. Words & an associated word list are
 * parsed from a database text file.
 * @author Zachary Hynes
 */
public class Cards {
    //============================
    //---------VARIABLES---------
    //============================
    private String codename;
    private List<CodenameWord.AssociatedWord> associatedWords;
    private CardValue typeOfCard;
    private boolean isGuessed;


    //============================
    //--------CONSTRUCTORS--------
    //============================
    private Cards(String codename, List<CodenameWord.AssociatedWord> associatedWords, CardValue typeOfCard) {
        this.codename = codename;
        this.associatedWords = associatedWords;
        this.typeOfCard = typeOfCard;
        this.isGuessed = false;
    }


    //============================
    //----------METHODS----------
    //============================
    /**
     * Returns a list of 25 Codename Cards to be used to populate the board
     * @return a list containing 25 Codename Cards
     */
    public static List<Cards> generate25Cards() {
        List<CodenameWord> wordList = generateRandomCodenameList();
        List<Cards> cardList = new ArrayList<>();

        CardValue[] cardValues = generateKeyCard();

        int i = 0;

        for (CodenameWord word : wordList) {
            Cards newCard = new Cards(word.getClueWord(), word.getAssociatedWords(), cardValues[i]);
            cardList.add(newCard);
            i++;
        }

        return cardList;

    }//END OF generate25Cards()

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public List<CodenameWord.AssociatedWord> getAssociatedWords() {
        return associatedWords;
    }

    public void setAssociatedWords(List<CodenameWord.AssociatedWord> associatedWords) {
        this.associatedWords = associatedWords;
    }

    public CardValue getTypeOfCard() {
        return typeOfCard;
    }

    public void setTypeOfCard(CardValue typeOfCard) {
        this.typeOfCard = typeOfCard;
    }

    public boolean isGuessed() {
        return isGuessed;
    }

    public void setGuessed(boolean guessed) {
        isGuessed = guessed;
    }

    @Override
    public String toString() {
        return "Cards{" +
                "codename='" + codename + '\'' +
                ", associatedWords=" + associatedWords +
                ", typeOfCard=" + typeOfCard +
                ", isGuessed=" + isGuessed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cards cards = (Cards) o;
        return isGuessed == cards.isGuessed &&
                Objects.equals(codename, cards.codename) &&
                Objects.equals(associatedWords, cards.associatedWords) &&
                typeOfCard == cards.typeOfCard;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codename, associatedWords, typeOfCard, isGuessed);
    }

    /**
     * Returns a list of 25 Codename Words to be used to create cards
     * @return a list containing 25 Codename Words
     */
    private static List<CodenameWord> generateRandomCodenameList() {
        //generate 25 random numbers between 0 and 400
//        generateRandomNumber();

        //parse database for 25 words
        String[] words = parseDatabaseFile();

        //turn the 25 words into 25 CodenameWord Objects
        List<CodenameWord> codenameWordList = generateCodenameWordList(words);

        return codenameWordList;
    }//END OF generateRandomCodenameList()

    //TODO: for next iteration
    private static int[] generateRandomNumber() {
        int[] random25 = new int[25];

        //populate the random array
        for (int i = 0; i < 25; i++) {
            random25[i] = (int) (Math.random() * 400);

            System.out.println(random25[i]);
        }

        //verify if there are doubles
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25-i; j++) {
                if (random25[i] == random25[j]) {
                    //generate a new array of random numbers
                }
            }
        }

        return random25;
    }//END OF generateRandomNumber()

    private static String[] parseDatabaseFile() {
        //============================
        //------TEST FILE ACCESS------
        //============================
//        System.out.println("Testing access to files...");


        //====================
        //-----IO STREAMS-----
        //====================
        //Input
        Scanner inputFromDatabase = null;
        String inputPath = "./src/ca/concordia/encs/comp354/model/TextFiles/database.txt";

        try {
            //create new input stream connected to input file.
            inputFromDatabase = new Scanner(new FileReader(inputPath));
        } catch (FileNotFoundException e) {
            System.out.println("Could not open input file for reading. Please check if file exists! Program will terminate after closing any opened files.");
            System.exit(0);
        } finally {
            //Assure a closed input in the event of an exception being thrown.
            assert inputFromDatabase != null;
            inputFromDatabase.close();
//            System.out.println("File opened successfully!");
        }


        //====================
        //--PARSING DATABASE--
        //====================
        String[] words = new String[25];

        try {
            //create new input stream connected to input file.
            inputFromDatabase = new Scanner(new FileReader(inputPath));

            //use the 25 parsed lines to create 25 AssociatedWords Lists
            for (int i = 0; i < 25; i++) {
                words[i] = inputFromDatabase.nextLine();
            }

        } catch (FileNotFoundException e) {
//            System.out.println("Could not open input file for reading. Please check if file exists! Program will terminate after closing any opened files.");
            System.exit(0);
        } finally {
            //Assure a closed input in the event of an exception being thrown.
            inputFromDatabase.close();
//            System.out.println("File parsed successfully!");
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


        //create a new Codename Object and attach the AssociatedWordList
        CodenameWord newCodenameObject = new CodenameWord(codeName, associatedWordList);

        return newCodenameObject;
    }//END OF parseCodenameObject(String str)

    //TODO: verify with the team if this is the best place to generate the baords' key card
    private static CardValue[] generateKeyCard() {
        CardValue[] cardValues = new CardValue[25];

        int reds = 9;
        int blues = 8;
        int neutral = 7;
        int assassin = 1;

        int cardsLeft = 0;
        int random;

        while (cardsLeft < 25) {
            random = (int) (Math.random() * 4 + 1);

            if (random == 1 && reds != 0) {
                cardValues[cardsLeft] = CardValue.RED;
                reds--;
                cardsLeft++;
            } else if (random == 2 && blues != 0) {
                cardValues[cardsLeft] = CardValue.BLUE;
                blues--;
                cardsLeft++;
            } else if (random == 3 && neutral != 0) {
                cardValues[cardsLeft] = CardValue.NEUTRAL;
                neutral--;
                cardsLeft++;
            } else if (random == 4 && assassin != 0) {
                cardValues[cardsLeft] = CardValue.ASSASSIN;
                assassin--;
                cardsLeft++;
            }
        }

        return cardValues;
    }

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
////        System.out.println("Cards List:");
////
////        List<Cards> cardList = generate25Cards();
////
////        int count = 1;
////        for (Cards card: cardList) {
////            System.out.println("Card " + count + ": " + card.getCodename() + ", " + card.getTypeOfCard() + ", " + card.isGuessed());
////            count++;
////        }
//
//    }//END OF main(String[] args)

}//END OF Cards CLASS