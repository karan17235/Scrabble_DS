/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mgoudarzi
 */
public class ServerRMIDriver {
	
	public ServerWindow serverWindow;

    public ServerRMIDriver(ServerWindow serverWindow) {
        try {
        	this.serverWindow = serverWindow;
            //Export the remote math object to the Java RMI runtime so that it
            //can receive incoming remote calls.
            //Because RemoteMath extends UnicastRemoteObject, this
            //is done automatically when the object is initialized.
            //Publish the remote object's stub in the registry under the name "Compute"
            //Registry registry = LocateRegistry.getRegistry();
            LocateRegistry.createRegistry(1099);
            //Registry registry = LocateRegistry.getRegistry();
//            GameServer server = new GameServer(registry);
            GameServer server = new GameServer(serverWindow);
            InetAddress inetAddress = InetAddress.getLocalHost();
            //String url = "rmi://10.12.99.30/server";
            String url = "rmi://localhost/server";
            //String url = "rmi://" + inetAddress.getHostAddress() + "/server";
            serverWindow.jTextArea1.append("Server URL: " + url + "\n");
//			IGameServer gameServer = new GameServer(registry);
            //registry.bind("GameServer", server);
            Naming.rebind(url, server);
            System.out.println("Game server ready");
           
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
            System.out.println("[System] Server failed: " + e);
        } catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
