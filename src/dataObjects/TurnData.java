package dataObjects;

// This is the data object which is used for communication between the client and server
public class TurnData implements java.io.Serializable {
	public int row;
	public int column;
	public char letter;

	// public String horizontal;
	// public String vertical;
	public int score;

	// Server -> Client
	public boolean isTurn;
	public String nextUser;
	
	public TurnData()
	{
		
	}
	
	public TurnData(TurnData other)
	{
		this.row = other.row;
		this.column = other.column;
		this.letter = other.letter;
		this.score = other.score;
		this.isTurn = other.isTurn;
		this.nextUser = other.nextUser;
	}
}
