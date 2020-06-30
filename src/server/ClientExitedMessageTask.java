package server;

import java.rmi.RemoteException;
import java.util.concurrent.Callable;

import remote.IClientCallback;

public class ClientExitedMessageTask  implements Callable<UserNameBooleanTuple> {

	public IClientCallback client;
	public String userName;
        
	ClientExitedMessageTask(IClientCallback client, String userName) {
		this.client = client;
		this.userName = userName;
	}


	public UserNameBooleanTuple call() {
		try {                    
                    client.UserExited(userName);
                    return new UserNameBooleanTuple(userName, true);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new UserNameBooleanTuple(userName, false);
	}
}
