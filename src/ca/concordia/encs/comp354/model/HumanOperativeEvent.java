package ca.concordia.encs.comp354.model;

public interface HumanOperativeEvent {
    
    default boolean isSkip() {
        return false;
    }
    
    default SkipEvent asSkip() {
        throw new UnsupportedOperationException();
    }
    
    default boolean isGuess() {
        return false;
    }
    
    default GuessEvent asGuess() {
        throw new UnsupportedOperationException();
    }
    
}
