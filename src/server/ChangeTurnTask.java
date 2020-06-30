package server;

import java.rmi.RemoteException;
import java.util.concurrent.Callable;

import dataObjects.TurnData;
import remote.IClientCallback;

public class ChangeTurnTask implements Callable<UserNameBooleanTuple> {

	public IClientCallback client;
	public TurnData turnData;
	public String userName;

	ChangeTurnTask(TurnData turnData, IClientCallback client, String userName)
	{
		this.turnData = turnData;
		this.client = client;
		this.userName = userName;
	}
	
	public UserNameBooleanTuple call() {
		try {
			boolean response = client.ChangeTurn(turnData);
			return new UserNameBooleanTuple(userName, response);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new UserNameBooleanTuple(userName, false);
	}
}
