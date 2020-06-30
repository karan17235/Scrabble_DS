package clientscrabble;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import dataObjects.StringListStringTuple;
import dataObjects.TurnData;
import java.awt.Color;
import java.net.MalformedURLException;
import java.rmi.Naming;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import remote.IGame;
import remote.IGameServer;
import remote.IClientCallback;

// Implementation of client callback along with client specific methods. CaLLS server rmi methods
public class Client extends UnicastRemoteObject implements IClientCallback, IClientGame {

    public IGame game;
    private IGameServer gameServer;
    private String gameName;
    public Player player;
    public GameWindow gameWindow;

    protected Client(IGameServer gameServer, Registry registry, GameWindow gameWindow, Player player)
            throws RemoteException {
        this.gameServer = gameServer;
        this.player = player;
        this.gameWindow = gameWindow;
        // TODO Auto-generated constructor stub
    }

    // Finish turn at the client side
    public boolean ChangeTurn(TurnData turnData) throws RemoteException {
        return gameWindow.changeTurn(turnData);
    }

    // Start game by calling server method
    public boolean StartGame(ArrayList<String> inviteList) throws RemoteException {
		StringListStringTuple response = gameServer.startGame(player.getUsername(), inviteList);
		gameName = response.string;
        try {
            // game = (IGame) Naming.lookup("rmi://10.12.102.156/" + gameName);
            System.out.println("Game Name: " + gameName + "\n");
            game = (IGame) Naming.lookup(gameName);
            gameWindow.CreateRankingScreen((ArrayList<String>) response.arrayList);
            // game = (IGame) registry.lookup(gameName);
        } catch (NotBoundException | MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    public boolean StartGame(String gameName, ArrayList<String> players) throws RemoteException {
        try {
            // game = (IGame) Naming.lookup("rmi://10.12.102.156/" + gameName);
            System.out.println("Game Name: " + gameName + "\n");
            game = (IGame) Naming.lookup(gameName);
            gameWindow.CreateRankingScreen(players);
            // game = (IGame) registry.lookup(gameName);
        } catch (NotBoundException | MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    // Get the name of the game which was set at the server
    public String GetGameName() {
        // TODO Auto-generated method stub
        return gameName;
    }

    // Vote on a word
    @Override
    public boolean Vote(String word) throws RemoteException {
        int response;
        if (word.length() > 1) {
            response = this.game.isWord(word);
        } else {
            response = 0;
        }
        ImageIcon ic = new javax.swing.ImageIcon(getClass().getResource("/clientscrabble/if_dictionary_87870.png"));
//        JFrame jf = new JFrame();
//        jf.setAlwaysOnTop(true);
        int option;
        if (response == 1) {
            option = JOptionPane.showConfirmDialog(this.gameWindow,
                    "Do you accept this word: " + word + " as a meanningful word ?",
                    "Select an Option...",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    ic);

        } else {
            option = JOptionPane.showConfirmDialog(this.gameWindow,
                    "Do you accept this word: " + word + " as a meanningful word ?",
                    "Select an Option...",
                    JOptionPane.YES_NO_OPTION);
        }

        if (option == 0) {
            return true;
        } else {
            return false;
        }

    }

    // Called by the server when it wants to update the list of users in the current
    // queue
    @Override
    public void UpdateQueue(String[] queue) throws RemoteException {
        // TODO Auto-generated method stub
        gameWindow.UpdateQueue(queue);
    }

    public void Quit() {
        try {
            gameServer.quit(player.getUsername());
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void UpdateChatbox(String msg) throws RemoteException {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        gameWindow.UpdateChatbox(msg);
    }

    @Override
    public void UpdateScore(int score, String userName) throws RemoteException {
        gameWindow.updateScoreForPlayer(score, userName);

    }

    @Override
    public String getPlayerName() throws RemoteException {
        return this.player.getUsername();
    }

    @Override
    public boolean invite(String inviter) throws RemoteException {
        // TODO Auto-generated method stub
        return gameWindow.invite(inviter);
    }

    @Override
    public void EndGame() {
        //JOptionPane.showMessageDialog(this.gameWindow, "Game Over, Every Participant Pressed Pass", "Exit");
        //JFrame jf= new JFrame();
        //jf.setAlwaysOnTop(true);
       // JOptionPane.showMessageDialog(this.gameWindow,"Game Over, Every Participant Pressed Pass Exit");
        gameWindow.jLabel6.setText("The Game Finished");
        gameWindow.DisableKeyboard();
        gameWindow.mainMenuPassButton.setEnabled(false);
        gameWindow.jButtonAskGame.setEnabled(false);
         gameWindow.mainMenuStartButton.setEnabled(false);
    }

	@Override
	public void UserExited(String userName) throws RemoteException {
		gameWindow.removeUserFromPlayerList(userName);
		
	}
            @Override
    public void HighlightWords(
            boolean word1Accepted,
            boolean word2Accepted,
            List<int[]> greenList1,
            List<int[]> greenList2) throws RemoteException {
        System.out.println("got here " + greenList1.size());
        System.out.println("got here " + greenList2.size());
        if(word1Accepted){
        for(int[] a : greenList1){
            System.out.println("Setting " + a[0] + ", " + a[1] + " into green!");
            gameWindow.gridButtonList.get(a[0]).get(a[1]).setBackground(Color.GREEN);
        }
        }
        if(word2Accepted){
                    for(int[] a : greenList2){
            System.out.println("Setting " + a[0] + ", " + a[1] + " into green!");
            gameWindow.gridButtonList.get(a[0]).get(a[1]).setBackground(Color.GREEN);
        }
        }
        
        /*System.out.println("word 1 is word " + w1isWord);
        System.out.println("word 2 is word " + w2isWord);
        if (w1isWord) {
		//up to down
		int x = gameWindow.word1X1coordinate;
		int y = gameWindow.word1Y1coordinate;

		int deltaX = gameWindow.word1X2coordinate - gameWindow.word1X1coordinate;
		for (int i = 0; i <= deltaX; i++) {
			hihglightUI(y, x + i);
		}
	}

	if (w2isWord) {
		int x = gameWindow.word2X1coordinate;
		int y = gameWindow.word2Y1coordinate;
		int deltaY = gameWindow.word2Y2coordinate - gameWindow.word2Y1coordinate;
		for (int i = 0; i <= deltaY; i++) {
			hihglightUI(y + i, x);
		}
	}*/
        
    }

    /*private void hihglightUI(int y, int x) {
        System.out.println("Setting " + x + ", " + y + " into green!");
        gameWindow.gridButtonList.get(x).get(y).setBackground(Color.GREEN);
        
    }*/

}
