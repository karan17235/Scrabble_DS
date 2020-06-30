package server;

import java.rmi.RemoteException;
import java.util.concurrent.Callable;

import remote.IClientCallback;

public class GameOverTask implements Callable<UserNamePassBooleanTuple> {
	
	public IClientCallback client;
	public String userName;

	public GameOverTask(IClientCallback client, String userName) {
		// TODO Auto-generated constructor stub
		this.client = client;
		this.userName = userName;
	}
	@Override
	public UserNamePassBooleanTuple call() throws RemoteException {
		// TODO Auto-generated method stub
		try {
			client.EndGame();
			return new UserNamePassBooleanTuple(userName, true);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return new UserNamePassBooleanTuple(userName, false);
	}
}
