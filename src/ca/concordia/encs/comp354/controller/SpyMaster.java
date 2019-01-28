//alexabrams

import java.util.Objects;

public class SpyMaster extends Player {
	static int linearCol = 0;
	static int linearRow = 0;

	public SpyMaster(CardValue team) {
		super(team);
	}
	
	public String giveClue(Board board) {
		CardValue color = board.getValue(linearRow, linearCol);
		while (color != team) {
			if (color != team) {
				if (linearCol <= 4) {
					linearCol++;
				}
				else {
					linearCol = 0;
					linearRow++;
				}
			}
			color = board.getValue(linearRow, linearCol);
		} 
		
		String codename = board.getWord(linearRow, linearCol);
		String clue = board.getAssociatedWord(linearRow, linearCol, 0);
		return clue;		
	}
}
