package ca.concordia.encs.comp354.controller;

import java.util.List;
import java.util.Random;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.ReadOnlyGameState;

public class RandomOperativeStrategy extends AbstractPlayerStrategy implements Operative.Strategy {
    
    private final Random random = new Random();
    @Override
    public Coordinates guessCard(Operative owner, ReadOnlyGameState state, String clue) {
        List<Coordinates> guesses = beginTurn(owner, state);
        return guesses.isEmpty()? null : guesses.remove(random.nextInt(guesses.size()));
    }

    @Override
    protected boolean isValidGuess(Player owner, Board board, int x, int y) {
        return true;
    }
}
