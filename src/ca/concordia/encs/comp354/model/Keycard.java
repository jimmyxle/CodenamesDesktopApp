package ca.concordia.encs.comp354.model;

import java.io.*;
import java.util.*;



/**
 * Represents a keycard that is used to determine the teams (red/blue/neutral/assassin)
 * of the 25 cards placed on the board.
 *
 * Strategy Implemented: a simple factory method for creating a keycard.
 *
 * @author Zachary Hynes
 */
public class Keycard {
    //============================
    //---------VARIABLES---------
    //============================
    private final int LENGTH = 5;
    private final int WIDTH = 5;
    private final CardValue[][] keycard = new CardValue[LENGTH][WIDTH];
    private static int numberOfKeycards = 0;
    public static final int NUMBER_OF_KEYCARDS = 25;

    //============================
    //--------CONSTRUCTORS--------
    //============================
    private Keycard(List<CardValue> keyCard) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < LENGTH; j++) {
                int iteration = (i*WIDTH) + j;
                this.keycard[i][j] = keyCard.get(iteration);
            }
        }
    }

    //============================
    //----------METHODS----------
    //============================
    /**
     * Returns the card at the given coordinates.
     * @param x horizontal coordinate; must be less than {@link #getWIDTH()}
     * @param y vertical coordinate; must be less than {@link #getLENGTH()}
     * @return the card at the given coordinates
     */
    CardValue getCardValue(int x, int y) {
        return keycard[x][y];
    }

    /**
     * Factory Method randomly generates a Keycard containing 25 CardValues (RED/BLUE/NEUTRAL/ASSASSIN)
     * @return a Keycard containing 25 CardValues (RED/BLUE/NEUTRAL/ASSASSIN)
     */
    private static Keycard generateRandomKeycard() {
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

        return new Keycard(cardValues);
    }//END OF generateRandomKeycard()

    /**
     * Factory Method generates a Keycard containing 25 CardValues (RED/BLUE/NEUTRAL/ASSASSIN)
     * @param cardValues a list of cardValues to set the team of the cards
     * @return a Keycard containing 25 CardValues (RED/BLUE/NEUTRAL/ASSASSIN)
     */
    private static Keycard generateKeycard(List<CardValue> cardValues) {
        return new Keycard(cardValues);
    }


    public static List<Keycard> generateKeyCards(int numberOfKeycards) {
        List<Keycard> keycards = new ArrayList<>();

        for (int i = 0; i < numberOfKeycards; i++) {
            keycards.add(generateRandomKeycard());
        }

        return keycards;
    }

    @Override
    public String toString() {
        StringBuilder keyCardPrint = new StringBuilder();

        keyCardPrint.append("Keycard ").append(numberOfKeycards).append(": \n\t\t\t");

        for (int y = 0; y < WIDTH; y++) {
            for (int x = 0; x < LENGTH; x++) {
                //noinspection RedundantStringOperation
                keyCardPrint.append(keycard[x][y].toString().substring(0,1)).append(" ");

                if (x == LENGTH-1) {
                    keyCardPrint.append("\n\t\t\t");
                }
            }
        }



        keyCardPrint.append("\n");

        return keyCardPrint.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Keycard keycard1 = (Keycard) o;
        return Arrays.equals(keycard, keycard1.keycard);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(LENGTH, WIDTH);
        result = 31 * result + Arrays.hashCode(keycard);
        return result;
    }

    private int getLENGTH() {
        return LENGTH;
    }

    private int getWIDTH() {
        return WIDTH;
    }

    static void outputKeycardToDatabase(Keycard keyCardToPrint) {
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
