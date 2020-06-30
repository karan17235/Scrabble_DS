package server;

import java.rmi.RemoteException;
import java.util.concurrent.Callable;

import dataObjects.TurnData;
import remote.IClientCallback;

public class InvitePlayerTask implements Callable<UserNameBooleanTuple> {

	public IClientCallback client;
	public String inviter;
	public String userName;

	InvitePlayerTask(IClientCallback client, String inviter, String userName)
	{
		this.client = client;
		this.inviter = inviter;
		this.userName = userName;
	}

	public UserNameBooleanTuple call() {
		try {
			boolean response = client.invite(inviter);
			return new UserNameBooleanTuple(userName, response);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new UserNameBooleanTuple(userName, false);
	}

}
