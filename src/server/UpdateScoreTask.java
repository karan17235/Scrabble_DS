package server;

import java.rmi.RemoteException;
import java.util.concurrent.Callable;

import remote.IClientCallback;

public class UpdateScoreTask implements Callable<UserNameBooleanTuple> {

	public IClientCallback client;
	public int score;
	public String userName;

	UpdateScoreTask(IClientCallback client, int score, String userName) {
		this.client = client;
		this.score = score;
		this.userName = userName;
	}

	public UserNameBooleanTuple call() {
		try {
			client.UpdateScore(score, userName);
			return new UserNameBooleanTuple(userName, true);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new UserNameBooleanTuple(userName, false);
	}
}
