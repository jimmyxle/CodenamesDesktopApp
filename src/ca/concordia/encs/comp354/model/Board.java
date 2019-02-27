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

    //this method is to get all clue word for the blue team. We need that to use it in RandomSpyMasterStrategy
/*    public List<String> getBlueClueword(List<CodenameWord> words, Keycard keycard){
        List<String> allBlueClueword = new ArrayList<String>();
        for(int i = 0; i < keycard.getLength(); i++){
            for(int j = 0; j< keycard.getWidth();j++){
                if(keycard.getCardValue(i,j)==CardValue.BLUE){
                    allBlueClueword.add(getCard(i,j).getCodename());
                }
            }
        }
        return allBlueClueword;
    }*/

    //====================
    //--------TEST--------
    //====================
//    public static void main(String[] args) throws IOException {
////        List<Card> cardList = Card.generate25Cards(Paths.get("res/words.txt"));
//
//        List<CodenameWord> codenameWords = Card.createRandomCodenameList(Paths.get("res/words.txt"));
//        Keycard keycard = Keycard.createRandomKeycard();
//
//
//        Board gameBoard = new Board(codenameWords, keycard);
//        //====================
//        //--------TEST--------
//        //====================
//        //print out the card list
//        System.out.println("Printing out the game board: ");
//        System.out.println(gameBoard.toString());
//
//        int x = 3;
//        int y = 4;
//        int i = 9;
//
//        Card card = gameBoard.getCard(x, y);
//        System.out.println("Word at [" + x + "][" + y + "]: " + card.getCodename() + "-" + card.getValue());
//        System.out.println("\nAssociated Word List for \"" + card.getCodename() + "\": " + card.getAssociatedWords());
//        System.out.println("\nAssociated Word i's Object for \"" + card.getCodename() + "\": " + card.getAssociatedWords().get(i));
//        System.out.println("\nAssociated Word i for \"" + card.getCodename() + "\": " + card.getAssociatedWords().get(i).getWord());
//        System.out.println("\nAssociated Word i's Weight for \"" + card.getCodename() + "\": " + card.getAssociatedWords().get(i).getWeight());
//
//
//
//    }//END OF main(String[] args)

}//END OF Board class
