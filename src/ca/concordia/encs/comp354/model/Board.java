package ca.concordia.encs.comp354.model;

public abstract class Board {

    /**
     * Returns the word at the given coordinates.
     * @param x horizontal coordinate; must be less than {@link #getWidth()}
     * @param y vertical coordinate; must be less than {@link #getLength()}
     * @return the word on the card at the given coordinates
     */
    public abstract String getWord(int x, int y);
    
    /**
     * Returns value of the card at the given coordinates.
     * @param x horizontal coordinate; must be less than {@link #getWidth()}
     * @param y vertical coordinate; must be less than {@link #getLength()}
     * @return a value representing the team colour at the given coordinates on the keycard
     */
    public abstract CardValue getValue(int x, int y);
    
    /**
     * @return the width of the board in tiles
     */
    public abstract int getWidth();
    
    /**
     * @return the length of the board in tiles
     */
    public abstract int getLength();

    
}
