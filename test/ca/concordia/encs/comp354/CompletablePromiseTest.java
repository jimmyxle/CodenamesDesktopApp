package ca.concordia.encs.comp354;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 
 * @author Nikita Leonidov
 *
 */
public class CompletablePromiseTest {

    @Test
    public void finishHandlerCalledOnFinish() {
        List<String> actual = new ArrayList<>();
        new CompletablePromise<Void>()
            .then(value->actual.add("finished"))
            .finish(null);
        
        assertEquals(Arrays.asList("finished"), actual);
    }
    
    @Test
    public void finishHandlersCalledInOrder() {
        List<String> actual = new ArrayList<>();
        new CompletablePromise<Void>()
            .then(value->actual.add("a"))
            .then(value->actual.add("b"))
            .finish(null);
        
        assertEquals(Arrays.asList("a", "b"), actual);
    }
    
    /**
     * Ensure that a handler called on an already-finished promise is invoked when attached.
     */
    @Test
    public void finishedHandlerCalledOnPreviouslyFinishedPromise() {
        List<String> actual = new ArrayList<>();
        new CompletablePromise<Void>()
            .finish(null)
            .then(value->actual.add("finished"));
        
        assertEquals(Arrays.asList("finished"), actual);
    }
    
    @Test
    public void cancelHandlerCalledOnCancel() {
        List<String> actual = new ArrayList<>();
        new CompletablePromise<Void>()
            .ifCancelled(()->actual.add("cancelled"))
            .cancel();
        
        assertEquals(Arrays.asList("cancelled"), actual);
    }
    
    @Test
    public void cancelHandlersCalledInOrder() {
        List<String> actual = new ArrayList<>();
        new CompletablePromise<Void>()
            .ifCancelled(()->actual.add("a"))
            .ifCancelled(()->actual.add("b"))
            .cancel();
        
        assertEquals(Arrays.asList("a", "b"), actual);
    }
    
    /**
     * Ensure that a handler called on an already-cancelled promise is invoked when attached.
     */
    @Test
    public void cancelHandlerCalledOnPreviouslyCancelledPromise() {
        List<String> actual = new ArrayList<>();
        new CompletablePromise<Void>()
            .cancel()
            .ifCancelled(()->actual.add("cancelled"));
        
        assertEquals(Arrays.asList("cancelled"), actual);
    }
    
    @Test(expected=IllegalStateException.class)
    public void failOnFinishFinishedPromise() {
        new CompletablePromise<Void>()
            .finish(null)
            .finish(null);
    }
    
    @Test(expected=IllegalStateException.class)
    public void failOnCancelCancelledPromise() {
        new CompletablePromise<Void>()
            .cancel()
            .cancel();
    }

    @Test(expected=IllegalStateException.class)
    public void failOnFinishCancelledPromise() {
        new CompletablePromise<Void>()
            .cancel()
            .finish(null);
    }

    @Test(expected=IllegalStateException.class)
    public void failOnCancelledFinishedPromise() {
        new CompletablePromise<Void>()
            .finish(null)
            .cancel();
    }
    
    @Test
    public void finishedPromiseIsFinished() {
        assertTrue(new CompletablePromise<Void>().finish(null).isFinished());
    }
    
    @Test
    public void cancelledPromiseIsCancelled() {
        assertTrue(new CompletablePromise<Void>().cancel().isCancelled());
    }
    
    @Test
    public void newPromiseIsNotFinished() {
        assertFalse(new CompletablePromise<Void>().isFinished());
    }
    
    @Test
    public void newPromiseIsNotCancelled() {
        assertFalse(new CompletablePromise<Void>().isCancelled());
    }
    
    @Test(expected=IllegalStateException.class)
    public void failOnGetUnfinishedPromise() {
        new CompletablePromise<Void>().get();
    }
    
    public void successfullyGetFinishedPromise() {
        assertEquals("foo", new CompletablePromise<String>().finish("foo").get());
    }
    
}
