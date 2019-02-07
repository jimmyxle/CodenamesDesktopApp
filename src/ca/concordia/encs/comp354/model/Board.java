package ca.concordia.encs.comp354.model;

import java.util.List;

import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.nio.file.Paths;

/**
 * Represents a game board configuration. Since the configuration does not change for the duration of the game, this 
 * class should be immutable (which is the assumption the view code makes). A new configuration means a new Board.
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
    //TODO: maybe it's best practice to set the default constructor to a null board, and create a init() function?
    // --well, this can get us something working for iteration 1 at least.
    public Board(List<Card> cardList) throws IllegalArgumentException {

        if (cardList.size() < WIDTH*LENGTH) {
            throw new IllegalArgumentException("Not enough cards to populate the board.");
        }

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < LENGTH; j++) {
                this.board[i][j] = cardList.get((i*WIDTH) + j);
            }
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
                str.append(this.board[i][j].getCodename() + "-" + this.board[i][j].getValue() + "\t\t");
            }
            str.append("\n");
        }

        return str.toString();
    }

    //====================
    //--------TEST--------
    //====================
    public static void main(String[] args) throws IOException {
        List<Card> cardList = Card.generate25Cards(Paths.get("res/words.txt"));
        Board gameBoard = new Board(cardList);
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
