package ca.concordia.encs.comp354.model;


import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.nio.file.Paths;

/**
 * Represents a game board configuration. Since the configuration does not change for the duration of the game, this 
 * class should be immutable (which is the assumption the view code makes). A new configuration means a new Board.
 *
 * Strategy Implemented: a simple factory method for creating a board.
 *
 * @author Nikita Leonidov
 * @author Zachary Hynes
 */
public class Board {
    //============================
    //---------VARIABLES---------
    //============================
    private final int LENGTH = 5;
    private final int WIDTH = 5;

    private Card[][] board = new Card[LENGTH][WIDTH];


    //============================
    //--------CONSTRUCTORS--------
    //============================
    private Board(List<CodenameWord> codenameWords, List<Keycard> keyCards) throws IllegalArgumentException {
        List<Card> cards = new ArrayList<>();

        for (int y = 0; y < WIDTH; y++) {
            for (int x = 0; x < LENGTH; x++) {
                int iteration = (y*WIDTH) + x;
                String codeName = codenameWords.get(iteration).getClueWord();
                List<CodenameWord.AssociatedWord> associatedWords = codenameWords.get(iteration).getAssociatedWords();
                int random = (int) (Math.random() * Keycard.NUMBER_OF_KEYCARDS);
                Keycard randomKeycard = keyCards.get(random);

                cards.add(Card.generateCard(codeName, associatedWords, randomKeycard.getCardValue(x, y)));
            }
        }

        if (cards.size() < 25) {
            throw new IllegalArgumentException("Not enough cards to populate the board.");
        }

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < LENGTH; j++) {
                this.board[i][j] = cards.get((i*WIDTH) + j);
            }
        }

        for (Keycard keycard : keyCards) {
            Keycard.outputKeycardToDatabase(keycard);
        }
    }


    //============================
    //----------METHODS----------
    //============================
    public Card[][] getBoard() {
        return board;
    }

    //TODO: maybe in the next iteration we can make it so that each card can be placed individually,
    // or that the user can rearrange the tiles of the board - instead of generating an entirely new
    // randomized board?
    public void setBoard(Card[][] board) {
        this.board = board;
    }

    /**
     * Returns the card at the given coordinates.
     * @param coords  coordinates; x must be less than {@link #getWidth()}, y must be less than {@link #getLength()}
     * @return the card at the given coordinates
     */
    public Card getCard(Coordinates coords) {
        return getCard(coords.getX(), coords.getY());
    }

    /**
     * Returns the card at the given coordinates.
     * @param x horizontal coordinate; must be less than {@link #getWidth()}
     * @param y vertical coordinate; must be less than {@link #getLength()}
     * @return the card at the given coordinates
     */
    public Card getCard(int x, int y) {
        return board[x][y];
    }

    /**
     * Returns a board that is populated by the words from the codenameWords list, which have their teams assigned by the
     * value of the keycard selected from the keycards list.
     * @param codenameWords a list of 25 codenameWords
     * @param keyCards a list of keycards
     * @return a board populated with the codenameWords and their teams set by the keycard
     */
    public static Board createBoard(List<CodenameWord> codenameWords, List<Keycard> keyCards) {
        return new Board(codenameWords, keyCards);
    }

    /**

     * @return the width of the board in tiles
     */
    public int getWidth() {
        return this.WIDTH;
    }
    
    /**
     * @return the length of the board in tiles
     */
    public int getLength() {
        return this.LENGTH;
    }

    @Override
    public String toString() {

        StringBuilder str = new StringBuilder();

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < LENGTH; j++) {
                str.append(this.board[i][j].getCodename()).append("-").append(this.board[i][j].getValue()).append("\t\t");
            }
            str.append("\n");
        }

        return str.toString();
    }

    //====================
    //--------TEST--------
    //====================
    public static void main(String[] args) throws IOException {
//        List<Card> cardList = Card.generate25Cards(Paths.get("res/words.txt"));

        List<CodenameWord> codenameWords = Card.generateRandomCodenameList(Paths.get("res/words.txt"));
        List<Keycard> keycards = Keycard.generateKeyCards(Keycard.NUMBER_OF_KEYCARDS);


        Board gameBoard = Board.createBoard(codenameWords, keycards);
        //====================
        //--------TEST--------
        //====================
        //print out the card list
        System.out.println("Printing out the game board: ");
        System.out.println(gameBoard.toString());

        int x = 3;
        int y = 4;
        int i = 9;

        Card card = gameBoard.getCard(x, y);
        System.out.println("Word at [" + x + "][" + y + "]: " + card.getCodename() + "-" + card.getValue());
        System.out.println("\nAssociated Word List for \"" + card.getCodename() + "\": " + card.getAssociatedWords());
        System.out.println("\nAssociated Word i's Object for \"" + card.getCodename() + "\": " + card.getAssociatedWords().get(i));
        System.out.println("\nAssociated Word i for \"" + card.getCodename() + "\": " + card.getAssociatedWords().get(i).getWord());
        System.out.println("\nAssociated Word i's Weight for \"" + card.getCodename() + "\": " + card.getAssociatedWords().get(i).getWeight());        

            
            
    }//END OF main(String[] args)

}//END OF Board class
