/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientscrabble;

//import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;
import dataObjects.TurnData;
import java.awt.Color;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import remote.IClientCallback;
import remote.IGame;
import remote.IGameServer;

/**
 *
 * @author mgoudarzi
 */
public class GameMainController {

	public GridButton button;
	GameWindow gameWindow;
	ClientScrabble clientScrabble = new ClientScrabble();

	private static IClientCallback client;
	// private static IGame game;
	private static IGameServer gameServer;
	// Player player = new Player();
	TurnData turnData = new TurnData();
	Registry registry;

	public GameMainController(GameWindow gameWindow) {
		try {
			this.gameWindow = gameWindow;
			// Connect to the rmiregistry that is running on localhost
			// registry = LocateRegistry.getRegistry("10.12.99.30");
			registry = LocateRegistry.getRegistry("localhost");

			// Retrieve the stub/proxy for the remote math object from the registry
			// gameServer = (IGameServer) Naming.lookup("rmi://10.12.99.30/server");
			gameServer = (IGameServer) Naming.lookup("rmi://localhost/server");

			// client.StartGame();
			// System.out.println(((IClientGame) client).GetGameName());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(gameWindow, "The Server is not Reachable.. Load the game Again");
			// e.printStackTrace();
		}
	}

	public void exit() {
		/*
		 * try { ((Client)client).game.exit(((Client)client).player.getUsername()); }
		 * catch (RemoteException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
	}

	public int setValidMoves(GridButton button) {
                if (button.getLetter()==' '){
                    return 0;
                }
		int x = button.getPositionRow();
		int y = button.getPositionColumn();
		int s = button.getButtonState();
		gameWindow.gridButtonList.get(x).get(y).setEnabled(false);
		gameWindow.gridButtonList.get(x).get(y).setBackground(Color.ORANGE);
		gameWindow.gridButtonList.get(x).get(y).setButtonState(button.ASSIGNED);

		// System.out.println(gameWindow.gridButtonList.get(x).get(y).getText());
		if (x > 0 && y > 0 && x < clientScrabble.BoardSize - 1 && y < clientScrabble.BoardSize - 1) {
			if (gameWindow.gridButtonList.get(x - 1).get(y).getButtonState() == button.NOT_ASSIGNED) {
				gameWindow.gridButtonList.get(x - 1).get(y).setEnabled(true);
				gameWindow.gridButtonList.get(x - 1).get(y).setBackground(Color.white);
			}
			if (gameWindow.gridButtonList.get(x + 1).get(y).getButtonState() == button.NOT_ASSIGNED) {
				gameWindow.gridButtonList.get(x + 1).get(y).setEnabled(true);
				gameWindow.gridButtonList.get(x + 1).get(y).setBackground(Color.white);
			}
			if (gameWindow.gridButtonList.get(x).get(y - 1).getButtonState() == button.NOT_ASSIGNED) {
				gameWindow.gridButtonList.get(x).get(y - 1).setEnabled(true);
				gameWindow.gridButtonList.get(x).get(y - 1).setBackground(Color.white);
			}
			if (gameWindow.gridButtonList.get(x).get(y + 1).getButtonState() == button.NOT_ASSIGNED) {
				gameWindow.gridButtonList.get(x).get(y + 1).setEnabled(true);
				gameWindow.gridButtonList.get(x).get(y + 1).setBackground(Color.white);
			}

		}

		if (x == 0 && y != 0 && y < clientScrabble.BoardSize - 1) {
			if (gameWindow.gridButtonList.get(x).get(y - 1).getButtonState() == button.NOT_ASSIGNED) {
				gameWindow.gridButtonList.get(x).get(y - 1).setEnabled(true);
				gameWindow.gridButtonList.get(x).get(y - 1).setBackground(Color.white);
			}
			if (gameWindow.gridButtonList.get(x).get(y + 1).getButtonState() == button.NOT_ASSIGNED) {
				gameWindow.gridButtonList.get(x).get(y + 1).setEnabled(true);
				gameWindow.gridButtonList.get(x).get(y + 1).setBackground(Color.white);
			}
			if (gameWindow.gridButtonList.get(x + 1).get(y).getButtonState() == button.NOT_ASSIGNED) {
				gameWindow.gridButtonList.get(x + 1).get(y).setEnabled(true);
				gameWindow.gridButtonList.get(x + 1).get(y).setBackground(Color.white);
			}

		}
		if (x != 0 && y == 0 && x < clientScrabble.BoardSize - 1) {
			if (gameWindow.gridButtonList.get(x - 1).get(y).getButtonState() == button.NOT_ASSIGNED) {
				gameWindow.gridButtonList.get(x - 1).get(y).setEnabled(true);
				gameWindow.gridButtonList.get(x - 1).get(y).setBackground(Color.white);
			}
			if (gameWindow.gridButtonList.get(x + 1).get(y).getButtonState() == button.NOT_ASSIGNED) {
				gameWindow.gridButtonList.get(x + 1).get(y).setEnabled(true);
				gameWindow.gridButtonList.get(x + 1).get(y).setBackground(Color.white);
			}
			if (gameWindow.gridButtonList.get(x).get(y + 1).getButtonState() == button.NOT_ASSIGNED) {
				gameWindow.gridButtonList.get(x).get(y + 1).setEnabled(true);
				gameWindow.gridButtonList.get(x).get(y + 1).setBackground(Color.white);
			}
		}
		if (x == clientScrabble.BoardSize - 1 && y != 0 && y < clientScrabble.BoardSize - 1) {

			if (gameWindow.gridButtonList.get(x - 1).get(y).getButtonState() == button.NOT_ASSIGNED) {
				gameWindow.gridButtonList.get(x - 1).get(y).setEnabled(true);
				gameWindow.gridButtonList.get(x - 1).get(y).setBackground(Color.white);
			}
			if (gameWindow.gridButtonList.get(x).get(y - 1).getButtonState() == button.NOT_ASSIGNED) {
				gameWindow.gridButtonList.get(x).get(y - 1).setEnabled(true);
				gameWindow.gridButtonList.get(x).get(y - 1).setBackground(Color.white);
			}
			if (gameWindow.gridButtonList.get(x).get(y + 1).getButtonState() == button.NOT_ASSIGNED) {
				gameWindow.gridButtonList.get(x).get(y + 1).setEnabled(true);
				gameWindow.gridButtonList.get(x).get(y + 1).setBackground(Color.white);
			}

		}
		if (x != 0 && y == clientScrabble.BoardSize - 1 && x < clientScrabble.BoardSize - 1) {

			if (gameWindow.gridButtonList.get(x - 1).get(y).getButtonState() == button.NOT_ASSIGNED) {
				gameWindow.gridButtonList.get(x - 1).get(y).setEnabled(true);
				gameWindow.gridButtonList.get(x - 1).get(y).setBackground(Color.white);
			}
			if (gameWindow.gridButtonList.get(x + 1).get(y).getButtonState() == button.NOT_ASSIGNED) {
				gameWindow.gridButtonList.get(x + 1).get(y).setEnabled(true);
				gameWindow.gridButtonList.get(x + 1).get(y).setBackground(Color.white);
			}
			if (gameWindow.gridButtonList.get(x).get(y - 1).getButtonState() == button.NOT_ASSIGNED) {
				gameWindow.gridButtonList.get(x).get(y - 1).setEnabled(true);
				gameWindow.gridButtonList.get(x).get(y - 1).setBackground(Color.white);
			}

		}
		if (x == 0 && y == 0) {
			if (gameWindow.gridButtonList.get(x + 1).get(y).getButtonState() == button.NOT_ASSIGNED) {
				gameWindow.gridButtonList.get(x + 1).get(y).setEnabled(true);
				gameWindow.gridButtonList.get(x + 1).get(y).setBackground(Color.white);
			}
			if (gameWindow.gridButtonList.get(x).get(y + 1).getButtonState() == button.NOT_ASSIGNED) {
				gameWindow.gridButtonList.get(x).get(y + 1).setEnabled(true);
				gameWindow.gridButtonList.get(x).get(y + 1).setBackground(Color.white);
			}

		}
		if (x == clientScrabble.BoardSize - 1 && y == 0) {
			if (gameWindow.gridButtonList.get(x + 1).get(y).getButtonState() == button.NOT_ASSIGNED) {
				gameWindow.gridButtonList.get(x + 1).get(y).setEnabled(true);
				gameWindow.gridButtonList.get(x + 1).get(y).setBackground(Color.white);
			}
			if (gameWindow.gridButtonList.get(x).get(y + 1).getButtonState() == button.NOT_ASSIGNED) {
				gameWindow.gridButtonList.get(x).get(y + 1).setEnabled(true);
				gameWindow.gridButtonList.get(x).get(y + 1).setBackground(Color.white);
			}

		}
		if (x == clientScrabble.BoardSize - 1 && y == clientScrabble.BoardSize - 1) {
			if (gameWindow.gridButtonList.get(x - 1).get(y).getButtonState() == button.NOT_ASSIGNED) {
				gameWindow.gridButtonList.get(x - 1).get(y).setEnabled(true);
				gameWindow.gridButtonList.get(x - 1).get(y).setBackground(Color.white);
			}
			if (gameWindow.gridButtonList.get(x).get(y - 1).getButtonState() == button.NOT_ASSIGNED) {
				gameWindow.gridButtonList.get(x).get(y - 1).setEnabled(true);
				gameWindow.gridButtonList.get(x).get(y - 1).setBackground(Color.white);
			}

		}
		if (x == 0 && y == clientScrabble.BoardSize - 1) {
			if (gameWindow.gridButtonList.get(x - 1).get(y).getButtonState() == button.NOT_ASSIGNED) {
				gameWindow.gridButtonList.get(x - 1).get(y).setEnabled(true);
				gameWindow.gridButtonList.get(x - 1).get(y).setBackground(Color.white);
			}
			if (gameWindow.gridButtonList.get(x).get(y - 1).getButtonState() == button.NOT_ASSIGNED) {
				gameWindow.gridButtonList.get(x).get(y - 1).setEnabled(true);
				gameWindow.gridButtonList.get(x).get(y - 1).setBackground(Color.white);
			}

		}
                
		gridButtonGarbageCollector();
                return 1;
	}

	public void gridButtonGarbageCollector() {
		for (int i = 0; i < gameWindow.boardSize; i++) {
			for (int j = 0; j < gameWindow.boardSize; j++) {
				if (gameWindow.gridButtonList.get(i).get(j).isEnabled() == true) {
					if (gameWindow.gridButtonList.get(i).get(j).getLetter() != ' '
							|| gameWindow.gridButtonList.get(i).get(j).getButtonState() == GridButton.NOT_ASSIGNED) {
						// gameWindow.letterTemp = ' ';
						gameWindow.gridButtonList.get(i).get(j).setLetter(' ');
						gameWindow.gridButtonList.get(i).get(j).setButtonState(GridButton.NOT_ASSIGNED);
					}
				}

			}

		}
	}

	public ArrayList<String> wordIdentifier(GridButton button) {
		int x = button.getPositionRow();
		int y = button.getPositionColumn();
		ArrayList<Character> word1Temp = new ArrayList<Character>();
		ArrayList<Character> word2Temp = new ArrayList<Character>();
		ArrayList<String> words = new ArrayList<String>();
		String word = "";
                
//                gameWindow.word1X1coordinate=0;
//                gameWindow.word1X2coordinate=0;
//                gameWindow.word1Y1coordinate=0;
//                gameWindow.word1Y2coordinate=0;
//                gameWindow.word2X1coordinate=0;
//                gameWindow.word2X2coordinate=0;
//                gameWindow.word2Y1coordinate=0;
//                gameWindow.word2Y2coordinate=0;
                


		gameWindow.gridButtonList.get(x).get(y).setButtonState(button.ASSIGNED);

		if (x >= 0 && y >= 0 && x <= clientScrabble.BoardSize - 1 && y <= clientScrabble.BoardSize - 1) {
			x = button.getPositionRow();
			y = button.getPositionColumn();
			while (gameWindow.gridButtonList.get(x).get(y).getLetter() != ' ' && x >= 0) {
				x = x - 1;
				if (x == -1) {
					break;
				}
			}
			x = x + 1;
                        gameWindow.word1X1coordinate=x;
                        gameWindow.word1Y1coordinate=y;
                        gameWindow.word1Y2coordinate=y;
			while (gameWindow.gridButtonList.get(x).get(y).getLetter() != ' ' && x <= clientScrabble.BoardSize - 1) {
				word1Temp.add(gameWindow.gridButtonList.get(x).get(y).getLetter());
				x = x + 1;
                                gameWindow.word1X2coordinate=x-1;
				if (x == clientScrabble.BoardSize) {
                                        gameWindow.word1X2coordinate=x-1;
					break;
				}
                                
			}
			StringBuilder sb1 = new StringBuilder();
			for (Character s : word1Temp) {
				sb1.append(s);
			}
			word = sb1.toString();
			// System.out.println(word);
			words.add(word);

			// end check up to down
			// start check left to right
			x = button.getPositionRow();
			y = button.getPositionColumn();

			while (gameWindow.gridButtonList.get(x).get(y).getLetter() != ' ' && y >= 0) {
				y = y - 1;
				if (y == -1) {
					break;
				}
			}
			y = y + 1;
                        gameWindow.word2Y1coordinate=y;
                        gameWindow.word2X1coordinate=x;
                        gameWindow.word2X2coordinate=x;
			while (gameWindow.gridButtonList.get(x).get(y).getLetter() != ' ' && y <= clientScrabble.BoardSize - 1) {
				word2Temp.add(gameWindow.gridButtonList.get(x).get(y).getLetter());
				y = y + 1;
                                gameWindow.word2Y2coordinate=y-1;
				if (y == clientScrabble.BoardSize) {
                                        gameWindow.word2Y2coordinate=y-1;
					break;
				}
			}
			StringBuilder sb2 = new StringBuilder();
			for (Character s : word2Temp) {
				sb2.append(s);
			}
			word = sb2.toString();
			// System.out.println(word);
			words.add(word);

		}
                          try {
                ((Client) client).game.updateGreenList(
                        gameWindow.word1X1coordinate,
                        gameWindow.word1X2coordinate,
                        gameWindow.word1Y1coordinate,
                        gameWindow.word1Y2coordinate,
                        gameWindow.word2X1coordinate,
                        gameWindow.word2X2coordinate,
                        gameWindow.word2Y1coordinate,
                        gameWindow.word2Y2coordinate);
            } catch (RemoteException ex) {
                Logger.getLogger(GameMainController.class.getName()).log(Level.SEVERE, null, ex);
            }
		return words;
	}

	public int requestVoting(ArrayList<String> s) {
		ArrayList<String> votingWords = new ArrayList<>();
		ArrayList<String> words = s;
		int score = 0;
		for (String temp : words) {
//			JFrame jf = new JFrame();
//			jf.setAlwaysOnTop(true);
			int option = JOptionPane.showConfirmDialog(this.gameWindow,
					"Do you want to request voting for this word: " + temp + " ?", "Select an Option...",
					JOptionPane.YES_NO_OPTION);
			if (option == 0) {
				votingWords.add(temp);
				System.out.println("word added to the list for voting");
			} else {
                                votingWords.add("#");
//				continue;
			}

		}
                 System.out.println("ask vote for " + votingWords.get(0) + " and " + votingWords.get(1));
		try {
            score = ((Client) client).game.Vote(((Client) client).player.getUsername(), votingWords);
		} catch (RemoteException ex) {
			Logger.getLogger(GameMainController.class.getName()).log(Level.SEVERE, null, ex);
		}
//            try {
//					score = ((Client) client).game.scoreCalculation(((Client) client).player.getUsername(), temp);
//				} catch (RemoteException ex) {
//					Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
//				}
//				System.out.println("The score is:" + score);

		return score;// call for vote method to send data to the server ---> voteMethod();
	}

	public boolean registerUser(String userName, String password) {
		try {
			Boolean flag;

			Player player = new Player();
			player.setUsername(userName);

			flag = gameServer.registerUser(userName, password);
			if (flag == true) {
				gameWindow.mainMenuStartButton.setEnabled(true);
				gameWindow.jButtonAskGame.setEnabled(true);
				gameWindow.chatBoxSendButton.setEnabled(true);

				client = new Client(gameServer, registry, gameWindow, player);

				int s = gameServer.register(client, ((Client) client).player.getUsername());

				if (s == IGameServer.FAILURE) {
					System.out.println("1. Couldn't send client object to server");
					System.exit(1);
				} else {
					System.out.println("1. Success sending ClientObject to server");
					gameWindow.mainMenuLoginButton.setEnabled(false);
				}

			} else {
				JOptionPane.showMessageDialog(gameWindow, "The User currently exists in the database..Error");
				return false;
			}

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	/*
	 * public boolean startGame() { try { ((Client) client).StartGame(); } catch
	 * (RemoteException e) { // TODO Auto-generated catch block e.printStackTrace();
	 * }
	 * 
	 * System.out.println(((IClientGame) client).GetGameName()); return true; }
	 */
	public boolean invite(ArrayList<String> inviteList) {
		try {
			((Client) client).StartGame(inviteList);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(((IClientGame) client).GetGameName());
		return true;
	}

//    public void playerTurnLabelHandler(JButton labelButton){
//        if(player.getTurn()==false){
//            labelButton.setBackground(Color.red);
//        }
//        else{
//            labelButton.setBackground(Color.GREEN);
//        }
	// }
	public boolean loginUser(String userName, String password) {
		Boolean flag2 = false;
		Player player = new Player();
		try {
			Boolean flag;

			player.setUsername(userName);

			flag = gameServer.login(userName, password);
			if (flag == true) {
				System.out.println("In the gameMainController in login user in IF");
				gameWindow.mainMenuStartButton.setEnabled(true);
				gameWindow.jButtonAskGame.setEnabled(true);
				gameWindow.chatBoxSendButton.setEnabled(true);

				player.setUsername(userName);
				player.setPassword(password);
				// client = new Client(gameServer, registry, gameWindow, player);
				// s = gameServer.register(client, ((Client) client).player.getUsername());

				flag2 = true;

				client = new Client(gameServer, registry, gameWindow, player);

				int s = gameServer.register(client, ((Client) client).player.getUsername());

				if (s == IGameServer.FAILURE) {
					System.out.println("1. Couldn't send client object to server");
					System.exit(1);
				} else {
					System.out.println("1. Success sending ClientObject to server");
					gameWindow.mainMenuLoginButton.setEnabled(false);
				}

			} else {
				JOptionPane.showMessageDialog(gameWindow, "The User is not in the database or password is incorrect");
				flag2 = false;
			}

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag2;
	}

	public void endTurn(TurnData turnData) {
		try {
			((Client) client).game.endTurn(((Client) client).player.getUsername(), turnData);
		} catch (RemoteException ex) {
			Logger.getLogger(GameMainController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void quit() {
		if (client != null) {
			((Client) client).Quit();
		}
	}

	public String getClientName() throws RemoteException {
		return client.getPlayerName();
	}

	public boolean broadcastChat(String userName, String msg) {
		try {
//			gameServer.registerUser(userName, password);

			Player player = new Player();
			player.setUsername(userName);

//			client = new Client(gameServer, registry, gameWindow, player);
//			int s = gameServer.register(client, ((Client) client).player.getUsername());
			int s = gameServer.broadcastMsg(client, userName, msg);

			if (s == IGameServer.FAILURE) {
				System.out.println("1. Couldn't send client object to server");
				System.exit(1);
			} else {
				System.out.println("1. Success sending ClientObject to server");
			}

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
        public void endGame(String userName) {
            try {
                ((Client) client).game.endGame(userName);
            } catch (RemoteException ex) {
                Logger.getLogger(GameMainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        public void ConsecutivePassHandler (){
            try {
                ((Client) client).game.setConsecutivePasses();
            } catch (RemoteException ex) {
                Logger.getLogger(GameMainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

}
