//@alexabrams

public class Operative extends Player {
	private static int numOps = 0; //keep track of number of operatives
	private int teamMember; //var for player number
	private SpyMaster teammate;
	
	public Operative(CardValue team, SpyMaster teammate) {
		super(team);
		this.teammate = teammate;
		numOps++;
		teamMember = numOps;			
	}
	
	public String guessWord() {
		String clue = teammate.giveClue(board);
		
	}
	
}
