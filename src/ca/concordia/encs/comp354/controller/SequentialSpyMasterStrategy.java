package ca.concordia.encs.comp354.controller;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Card;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.ReadOnlyGameState;

public class SequentialSpyMasterStrategy implements SpyMaster.Strategy {
    
    private int nextChoiceIndex = 0;

    @Override
    public String giveClue(SpyMaster owner, ReadOnlyGameState state) {
        Card  ret    = null;
        Board board  = state.boardProperty().get();
        int   w      = board.getWidth();
        int   l      = board.getLength();
        int   count  = w * l;

        int nextRow = nextChoiceIndex % w;
        int nextCol = nextChoiceIndex % l;
        
        while (nextChoiceIndex < count && (ret=getCardIfValid(owner, state, nextRow, nextCol))==null) {
            nextChoiceIndex++;
            nextRow = nextChoiceIndex % w;
            nextCol = nextChoiceIndex % l;
        }
        
        return ret==null? null : ret.getCodename();
    }

    private Card getCardIfValid(SpyMaster owner, ReadOnlyGameState state, int x, int y) {
        Card card = state.boardProperty().get().getCard(x, y);
        
        if (card.getValue()!=owner.getTeam().getValue() || 
                state.getChosenCards().contains(new Coordinates(x, y))) {
            return null;
        } else {
            return card;
        }
    }
}
