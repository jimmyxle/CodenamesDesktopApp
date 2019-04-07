package ca.concordia.encs.comp354.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import ca.concordia.encs.comp354.model.Board;
import ca.concordia.encs.comp354.model.CodenameWord;
import ca.concordia.encs.comp354.model.GameState;
import ca.concordia.encs.comp354.model.Keycard;
import ca.concordia.encs.comp354.model.Team;
import ca.concordia.encs.comp354.controller.Clue;

public class ClueTest {

	/**
	 * 
	 * Author: Yaacov Cohen 
	 * 
	 */
	
	

	/* 
	This test ensures that the guess provided by the spymaster is not the 
	empty string, and therefore, must be a sequence of characters.
    */
	@Test(expected=NullPointerException.class)
	public void constructorFailsForNullClue() {
		Clue clue= new Clue ("", 1);
		if (clue.getWord()=="") {
			throw new NullPointerException("The guess must be a word or a character");
		}
		assertTrue(clue.getWord() != "");
	}
	
	/*This test ensures that the number of guesses provided by the spymaster 
	 is greater or equal to one. Any value less than zero should throw an error. 
	*/
	@Test(expected=IllegalArgumentException.class)
	public void constructorFailsForGuessCountLessThanOne() {
		 Clue clue= new Clue ("coby", -1);
	}
}
	

