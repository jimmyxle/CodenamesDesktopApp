package ca.concordia.encs.comp354;

import java.util.function.Consumer;

public interface Promise<T> {

    T get();

    Promise<T> then(Consumer<T> func);

    static <T> Promise<T> finished(T value) {
        return new CompletablePromise<T>().finish(value);
    }
}
