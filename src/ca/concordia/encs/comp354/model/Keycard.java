package ca.concordia.encs.comp354.model;

import java.io.*;
import java.util.*;

/**
 * Represents a keycard that is used to determine the teams (red/blue/neutral/assassin)
 * of the 25 cards placed on the board.
 * @author Zachary Hynes
 */
public class Keycard {
    //============================
    //---------VARIABLES---------
    //============================
    private static int numberOfKeycards = 0;
    private final List<CardValue> keyCard;

    //============================
    //--------CONSTRUCTORS--------
    //============================
    private Keycard(List<CardValue> keyCard) {
        this.keyCard = keyCard;
    }

    //============================
    //----------METHODS----------
    //============================
    /**
     * Gets the CardValue (RED/BLUE/NEUTRAL/ASSASSIN) from the KeyCard at index i
     * @param i index of the cardValue you'd like to retrieve from the keyCard {must be ≤ 24}
     * @return the CardValue at index i
     */
    CardValue getCardValue(int i) {
        return keyCard.get(i);
    }

    /**
     * Generates a Keycard containing 25 CardValues (RED/BLUE/NEUTRAL/ASSASSIN)
     * @return a Keycard containing 25 CardValues (RED/BLUE/NEUTRAL/ASSASSIN)
     */
    static Keycard generateKeyCard() {
        List<CardValue> cardValues = new ArrayList<>();

        //TODO: We assume the starting team is red - this needs to be changed in further iterations.
        int startingTeam = 9;
        int nonStartingTeam = 8;
        int neutral = 7;
        int assassin = 1;


        for (int i = 0; i < startingTeam; i++) {
            cardValues.add(CardValue.RED);
        }

        for (int i = 0; i < nonStartingTeam; i++) {
            cardValues.add(CardValue.BLUE);
        }

        for (int i = 0; i < neutral; i++) {
            cardValues.add(CardValue.NEUTRAL);
        }

        for (int i = 0; i < assassin; i++) {
            cardValues.add(CardValue.ASSASSIN);
        }

        //Shuffle the list
        Collections.shuffle(cardValues);

        //make list unmodifiable
        cardValues = Collections.unmodifiableList(cardValues);

        //create a new keyCard object
        Keycard newKeycard = new Keycard(cardValues);

        //output the keyCard object to the database
        outputKeycardToDatabase(newKeycard);

        return newKeycard;
    }//END OF generateKeyCard()

    @Override
    public String toString() {
        StringBuilder keyCardPrint = new StringBuilder();

        keyCardPrint.append("Keycard ").append(numberOfKeycards).append(": \n\t\t\t");

        int i = 1;
        for (CardValue cardValue : this.getKeyCard()) {

            //noinspection RedundantStringOperation
            keyCardPrint.append(cardValue.toString().substring(0,1)).append(" ");

            if (i % 5 == 0) {
                keyCardPrint.append("\n\t\t\t");
            }

            i++;
        }

        keyCardPrint.append("\n");

        return keyCardPrint.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Keycard keycard = (Keycard) o;
        return Objects.equals(keyCard, keycard.keyCard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyCard);
    }

    private List<CardValue> getKeyCard() {
        return keyCard;
    }

    private static void outputKeycardToDatabase(Keycard keyCardToPrint) {
        File file = new File("./res/lastRunGamesKeyCards.txt");
        FileWriter fr = null;
        BufferedWriter br = null;
        PrintWriter pr = null;

        try {
            //clear the file at the start of every game
            if (numberOfKeycards == 0) {
                PrintWriter writer = new PrintWriter(file);
                writer.print("");
                writer.close();
            }

            // to append to file, you need to initialize FileWriter using below constructor
            fr = new FileWriter(file, true);
            br = new BufferedWriter(fr);
            pr = new PrintWriter(br);

            pr.println(keyCardToPrint.toString());
            numberOfKeycards ++;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pr != null) {
                    pr.close();
                }
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}//END OF Keycard CLASS
