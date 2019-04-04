package ca.concordia.encs.comp354.model;

public final class SkipEvent implements HumanOperativeEvent {

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
