package server;

import clientscrabble.Client;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import dataObjects.StringListStringTuple;
import dataObjects.TurnData;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import remote.IGame;
import remote.IGameServer;
import remote.IClientCallback;

/**
 * Server side implementation of the remote interface. Must extend
 * UnicastRemoteObject, to allow the JVM to create a remote proxy/stub.
 *
 */
// This class is the waiting room and handles the registration of client objects.
public class GameServer extends UnicastRemoteObject implements IGameServer {
    // Player player = new Player();

    //private int numberOfComputations;
    SynchronizedFunctions syncfunc = new SynchronizedFunctions();

    // The queue of players which are not in any game and are ready to start
    // playing. This is synonymous with "Pool"
    private List<ClientPlayerTuple<IClientCallback, Player>> queue = new ArrayList<ClientPlayerTuple<IClientCallback, Player>>();
    // private List<IClientCallback> clients = new ArrayList<IClientCallback>();
    // The list of ongoing games
    private List<IGame> games = new ArrayList<IGame>();
    RMIServer server = new RMIServer();
    // private Registry registry;

    private ServerWindow serverWindow;
    public Object callbackObj;

    // Registers the client object at the server. needs to be called by the client
    // after login.
    public int register(Object callbackObj, String userName) // throws RemoteException
    {
        Boolean flag = false;
        if (callbackObj == null) {
            return IGameServer.FAILURE;
        }
        Player newPlayer = new Player();

        newPlayer.setUsername(userName);
        newPlayer.setTimeStamp(new Timestamp(System.currentTimeMillis()));
        newPlayer.setActive(true);

        // System.out.println("in the Game SErver in the if method");
        queue.add(new ClientPlayerTuple<IClientCallback, Player>((IClientCallback) callbackObj, newPlayer));

        serverWindow.jTextArea1.append("Registering new Client Object. UserName: " + userName + "\n");

        updateQueue();

        /*
		 * for (ClientPlayerTuple<IClientCallback, Player> tuple : queue) { //
		 * userNames[i] = tuple.player.getUsername(); try {
		 * tuple.client.UpdateQueue(userNames); } catch (RemoteException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } // i=i+1; }
         */
        // clients.add((IClientCallback)callbackObj);
        // }
        return IGameServer.SUCCESS;
    }

    private void updateQueue() {
        String[] userNames = new String[queue.size()];
        int i = 0;

        // serverWindow.jTextArea1.append("Queue length: " + queue.size() + "\n");
        for (ClientPlayerTuple<IClientCallback, Player> tuple : queue) {
            userNames[i] = tuple.player.getUsername();
            // tuple.client.UpdateQueue();
            i = i + 1;
        }

        List<Callable<UserNameBooleanTuple>> updateQueueTasks = new ArrayList<Callable<UserNameBooleanTuple>>();
        for (ClientPlayerTuple<IClientCallback, Player> tuple : queue) {
            UpdateQueueTask task = new UpdateQueueTask(tuple.client, userNames, tuple.player.getUsername());
            updateQueueTasks.add(task);
        }

        doUpdateQueueTasks(updateQueueTasks, userNames);
    }

    private boolean doUpdateQueueTasks(List<Callable<UserNameBooleanTuple>> updateQueueTasks, String[] userNames) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        List<Future<UserNameBooleanTuple>> results = forkJoinPool.invokeAll(updateQueueTasks);

        List<String> retries = new ArrayList<String>();

        for (Future<UserNameBooleanTuple> result : results) {
            try {
                if (!result.get().bool) {
                    retries.add(result.get().userName);
                }
            } catch (InterruptedException | ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (retries.size() > 0) {
            List<Callable<UserNameBooleanTuple>> retryTasks = new ArrayList<Callable<UserNameBooleanTuple>>();
            for (String failedUser : retries) {
                retryTasks.add(updateQueueTasks.stream()
                        .filter(task -> failedUser.equals(((UpdateQueueTask) task).userName)).findAny().orElse(null));
            }
            return doUpdateQueueTasks(retryTasks, userNames);
        }
        return true;
    }

//	protected GameServer(Registry registry) throws RemoteException {
//		numberOfComputations = 0;
//		this.registry = registry;
//	}
    public GameServer(ServerWindow serverWindow) throws RemoteException {
        this.serverWindow = serverWindow;
        //numberOfComputations = 0;
    }

    /*
	 * @Override public double add(double a, double b) throws RemoteException {
	 * numberOfComputations++;
	 * System.out.println("Number of computations performed so far = " +
	 * numberOfComputations);
	 *
	 * if (clients.size() == 0 || !(clients.get(0) instanceof IClientCallback))
	 * System.out.
	 * println("ClientObject is not instance of CallbackClientIntf at server side");
	 *
	 * // client call_back object if (((IClientCallback)clients.get(0)).Vote("abc"))
	 * System.out.println("Vote"); else
	 * System.out.println("fail on send dataobject to server");
	 *
	 * return (a+b); }
	 *
     */
    // Start a game with currently queued players
    @Override
    public StringListStringTuple startGame(String userName, List<String> inviteList) throws RemoteException {
        List<ClientPlayerTuple<IClientCallback, Player>> invitedPlayers = new ArrayList<ClientPlayerTuple<IClientCallback, Player>>();

        invitedPlayers.add(queue.stream().filter(pl -> pl.player.getUsername().equals(userName)).findFirst().get());

        invitedPlayers.addAll(queue
                .stream().filter(pl -> (inviteList.indexOf(pl.player.getUsername()) >= 0
                && !pl.player.getUsername().equals(userName))).collect(Collectors.toList()));

        List<ClientPlayerTuple<IClientCallback, Player>> acceptances = invitePlayers(invitedPlayers, userName);

        List<ClientPlayerTuple<IClientCallback, Player>> notAccepted = new ArrayList<ClientPlayerTuple<IClientCallback, Player>>(invitedPlayers);
        notAccepted.removeAll(acceptances);

        invitedPlayers.removeAll(notAccepted);

        IGame game = new Game("Game1", acceptances, serverWindow, userName);
        String gameUrl = "";
        // game.
        games.add(game);
        try {
            gameUrl = bindGame(game);
        } catch (AlreadyBoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        startGameForOtherClients(userName, gameUrl, invitedPlayers);

        // queue.clear();
        return new StringListStringTuple(gameUrl, invitedPlayers.stream().map(pl -> pl.player.getUsername()).collect(Collectors.toList()));
    }

    private List<ClientPlayerTuple<IClientCallback, Player>> invitePlayers(
            List<ClientPlayerTuple<IClientCallback, Player>> invitedPlayers, String inviter) {
        List<Callable<UserNameBooleanTuple>> invitePlayerTasks = new ArrayList<Callable<UserNameBooleanTuple>>();
        for (ClientPlayerTuple<IClientCallback, Player> tuple : invitedPlayers) {
            if (tuple.player.getUsername().equals(inviter)) {
                continue;
            }
            InvitePlayerTask task = new InvitePlayerTask(tuple.client, inviter, tuple.player.getUsername());
            invitePlayerTasks.add(task);
        }

        List<String> acceptances = doInvitePlayersTasks(invitePlayerTasks, null);

        return (List<ClientPlayerTuple<IClientCallback, Player>>) queue.stream().filter(
                pl -> (acceptances.indexOf(pl.player.getUsername()) >= 0) || pl.player.getUsername().equals(inviter))
                .collect(Collectors.toList());

    }

    private List<String> doInvitePlayersTasks(List<Callable<UserNameBooleanTuple>> invitePlayerTasks,
            List<String> acceptances) {

        if (acceptances == null) {
            acceptances = new ArrayList<String>();
        }

        ForkJoinPool forkJoinPool = new ForkJoinPool();

        List<Future<UserNameBooleanTuple>> results = forkJoinPool.invokeAll(invitePlayerTasks);

        List<String> retries = new ArrayList<String>();

        for (Future<UserNameBooleanTuple> result : results) {
            try {
                if (!result.get().bool) {
                    retries.add(result.get().userName);
                } else {
                    acceptances.add(result.get().userName);
                }
            } catch (InterruptedException | ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (retries.size() > 0) {
            List<Callable<UserNameBooleanTuple>> retryTasks = new ArrayList<Callable<UserNameBooleanTuple>>();
            for (String failedUser : retries) {
                retryTasks.add(invitePlayerTasks.stream()
                        .filter(task -> failedUser.equals(((UpdateQueueTask) task).userName)).findAny().orElse(null));
            }
            return doInvitePlayersTasks(retryTasks, acceptances);
        }
        return acceptances;
    }

    public boolean startGameForOtherClients(String userName, String gameUrl,
            List<ClientPlayerTuple<IClientCallback, Player>> acceptances) {
        List<Callable<UserNameBooleanTuple>> startGameTasks = new ArrayList<Callable<UserNameBooleanTuple>>();
        ArrayList<String> players = (ArrayList<String>) acceptances.stream().map(acc -> acc.player.getUsername())
                .collect(Collectors.toList());
        for (ClientPlayerTuple<IClientCallback, Player> tuple : acceptances) {
            if (tuple.player.getUsername().equals(userName)) {
                continue;
            }
            StartGameTask task = new StartGameTask(tuple.client, gameUrl, tuple.player.getUsername(), players);
            startGameTasks.add(task);
        }

        return doStartGameTasks(startGameTasks);
    }

    private boolean doStartGameTasks(List<Callable<UserNameBooleanTuple>> updateQueueTasks) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        List<Future<UserNameBooleanTuple>> results = forkJoinPool.invokeAll(updateQueueTasks);

        List<String> retries = new ArrayList<String>();

        for (Future<UserNameBooleanTuple> result : results) {
            try {
                if (!result.get().bool) {
                    retries.add(result.get().userName);
                }
            } catch (InterruptedException | ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (retries.size() > 0) {
            List<Callable<UserNameBooleanTuple>> retryTasks = new ArrayList<Callable<UserNameBooleanTuple>>();
            for (String failedUser : retries) {
                retryTasks.add(updateQueueTasks.stream()
                        .filter(task -> failedUser.equals(((UpdateQueueTask) task).userName)).findAny().orElse(null));
            }
            return doStartGameTasks(retryTasks);
        }
        return true;
    }

    private String bindGame(IGame game) throws RemoteException, AlreadyBoundException {
        String gameUrl = "";
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            gameUrl = "rmi://" + inetAddress + "/" + game.getGameName();
            serverWindow.jTextArea1.append("Game URL: " + gameUrl + "\n");
            Naming.rebind(gameUrl, game);
            // registry.bind(game.getGameName(), game);
        } catch (MalformedURLException ex) {
            Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return gameUrl;
    }

    // Method used by the client object to cast a vote
    @Override
    public boolean registerUser(String userName, String password) throws RemoteException {
        // TODO Auto-generated method stub
        Boolean flag = false;
        String result = "";
        try {
            flag = syncfunc.query(userName);
            // System.out.println(flag);
        } catch (SQLException ex) {
            Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (flag == true) {

            Player newPlayer = new Player();
            newPlayer.setUsername(userName);
            newPlayer.setPassword(password);
            newPlayer.setActive(true);
            try {
                result = syncfunc.Register(userName, password);
                serverWindow.jTextArea1.append(result + "\n");

            } catch (SQLException ex) {
                Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            serverWindow.jTextArea1.append("The User currently exists in the database" + "\n");

            flag = false;
        }
        return flag;
    }

    @Override
    public boolean login(String userName, String password) throws RemoteException {
        // TODO Auto-generated method stub
        Boolean flag = true;
        String result = "";
        Player player = new Player();
        try {
            System.out.println("in the gameserver login");
            flag = syncfunc.query(userName);
            System.out.println(flag);
        } catch (SQLException ex) {
            Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (flag == false) {
            try {
                // if the username found in the database
                // if (player.getActive() == false) {
                result = syncfunc.query1(userName);
                if (result.equals(password)) {

                    //Player player = new Player();
                    player.setUsername(userName);
                    player.setPassword(password);
                    player.setTimeStamp(new Timestamp(System.currentTimeMillis()));
                    if (player.getActive() == false) {
                        player.setActive(true);
                        serverWindow.jTextArea1.append("The User" + userName + "logged in to the game" + "\n");

                        updateQueue();

                        return true;
                    } else {
                        player.setActive(false);
                        serverWindow.jTextArea1.append("The User" + userName + "currently exists in the game" + "\n");
                    }

                }
                //  }
            } catch (SQLException ex) {
                Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return false;
    }

    @Override
    public boolean quit(String userName) throws RemoteException {
        removeUserFromQueue(userName);
        removeUserFromGames(userName);
        return false;
    }

    private void removeUserFromGames(String userName) {
        for (IGame game : games) {
            ((Game) game).removeUserFromGame(userName);
        }

    }

    private void removeUserFromQueue(String userName) {
        boolean hasRemovedUser = false;
        Iterator<ClientPlayerTuple<IClientCallback, Player>> i = queue.iterator();
        while (i.hasNext()) {
            ClientPlayerTuple<IClientCallback, Player> tuple = i.next();
            if (tuple.player.getUsername().equals(userName)) {
                i.remove();
                hasRemovedUser = true;
                break;
            }
        }
        if (hasRemovedUser) {
            updateQueue();
        }
    }

    @Override
    public synchronized int broadcastMsg(Object callbackObj, String userName, String msg) throws RemoteException {
        System.out.println(userName + " says " + msg);

        String[] userNames = new String[queue.size()];
        int i = 0;
        for (ClientPlayerTuple<IClientCallback, Player> tuple : queue) {
            userNames[i] = tuple.player.getUsername();
            // tuple.client.UpdateQueue();
            i = i + 1;
        }

        List<Callable<UserNameBooleanTuple>> updateQueueTasks = new ArrayList<Callable<UserNameBooleanTuple>>();
        for (ClientPlayerTuple<IClientCallback, Player> tuple : queue) {
            UpdateChatboxTask task = new UpdateChatboxTask(tuple.client, msg, userName);
//			UpdateQueueTask task = new UpdateQueueTask(tuple.client, userNames, tuple.player.getUsername());
            updateQueueTasks.add(task);
        }
        updateCbox(updateQueueTasks, userNames);

        return IGameServer.SUCCESS;
    }

    private boolean updateCbox(List<Callable<UserNameBooleanTuple>> updateQueueTasks, String[] userNames) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        List<Future<UserNameBooleanTuple>> results = forkJoinPool.invokeAll(updateQueueTasks);

        List<String> retries = new ArrayList<String>();

        for (Future<UserNameBooleanTuple> result : results) {
            try {
                if (!result.get().bool) {
                    retries.add(result.get().userName);
                }
            } catch (InterruptedException | ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (retries.size() > 0) {
            List<Callable<UserNameBooleanTuple>> retryTasks = new ArrayList<Callable<UserNameBooleanTuple>>();
            for (String failedUser : retries) {
                retryTasks.add(updateQueueTasks.stream()
                        .filter(task -> failedUser.equals(((UpdateQueueTask) task).userName)).findAny().orElse(null));
            }
            return updateCbox(retryTasks, userNames);
        }
        return true;
    }

}
