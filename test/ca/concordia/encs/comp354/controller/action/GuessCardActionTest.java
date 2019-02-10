package ca.concordia.encs.comp354.controller.action;

import org.junit.Test;

import ca.concordia.encs.comp354.controller.GameEvent;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.Team;

import static org.junit.Assert.*;
import static ca.concordia.encs.comp354.controller.GameEvent.*;
import static ca.concordia.encs.comp354.model.CardValue.*;

public class GuessCardActionTest extends AbstractActionTest {

    private static final Coordinates
        RED_CARD      = new Coordinates(0, 0),
        BLUE_CARD     = new Coordinates(1, 0),
        NEUTRAL_CARD  = new Coordinates(2, 0),
        ASSASSIN_CARD = new Coordinates(3, 0);
    
    private static final int TEAM_GOAL = 5;
    private static final int RED_COLUMN = 0;
    private static final int BLUE_COLUMN = 1;
    
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
        setGuesses(1);
        pushGuess(Team.RED, RED_CARD);
        assertTrue(model.getChosenCards().contains(new Coordinates(0, 0)));
    }
    
    @Test(expected=IllegalStateException.class)
    public void failsOnRepeatCard() {
        setGuesses(2);
        pushGuess(Team.RED, RED_CARD);
        pushGuess(Team.RED, RED_CARD);
    }
    
    @Test
    public void redCardGivesRedPoints() {
        setGuesses(2);
        assertScores(0, 0);
        pushGuess(Team.RED,  RED_CARD);
        assertScores(1, 0);
        pushGuess(Team.BLUE, RED_CARD);
        assertScores(2, 0);
    }
    
    @Test
    public void blueCardGivesBluePoints() {
        setGuesses(2);
        assertScores(0, 0);
        // red guess blue
        pushGuess(Team.RED,  BLUE_CARD);
        assertScores(0, 1);
        // blue guess blue
        pushGuess(Team.BLUE, BLUE_CARD);
        assertScores(0, 2);
    }
    
    @Test
    public void neutralCardGivesNoPoints() {
        setGuesses(2);
        assertScores(0, 0);
        pushGuess(Team.RED,  NEUTRAL_CARD);
        assertScores(0, 0);
        pushGuess(Team.BLUE, NEUTRAL_CARD);
        assertScores(0, 0);
    }
    
    @Test
    public void assassinCardGivesNoPoints() {
        setGuesses(2);
        assertScores(0, 0);
        pushGuess(Team.RED,  ASSASSIN_CARD);
        assertScores(0, 0);
        pushGuess(Team.BLUE, ASSASSIN_CARD);
        assertScores(0, 0);
    }

    @Test
    public void redGuessingBlueEndsTurn() {
        setGuesses(2);
        pushGuess(Team.RED, BLUE_CARD);
        assertEvent(END_TURN);
        assertGuesses(0);
    }

    @Test
    public void blueGuessingRedEndsTurn() {
        setGuesses(1);
        pushGuess(Team.BLUE, RED_CARD);
        assertEvent(END_TURN);
        assertGuesses(0);
    }
    
    @Test
    public void assassinCardEndsGame() {
        setGuesses(1);
        pushGuess(Team.RED, ASSASSIN_CARD);
        assertEvent(GAME_OVER_ASSASSIN);
    }
    
    @Test
    public void decrementsGuesses() {
        setGuesses(3);
        pushGuess(Team.RED, RED_CARD);
        assertGuesses(2);
        pushGuess(Team.RED, BLUE_CARD);
        assertGuesses(1);
        pushGuess(Team.RED, NEUTRAL_CARD);
        assertGuesses(0);
    }
    
    @Test(expected=IllegalStateException.class)
    public void failsOnNoGuessesLeft() {
        pushGuess(Team.RED, RED_CARD);
    }
    
    @Test
    public void redWinOnObjectiveMet() {
        setGuesses(TEAM_GOAL);
        for (int i=0; i<TEAM_GOAL; i++) {
            assertEvent(NONE);
            pushGuess(Team.RED, RED_COLUMN, i);
        }
        assertEvent(GAME_OVER_RED_WON);
    }
    
    @Test
    public void blueWinOnObjectiveMet() {
        setGuesses(TEAM_GOAL);
        for (int i=0; i<TEAM_GOAL; i++) {
            assertEvent(NONE);
            pushGuess(Team.BLUE, BLUE_COLUMN, i);
        }
        assertEvent(GAME_OVER_BLUE_WON);
    }
    
    // helpers
    //==================================================================================================================
    private void assertGuesses(int count) {
        assertEquals(count, model.guessesRemainingProperty().get());
    }
    
    private void assertEvent(GameEvent event) {
        assertEquals(event, model.getLastEvent());
    }
    
    private void assertScores(int red, int blue) {
        assertEquals(red,  model.getRedScore());
        assertEquals(blue, model.getBlueScore());
    }
    
    private void setGuesses(int count) {
        model.guessesRemainingProperty().set(count);
    }
    
    private void pushGuess(Team team, int x, int y) {
        pushGuess(team, new Coordinates(x, y));
    }
    
    private void pushGuess(Team team, Coordinates coords) {
        model.pushAction(new GuessCardAction(team, coords));
    }
}
