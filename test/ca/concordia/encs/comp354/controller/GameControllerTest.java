package ca.concordia.encs.comp354.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.CodenameWord;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Keycard;
import ca.concordia.encs.comp354.model.Team;

import static org.junit.Assert.*;

public class GameControllerTest {

    final GameState      model;
    final GameController controller;
    
    public GameControllerTest() {
        Keycard keycard = Keycard.generateRandomKeycard();
        List<CodenameWord> words = new ArrayList<>();
        for (int i=0; i<25; i++) {
            words.add(new CodenameWord("foo", Arrays.asList(new CodenameWord.AssociatedWord("bar", 1))));
        }
        
        model = new GameState(new Board(words, keycard));
        controller = new GameController.Builder()
                .setRedSpyMaster (new SpyMaster(Team.RED,  new SequentialSpyMasterStrategy()))
                .setBlueSpyMaster(new SpyMaster(Team.BLUE, new SequentialSpyMasterStrategy()))
                .setRedOperative (new Operative(Team.RED,  new SequentialOperativeStrategy()))
                .setBlueOperative(new Operative(Team.BLUE, new SequentialOperativeStrategy()))
                .setInitialTurn(Team.RED)
                .setModel(model)
                .create();
    }
}
