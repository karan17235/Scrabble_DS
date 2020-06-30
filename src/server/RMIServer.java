package server;

import java.lang.reflect.Method;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

import remote.IGameServer;
//import sun.awt.Win32ColorModel24;

/**
 * Creates an instance of the RemoteMath class and publishes it in the
 * rmiregistry
 * 
 */

//This is the main class and we do not need to change it anymore
public class RMIServer {
	public static int boardSize = 20;
	static ServerWindow serverWindow = new ServerWindow();
	static RMIServer server = new RMIServer();

	;
	// MatriceCell cell = new MatriceCell();
	public static ArrayList<ArrayList<MatriceCell>> matrice = new ArrayList<>();

	// ServerWindow serverWindow= new ServerWindow();

	public static void main(String[] args) {

		ServerRMIDriver rmiDriver = new ServerRMIDriver(serverWindow);
		server.matriceCreator(boardSize);

	}

	public void matriceCreator(int boardSize) {
		int i = 0;
		int j = 0;
		for (i = 0; i < boardSize; i++) {
			matrice.add(new ArrayList<>());
			for (j = 0; j < boardSize; j++) {
				matrice.get(i).add(new MatriceCell());
				matrice.get(i).get(j).setLetter(' ');
				matrice.get(i).get(j).setPositionRow(i);
				matrice.get(i).get(j).setPositionColumn(j);

			}

		}
		serverWindow.jTextArea1.append("The matrice in the server side is created"+"\n");

	}
}