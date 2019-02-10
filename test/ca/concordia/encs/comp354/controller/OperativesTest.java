package ca.concordia.encs.comp354.controller;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.Card;
import ca.concordia.encs.comp354.model.CardValue;
import ca.concordia.encs.comp354.model.CodenameWord;
import ca.concordia.encs.comp354.model.CodenameWord.AssociatedWord;
import ca.concordia.encs.comp354.model.Coordinates;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Keycard;
import ca.concordia.encs.comp354.model.Team;

public class OperativesTest extends AbstractPlayerTest {
	
	final GameState model;
	
	public OperativesTest() throws IOException {
		List<CodenameWord> codenameWords = Card.createRandomCodenameList(Paths.get("res/words.txt"));
		Keycard keycard = Keycard.createRandomKeycard();
		
		model = new GameState(new Board(codenameWords, keycard));
	}
	
	@Test
	public void picksAllCardsSeq() {
		Operative seq = new Operative(Team.RED, new SequentialOperativeStrategy());
		Clue clue = new Clue("test", 1);
		for (int i = 0; i < 25; i++) {
			Coordinates guess = seq.guessCard(model, clue);
			model.chooseCard(guess);
		}
		
		assertTrue(model.getChosenCards().size() == model.getBoard().getLength() * model.getBoard().getWidth());
	}
	
	@Test
	public void picksAllCardsRand() {
		Operative seq = new Operative(Team.RED, new RandomOperativeStrategy());
		Clue clue = new Clue("test", 1);
		for (int i = 0; i < 25; i++) {
			Coordinates guess = seq.guessCard(model, clue);
			model.chooseCard(guess);
		}
		
		assertTrue(model.getChosenCards().size() == model.getBoard().getLength() * model.getBoard().getWidth());
	}
	
	@Test (expected = IllegalStateException.class)
	public void pickExtraCardSeq() {
		Operative seq = new Operative(Team.RED, new SequentialOperativeStrategy());
		Clue clue = new Clue("test", 1);
		for (int i = 0; i < 26; i++) {
			Coordinates guess = seq.guessCard(model, clue);
			model.chooseCard(guess);
		}
	}
	
	@Test (expected = IllegalStateException.class)
	public void pickExtraCardRand() {
		Operative seq = new Operative(Team.RED, new RandomOperativeStrategy());
		Clue clue = new Clue("test", 1);
		for (int i = 0; i < 26; i++) {
			Coordinates guess = seq.guessCard(model, clue);
			model.chooseCard(guess);
		}
	}
	
	
	
}
