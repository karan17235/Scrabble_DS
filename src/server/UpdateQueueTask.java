package server;

import java.rmi.RemoteException;
import java.util.concurrent.Callable;

import remote.IClientCallback;

public class UpdateQueueTask implements Callable<UserNameBooleanTuple> {

	public IClientCallback client;
	public String[] userNames;
	public String userName;

	UpdateQueueTask(IClientCallback client, String[] usernames, String userName) {
		this.client = client;
		this.userNames = usernames;
		this.userName = userName;
	}

	public UserNameBooleanTuple call() {
		try {
			client.UpdateQueue(userNames);
			return new UserNameBooleanTuple(userName, true);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new UserNameBooleanTuple(userName, false);
	}
}
