package ca.concordia.encs.comp354.model;

public final class SkipEvent implements OperativeEvent {

    public SkipEvent() {}
    
    @Override
    public boolean isSkip() {
        return true;
    }

    @Override
    public SkipEvent asSkip() {
        return this;
    }

}
