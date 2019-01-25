//============================
//----------PACKAGE----------
//============================
package ca.concordia.encs.comp354.model;


//============================
//----------IMPORTS----------
//============================
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


//============================
//------STATIC VARIABLES------
//============================


//============================
//--------CARDS CLASS--------
//============================
public class Cards {
    //============================
    //---------VARIABLES---------
    //============================
    //ENUM for RED, BLUE, NEUTRAL, ASSASSIN

    //============================
    //--------CONSTRUCTORS--------
    //============================


    //============================
    //----------METHODS----------
    //============================


    //====================
    //------MUTATORS------
    //====================


    //====================
    //-----ACCESSORS-----
    //====================


    //====================
    //-----TO STRING-----
    //====================


    //====================
    //-------EQUALS-------
    //====================


    //====================
    //-------OTHER-------
    //====================
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
//    private static int[] generateRandomNumber() {
//        int[] random25 = new int[25];
//
//        //populate the random array
//        for (int i = 0; i < 25; i++) {
//            random25[i] = (int) (Math.random() * 400);
//
//            System.out.println(random25[i]);
//        }
//
//        //verify if there are doubles
//        for (int i = 0; i < 25; i++) {
//            for (int j = 0; j < 25-i; j++) {
//                if (random25[i] == random25[j]) {
//                    //generate a new array of random numbers
//                }
//            }
//        }
//
//        return random25;
//    }//END OF generateRandomNumber()

    private static String[] parseDatabaseFile() {
        //============================
        //------TEST FILE ACCESS------
        //============================
        System.out.println("Testing access to files...");


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
            System.out.println("File opened successfully!");
        }

        //====================
        //-----VARIABLES-----
        //====================
        String[] words = new String[25];

        //====================
        //--PARSING DATABASE--
        //====================
        try {
            //create new input stream connected to input file.
            inputFromDatabase = new Scanner(new FileReader(inputPath));

            //use the 25 parsed lines to create 25 AssociatedWords Lists
            for (int i = 0; i < 25; i++) {
                words[i] = inputFromDatabase.nextLine();
            }

        } catch (FileNotFoundException e) {
            System.out.println("Could not open input file for reading. Please check if file exists! Program will terminate after closing any opened files.");
            System.exit(0);
        } finally {
            //Assure a closed input in the event of an exception being thrown.
            inputFromDatabase.close();
            System.out.println("File parsed successfully!");
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


    //====================
    //--------TEST--------
    //====================
    //============================
    //------------MAIN------------
    //============================
    public static void main(String[] args) {
        List<CodenameWord> codenameWordList = generateRandomCodenameList();

        //====================
        //--------TEST--------
        //====================
        //print out the codenameWordList
        System.out.println("CodeName Word List:");
        int count = 1;
        for (CodenameWord codeNameWord: codenameWordList) {
            System.out.println("Word " + count + ": " + codeNameWord);
            count++;
        }

    }//END OF main(String[] args)

}//END OF Cards CLASS