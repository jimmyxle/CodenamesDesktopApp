package ca.concordia.encs.comp354.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.ReadOnlyGameState;

abstract class AbstractPlayerStrategy {
    
    private List<Coordinates> guesses;
    private Board             lastBoard;
    
    protected List<Coordinates> beginTurn(Player owner, ReadOnlyGameState state) {
        Board board = state.boardProperty().get();
        Set<Coordinates> marked = state.getChosenCards();
        
        // board has changed; reset guesses
        if (board!=lastBoard) {
            rebuildGuesses(owner, board);
            lastBoard = board;
        }
        
        // clear invalid guesses (have to do this each turn, since other team may have guessed one of our cards)
        for (int i=guesses.size(); i>=0; i--) {
            if (marked.contains(guesses.get(i))) {
                guesses.remove(i);
            }
        }
        
        return guesses;
    }

    private void rebuildGuesses(Player owner, Board board) {
        guesses = new ArrayList<>();
        for (int x=0; x<board.getWidth(); x++) {
            for (int y=0; y<board.getLength(); y++) {
                if (isValidGuess(owner, board, x, y)) {
                    guesses.add(new Coordinates(x, y));
                }
            }
        }
    }
    
    protected abstract boolean isValidGuess(Player owner, Board board, int x, int y);
}
