package ca.concordia.encs.comp354.controller.action;

import org.junit.Test;

import ca.concordia.encs.comp354.controller.Clue;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.Team;

import static org.junit.Assert.*;
import static ca.concordia.encs.comp354.model.CardValue.*;

public class GuessCardActionTest extends AbstractActionTest {

    GuessCardActionTest() {
        super(
            RED, BLUE, NEUTRAL, ASSASSIN, NEUTRAL,
            RED, BLUE, NEUTRAL, ASSASSIN, NEUTRAL,
            RED, BLUE, NEUTRAL, ASSASSIN, NEUTRAL,
            RED, BLUE, NEUTRAL, ASSASSIN, NEUTRAL,
            RED, BLUE, NEUTRAL, ASSASSIN, NEUTRAL
        );
    }
    
    @Test
    public void revealsCard() {
        model.pushAction(new GuessCardAction(Team.RED, new Coordinates(0, 0)));
        assertEquals(new Clue("foo", 1), model.lastClueProperty().get());
    }
}
