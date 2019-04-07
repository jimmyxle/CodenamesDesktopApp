package ca.concordia.encs.comp354;

import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of {@link Promise} that may have values assigned to it.
 * @author Mykyta Leonidov
 *
 * @param <T> the type of the promised value
 */
public class CompletablePromise<T> implements Promise<T> {
    
    private T       value     = null;
    private boolean finished       = false;
    private boolean cancelled = false;
    
    private final List<Consumer<T>> onFinished  = new ArrayList<>();
    private final List<Runnable>    onCancelled = new ArrayList<>();
    
    public CompletablePromise() {}
    
    /**
     * Finishes this promise with the given value, triggering any attached consumers.
     * @param value the value to pass to any consumers
     * @return this promise
     */
    public CompletablePromise<T> finish(T value) {
        if (cancelled) {
            throw new IllegalStateException("cancelled");
        }
        
        this.value = value;
        finished = true;
        
        if (finished && onFinished!=null) {
            for (Consumer<T> k : onFinished) {
                k.accept(value);
            }
        }
        
        return this;
    }
    
    /**
     * Cancels this promise, triggering any attached cancellation handlers.
     * @return this promise
     */
    public CompletablePromise<T> cancel() {
        if (cancelled) {
            throw new IllegalStateException("finished");
        }
        
        cancelled = true;
        
        if (finished && onCancelled!=null) {
            for (Runnable k : onCancelled) {
                k.run();
            }
        }
        
        return this;
    }

    @Override
    public T get() {
        if (finished==false) {
            throw new IllegalStateException("not finished");
        }
        return value;
    }
    
    @Override
    public CompletablePromise<T> then(Consumer<T> func) {
        requireNonNull(func);
        if (finished) {
            func.accept(value);
        } else {
            onFinished.add(func);
        }
        return this;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
    
    @Override
    public Promise<T> ifCancelled(Runnable func) {
        requireNonNull(func);
        if (cancelled) {
            func.run();
        } else {
            onCancelled.add(func);
        }
        return this;
    }
    
    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
