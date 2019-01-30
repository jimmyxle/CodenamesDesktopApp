package ca.concordia.encs.comp354.controller;

import static java.util.Objects.requireNonNull;

/**
 * A word-integer pair representing a clue produced by the {@link SpyMaster}.
 * @author Nikita Leonidov
 *
 */
public final class Clue {
    private final String clue;
    private final int    guesses;
    
    public Clue(String clue, int guesses) {
        this.clue = requireNonNull(clue);
        if (guesses<1) {
            throw new IllegalArgumentException("guesses must be greater than 0");
        }
        this.guesses = guesses;
    }
    
    public String getWord() {
        return clue;
    }
    
    public int getGuesses() {
        return guesses;
    }
    
    @Override
    public String toString() {
        return "Clue [clue=" + clue + ", guesses=" + guesses + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((clue == null) ? 0 : clue.hashCode());
        result = prime * result + guesses;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Clue other = (Clue) obj;
        if (clue == null) {
            if (other.clue != null)
                return false;
        } else if (!clue.equals(other.clue))
            return false;
        if (guesses != other.guesses)
            return false;
        return true;
    }
    
    
}
