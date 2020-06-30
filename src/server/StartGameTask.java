package server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import remote.IClientCallback;

public class StartGameTask implements Callable<UserNameBooleanTuple> {

	public IClientCallback client;
	public String gameUrl;
	public String userName;
	public ArrayList<String> players;
	
	StartGameTask(IClientCallback client, String gameUrl, String userName, ArrayList<String> players)
	{
		this.client = client;
		this.gameUrl = gameUrl;
		this.userName = userName;
		this.players = players;
	}
	
	public UserNameBooleanTuple call() {
		try {
			boolean response = client.StartGame(gameUrl, players);
			return new UserNameBooleanTuple(userName, response);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new UserNameBooleanTuple(userName, false);
	}
}
