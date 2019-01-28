package ca.concordia.encs.comp354.model;

import java.util.List;
import java.io.IOException;
import java.lang.IllegalArgumentException;

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
//    public Board() {
////        try {
//            List<Card> cardList = Card.generate25Cards();
//
//            if (cardList.size() < 25) {
//                throw new IllegalArgumentException("Not enough cards to populate the board.");
//            }
//
//            for (int i = 0; i < WIDTH; i++) {
//                for (int j = 0; j < LENGTH; j++) {
//                    this.board[i][j] = cardList.get((i*WIDTH) + j);
//                }
//            }
////        } catch (IOException e) {
////            //TODO: Figure out how to safely throw the exception to be handled higher up the call stack
////            // or handle it here.
////            System.out.println("Exception.... to be handled with at a future date!");
////        }
//    }

    //TODO: maybe it's best practice to set the default constructor to a null board, and create a init() function?
    // --well, this can get us something working for iteration 1 at least.
    public Board(List<Card> cardList) throws IllegalArgumentException {

        if (cardList.size() < 25) {
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
     * Returns the word at the given coordinates.
     * @param x horizontal coordinate; must be less than {@link #getWidth()}
     * @param y vertical coordinate; must be less than {@link #getLength()}
     * @return the word on the card at the given coordinates
     */
    public String getWord(int x, int y) {
        return board[x][y].getCodename();
    }
    
    /**
     * Returns value of the card at the given coordinates.
     * @param x horizontal coordinate; must be less than {@link #getWidth()}
     * @param y vertical coordinate; must be less than {@link #getLength()}
     * @return a value representing the team colour at the given coordinates on the board
     */
    public CardValue getValue(int x, int y) {
        return board[x][y].getTypeOfCard();
    }

    /**
     * Returns associated word list of the card at the given coordinates.
     * @param x horizontal coordinate; must be less than {@link #getWidth()}
     * @param y vertical coordinate; must be less than {@link #getLength()}
     * @return the associated word list of the word at the given coordinates on the board
     */
    public List<CodenameWord.AssociatedWord> getAssociatedWordList(int x, int y) {
        return board[x][y].getAssociatedWords();
    }

    /**
     * Returns a specific associated word of the card at the given coordinates.
     * @param x horizontal coordinate; must be less than {@link #getWidth()}
     * @param y vertical coordinate; must be less than {@link #getLength()}
     * //TODO: if we change the number of words in the associated word list / database, this value should
     *          be linked to the length of the associated word list - potential update issue -
     * @param i index of the associated word; must be less than getAssociatedWordList(int, int).length
     * @return the associated word list of the word at the given coordinates on the board
     */
    public String getAssociatedWord (int x, int y, int i) {
        List<CodenameWord.AssociatedWord> list = board[x][y].getAssociatedWords();

        return list.get(i).getAssociatedWord();
    }

    /**
     * Returns a specific associated word weight of the card at the given coordinates.
     * @param x horizontal coordinate; must be less than {@link #getWidth()}
     * @param y vertical coordinate; must be less than {@link #getLength()}
     * //TODO: if we change the number of words in the associated word list / database, this value should
     *          be linked to the length of the associated word list - potential update issue -
     * @param i index of the associated word; must be less than getAssociatedWordList(int, int).length
     * @return the associated word list of the word at the given coordinates on the board
     */
    public int getAssociatedWordWeight (int x, int y, int i) {
        List<CodenameWord.AssociatedWord> list = board[x][y].getAssociatedWords();

        return list.get(i).getWeight();
    }

    /**
     * Returns a specific associated word object of the card at the given coordinates.
     * @param x horizontal coordinate; must be less than {@link #getWidth()}
     * @param y vertical coordinate; must be less than {@link #getLength()}
     * //TODO: if we change the number of words in the associated word list / database, this value should
     *          be linked to the length of the associated word list - potential update issue -
     * @param i index of the associated word; must be less than {@link #getAssociatedWordList(int, int).length}
     * @return the associated word list of the word at the given coordinates on the board
     */
    public CodenameWord.AssociatedWord getAssociatedWordObject(int x, int y, int i) {
        List<CodenameWord.AssociatedWord> list = board[x][y].getAssociatedWords();

        return list.get(i);
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
                str.append(this.board[i][j].getCodename() + "-" + this.board[i][j].getTypeOfCard() + "\t\t");
            }
            str.append("\n");
        }

        return str.toString();
    }

    //====================
    //--------TEST--------
    //====================
    public static void main(String[] args) throws IOException {
            List<Card> cardList = Card.generate25Cards();
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

            System.out.println("Word at [" + x + "][" + y + "]: " + gameBoard.getWord(x,y) + "-" + gameBoard.getValue(x,y));
            System.out.println("\nAssociated Word List for \"" + gameBoard.getWord(x,y) + "\": " + gameBoard.getAssociatedWordList(x,y));
            System.out.println("\nAssociated Word i's Object for \"" + gameBoard.getWord(x,y) + "\": " + gameBoard.getAssociatedWordObject(x,y,i));
            System.out.println("\nAssociated Word i for \"" + gameBoard.getWord(x,y) + "\": " + gameBoard.getAssociatedWord(x,y,i));
            System.out.println("\nAssociated Word i's Weight for \"" + gameBoard.getWord(x,y) + "\": " + gameBoard.getAssociatedWordWeight(x,y,i));
    }//END OF main(String[] args)

}//END OF Board class
