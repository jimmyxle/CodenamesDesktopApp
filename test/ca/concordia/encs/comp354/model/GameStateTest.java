package ca.concordia.encs.comp354.model;

import org.junit.Test;

import ca.concordia.encs.comp354.controller.GameEvent;
import ca.concordia.encs.comp354.controller.action.ChangeTurnAction;
import ca.concordia.encs.comp354.model.CodenameWord.AssociatedWord;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class GameStateTest {
    
    private GameState model;
    private Supplier<Board> boardFunc;

    public GameStateTest() {
        List<CodenameWord> words = new ArrayList<>();
        
        for (int i=0; i<25; i++) {
            CodenameWord w = new CodenameWord("foo", Arrays.asList(new AssociatedWord("bar", 1)));
            words.add(w);
        }
        
        model = new GameState(new Board(words, Keycard.createRandomKeycard()));
        model = new GameState(()->new Board(words, Keycard.createRandomKeycard()));
    }
    
    // card choosing
    //==================================================================================================================
    public void chooseSucceedsOnCoordinatesWithinBounds() {
        chooseCoordinates(1, 1);
        assertChosen(1, 1);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void chooseFailsOnLargeXCoordinate() {
        chooseCoordinates(10, 0);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void chooseFailsOnLargeYCoordinate() {
        chooseCoordinates(0, 10);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void chooseFailsOnNegativeXCoordinate() {
        chooseCoordinates(-1, 0);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void chooseFailsOnNegativeYCoordinate() {
        chooseCoordinates(0, -1);
    }
    
    // card hiding
    //==================================================================================================================
    public void hideSucceedsOnCoordinatesWithinBounds() {
        chooseCoordinates(1, 1);
        hideCoordinates(1, 1);
        assertTrue(model.getChosenCards().isEmpty());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void hideFailsOnLargeXCoordinate() {
        hideCoordinates(10, 0);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void hideFailsOnLargeYCoordinate() {
        hideCoordinates(0, 10);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void hideFailsOnNegativeXCoordinate() {
        hideCoordinates(-1, 0);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void hideFailsOnNegativeYCoordinate() {
        hideCoordinates(0, -1);
    }
    
    // command queue
    //==================================================================================================================
    @Test
    public void undoFailsWithEmptyQueue() {
        assertFalse(model.undoAction());
    }
    
    @Test
    public void redoFailsWithEmptyQueue() {
        assertFalse(model.redoAction());
    }
    
    @Test
    public void pushAddsActionToHistory() {
        model.pushAction(new ChangeTurnAction(Team.RED));
        assertFalse(model.getHistory().isEmpty());
    }
    
    @Test
    public void undoAddsActionToUndoStack() {
        model.pushAction(new ChangeTurnAction(Team.RED));
        assertTrue(model.undoAction());
        assertFalse(model.getUndone().isEmpty());
    }
    
    @Test
    public void undoRemovesActionFromHistory() {
        model.pushAction(new ChangeTurnAction(Team.RED));
        assertTrue(model.undoAction());
        assertTrue(model.getHistory().isEmpty());
    }
    
    @Test
    public void redoAddsActionToHistory() {
        model.pushAction(new ChangeTurnAction(Team.RED));
        assertTrue(model.undoAction());
        assertTrue(model.redoAction());
        assertFalse(model.getHistory().isEmpty());
    }
    
    @Test
    public void redoRemovesActionFromUndoStack() {
        model.pushAction(new ChangeTurnAction(Team.RED));
        assertTrue(model.undoAction());
        assertTrue(model.redoAction());
        assertTrue(model.getUndone().isEmpty());
    }
    
    @Test
    public void pushSetsLastStepProperty() {
        assertEquals(GameEvent.NONE, model.getLastEvent());
        model.pushAction(new ChangeTurnAction(Team.RED));
        assertEquals(
            new GameStep(new ChangeTurnAction(Team.RED), GameEvent.NONE, 0, 0, 0), 
            model.lastStepProperty().get()
        );
    }
    
    @Test
    public void undoSetsLastStepProperty() {
        assertEquals(GameEvent.NONE, model.getLastEvent());
        model.pushAction(new ChangeTurnAction(Team.BLUE));
        model.undoAction();
        assertNull(model.lastStepProperty().get());
    }
    
    @Test
    public void redoSetsLastStepProperty() {
        assertEquals(GameEvent.NONE, model.getLastEvent());
        model.pushAction(new ChangeTurnAction(Team.RED));
        
        model.undoAction();
        assertNull(model.lastStepProperty().get());
        
        model.redoAction();
        assertEquals(
            new GameStep(new ChangeTurnAction(Team.RED), GameEvent.NONE, 0, 0, 0), 
            model.lastStepProperty().get()
        );
    }
    
    @Test
    public void requestNonNullPromise() {
    	assertNotNull(model.requestOperativeInput());    	
    }
    
    @Test
    public void operativeInputPropertyNonNullPromise() {
    	model.requestOperativeInput();
    	assertNotNull(model.operativeInputProperty().get());
    }
    
    @Test(expected=IllegalStateException.class)
    public void failsOnRedundantInputRequest() {
    	model.requestOperativeInput();
    	model.requestOperativeInput();
    }
    
    @Test
    public void operativeInputPropertySetToNull() {
    	model.requestOperativeInput();
    	model.operativeInputProperty().get().finish(null);
    	assertNull(model.operativeInputProperty().get());
    }
    
    @Test
    public void propertiesBackToDefault() {
    	model.reset();
    	boardFunc.get();
    	assertSame(boardFunc.get(), boardFunc.get());
    }
    
    @Test
    public void boardPropertyAfterReset() {
    	model.boardProperty();
    	model.reset();
    	boardFunc.get();
    	assertNotSame(model.boardProperty().get(), boardFunc.get());
    }
    
    // helpers
    //==================================================================================================================
    private void chooseCoordinates(int x, int y) {
        model.chooseCard(new Coordinates(x, y));
    }
    
    private void hideCoordinates(int x, int y) {
        model.chooseCard(new Coordinates(x, y));
    }
    
    private void assertChosen(int x, int y) {
        assertTrue(model.getChosenCards().contains(new Coordinates(x, y)));
    }
}
