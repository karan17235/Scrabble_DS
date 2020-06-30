package server;

//This class is to store the client and player objects in the same array. It creates an association between the client and player objects. 
public class ClientPlayerTuple<IClientCallback, Player> {
	  public final IClientCallback client; 
	  public final Player player; 
	  public ClientPlayerTuple(IClientCallback client, Player player) { 
	    this.client = client; 
	    this.player = player; 
	  } 
}
