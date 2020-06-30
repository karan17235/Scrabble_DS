package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import dataObjects.TurnData;
// FOR COMMENTS SEE IMPLEMENTATION ALSO
//Has methods which the server needs to call on the client object. 

public interface IClientCallback extends Remote {

    public boolean ChangeTurn(TurnData turnData) throws RemoteException;

	public boolean StartGame(String gameName, ArrayList<String> players) throws RemoteException;

    public void EndGame() throws RemoteException;

    public boolean Vote(String word) throws RemoteException;

    public String getPlayerName() throws RemoteException;

    // Callback to the client side to tell the client that a new player has joined
    // the queue.
    public void UpdateQueue(String[] queue) throws RemoteException;

    public void UpdateChatbox(String msg) throws RemoteException;

    public void UpdateScore(int score, String userName) throws RemoteException;

    public boolean invite(String inviter) throws RemoteException;
    // public String GetUserName() throws RemoteException;

	public void UserExited(String userName) throws RemoteException;
           public void HighlightWords(boolean word1Accepted,
            boolean word2Accepted,
            List<int[]> greenList1,
            List<int[]> greenList2) throws RemoteException;
}

