package ca.concordia.encs.comp354.model;

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
    public Board(List<CodenameWord> words, Keycard keycard) {
        if (keycard.getWidth()!=getWidth() || keycard.getLength()!=getLength()) {
            throw new IllegalArgumentException("keycard size mismatch");
        }
        
        if (words.size() < getWidth()*getLength()) {
            throw new IllegalArgumentException("not enough cards to populate the board");
        }
        
        for (int x=0; x<getWidth(); x++) {
            for (int y = 0; y < getLength(); y++) {
                CodenameWord w = words.get((y*getWidth()) + x);
                CardValue v = keycard.getCardValue(x, y);
                this.board[x][y] = new Card(w, v);
            }
        }
    }


    //============================
    //----------METHODS----------
    //============================
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
        return WIDTH;
    }
    
    /**
     * @return the length of the board in tiles
     */
    public int getLength() {
        return LENGTH;
    }

    @Override
    public String toString() {

        StringBuilder str = new StringBuilder();

        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getLength(); j++) {
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
        Keycard keycard = Keycard.generateRandomKeycard();

        
        Board gameBoard = new Board(codenameWords, keycard);
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
