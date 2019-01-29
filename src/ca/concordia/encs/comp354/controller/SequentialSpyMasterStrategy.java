package ca.concordia.encs.comp354.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Card;
import ca.concordia.encs.comp354.model.CodenameWord.AssociatedWord;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.ReadOnlyGameState;

public class SequentialSpyMasterStrategy implements SpyMaster.Strategy {
    
    private final Random random = new Random();
    
    private List<Coordinates> guesses;
    private Board             lastBoard;

    @Override
    public String giveClue(SpyMaster owner, ReadOnlyGameState state) {
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
        
        return guesses.isEmpty()? null : getAssociatedWord(board, guesses.remove(0));
    }

    private void rebuildGuesses(SpyMaster owner, Board board) {
        guesses = new ArrayList<>();
        for (int x=0; x<board.getWidth(); x++) {
            for (int y=0; y<board.getLength(); y++) {
                // the card is ours, so add coordinates to list of guesses
                Card c = board.getCard(x, y);
                if (c.getValue()==owner.getTeam().getValue()) {
                    guesses.add(new Coordinates(x, y));
                }
            }
        }
    }

    private String getAssociatedWord(Board board, Coordinates coords) {
        Card card = board.getCard(coords);
        List<AssociatedWord> words = card.getAssociatedWords();
        return words.get(random.nextInt(words.size())).getWord();
    }
}
