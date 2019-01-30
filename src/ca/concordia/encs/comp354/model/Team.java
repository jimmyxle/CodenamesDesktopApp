package ca.concordia.encs.comp354.model;

public enum Team {
    RED(CardValue.RED),
    BLUE(CardValue.BLUE);
    
    private CardValue scoreValue;

    private Team(CardValue scoreValue) {
        this.scoreValue = scoreValue;
    }
    
    public CardValue getValue() {
        return scoreValue;
    }
}
