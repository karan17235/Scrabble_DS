package remote;

import dataObjects.TurnData;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
// FOR COMMENTS SEE IMPLEMENTATION ALSO
// This interface has all the game mechanics

public interface IGame extends Remote {
    
    public void setConsecutivePasses() throws RemoteException;
        public void updateGreenList(
            int word1X1coordinate,
            int word1X2coordinate,
            int word1Y1coordinate,
            int word1Y2coordinate,
            int word2X1coordinate,
            int word2X2coordinate,
            int word2Y1coordinate,
            int word2Y2coordinate) throws RemoteException;

    public String getGameName() throws RemoteException;
    //public int scoreCalculation (String userName, String word)throws RemoteException;

    public void endTurn(String userName, TurnData turnData) throws RemoteException;

    public void exit(String userName) throws RemoteException;

    public int Vote(String userName, ArrayList words) throws RemoteException;

    public int isWord(String word) throws RemoteException;

    public int endGame(String userName) throws RemoteException;
}
