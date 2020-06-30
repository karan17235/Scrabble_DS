/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientscrabble;

/**
 *
 * @author mgoudarzi
 */
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import remote.IGame;
import remote.IGameServer;
import remote.IClientCallback;

public class ClientScrabble {

    public static int BoardSize = 20;
    private static IClientCallback client;
    private static IGameServer gameServer;

    /**
     * @param args the command line argument
     */
    public static void main(String[] args) {

      //  LoginScreen loginScreen = new LoginScreen();
       GameWindow gameWindow=new GameWindow(BoardSize);
        
       gameWindow.DisableKeyboard();
       gameWindow.GridButtonPressed();

      // GameMainController controller = new GameMainController();
    }
}
