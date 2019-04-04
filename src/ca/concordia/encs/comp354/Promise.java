package ca.concordia.encs.comp354;

import java.util.function.Consumer;

/**
 * Promises are values that may have their values set at a later time, triggering consumers once a value has 
 * been assigned. A promise may only have its value set once. Not to be confused with {@link 
 * java.util.concurrent.Future}s; these are not meant for concurrent programming.
 * 
 * @author Mykyta Leonidov
 *
 * @param <T> the type of the promised value
 */
public interface Promise<T> {

    /**
     * @return this promise's value iff <code>{@link #isFinished()}==true</code>
     */
    T get();

    /**
     * @return <code>true</code> if this promise has been finished
     */
    boolean isFinished();
    
    /**
     * Attaches a consumer to this promise. The consumer will be called when the value has been set, or 
     * immediately if <code>{@link #isFinished()}==true</code>.
     * 
     * @param func  the consumer to attach to this promise
     * @return this promise
     */
    Promise<T> then(Consumer<T> func);

    /**
     * Creates an already-finished promise with the given value.
     * @param value the desired value for the promise
     * @return a finished promise with the given value
     */
    static <T> Promise<T> finished(T value) {
        return new CompletablePromise<T>().finish(value);
    }
}
