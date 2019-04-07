package ca.concordia.encs.comp354.controller;

import org.junit.Test;
import ca.concordia.encs.comp354.controller.Clue;

/**
 * 
 * @author Yaacov Cohen 
 * 
 */
public class ClueTest {

	/* 
	This test ensures that the guess provided by the spymaster is not the 
	empty string, and therefore, must be a sequence of characters.
    */
	@Test(expected=NullPointerException.class)
	public void constructorFailsForNullClue() {
		new Clue (null, 1);
	}
	
	/*This test ensures that the number of guesses provided by the spymaster 
	 is greater or equal to one. Any value less than zero should throw an error. 
	*/
	@Test(expected=IllegalArgumentException.class)
	public void constructorFailsForGuessCountLessThanOne() {
		 new Clue("coby", -1);
	}
}
	

