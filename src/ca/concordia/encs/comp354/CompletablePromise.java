package ca.concordia.encs.comp354;

import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

public class CompletablePromise<T> implements Promise<T> {
    
    private T       value = null;
    private boolean set   = false;
    
    private final List<Consumer<T>> onFinished = new ArrayList<>();
    
    public CompletablePromise() {}
    
    public CompletablePromise<T> finish(T value) {
        this.value = value;
        set = true;
        
        if (set && onFinished!=null) {
            for (Consumer<T> k : onFinished) {
                k.accept(value);
            }
        }
        
        return this;
    }

    @Override
    public T get() {
        if (set==false) {
            throw new IllegalStateException("not finished");
        }
        return value;
    }
    
    @Override
    public CompletablePromise<T> then(Consumer<T> func) {
        requireNonNull(func);
        if (set) {
            func.accept(value);
        } else {
            onFinished.add(func);
        }
        return this;
    }
}
