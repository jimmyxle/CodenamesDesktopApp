package ca.concordia.encs.comp354.controller.action;

import ca.concordia.encs.comp354.controller.Clue;
import ca.concordia.encs.comp354.controller.SpyMaster;
import ca.concordia.encs.comp354.controller.SpyMaster.Strategy;
import ca.concordia.encs.comp354.model.ReadOnlyGameState;

public class NullSpyMasterStrategy implements Strategy {

    @Override
    public Clue giveClue(SpyMaster owner, ReadOnlyGameState state) {
        // do nothing
        return null;
    }

}
