package remote;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import dataObjects.StringListStringTuple;
import dataObjects.TurnData;

/**
 * RMI Remote interface - must be shared between client and server. All methods
 * must throw RemoteException. All parameters and return types must be either
 * primitives or Serializable.
 *
 * Any object that is a remote object must implement this interface. Only those
 * methods specified in a "remote interface" are available remotely.
 */
//FOR COMMENTS SEE IMPLEMENTATION ALSO
// The login and queuing methods on the server side
public interface IGameServer extends Remote {

	public static final int FAILURE = -1;
	public static final int SUCCESS = 0;

	public int register(Object callbackObj, String userName) throws RemoteException;

	public int broadcastMsg(Object callbackObj, String userName, String msg) throws RemoteException;

	public StringListStringTuple startGame(String userName, List<String> inviteList) throws RemoteException;

	public boolean registerUser(String userName, String password) throws RemoteException;

	public boolean login(String userName, String password) throws RemoteException;

	public boolean quit(String userName) throws RemoteException;

}
