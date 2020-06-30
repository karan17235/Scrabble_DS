package server;

import java.rmi.RemoteException;
import java.util.concurrent.Callable;

import remote.IClientCallback;

public class UpdateChatboxTask implements Callable<UserNameBooleanTuple> {

	public IClientCallback client;
	public String msgs;
	public String userName;
        
	UpdateChatboxTask(IClientCallback client, String msgs, String userName) {
		this.client = client;
		this.msgs = msgs;
		this.userName = userName;
	}


	public UserNameBooleanTuple call() {
		try {                    
                    client.UpdateChatbox("["+ userName + "] " + msgs);
                    return new UserNameBooleanTuple(userName, true);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new UserNameBooleanTuple(userName, false);
	}
}
