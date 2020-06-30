package server;

import dataObjects.TurnData;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import remote.IClientCallback;

import remote.IGame;

// This class has all the methods which are called when the game is in progress
public class Game extends UnicastRemoteObject implements IGame {

    // All games will have a unique name
    private String name;
    // List<Player> players = new ArrayList<Player>();
    // the list of players in a game.
    private List<ClientPlayerTuple<IClientCallback, Player>> players = new ArrayList<ClientPlayerTuple<IClientCallback, Player>>();
    // Player player = new Player();
    private int currentTurn;
    public int consecutivePasses = 0;

    RMIServer server = new RMIServer();
    ServerWindow serverWindow;

    protected Game(String name, List<ClientPlayerTuple<IClientCallback, Player>> players, ServerWindow serverWindow,
            String userName) throws RemoteException {
        this.name = name;
        this.players.addAll(players);
        this.serverWindow = serverWindow;
        SetTurns(userName);
        // TODO Auto-generated constructor stub
    }

    private void SetTurns(String userName) {
        currentTurn = 0;
        int firstPlayerIndex = 0;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) != null && players.get(i).player.getUsername().equals(userName)) {
                firstPlayerIndex = i;
                players.get(i).player.setIsTurn(true);
            }
        }
        List<ClientPlayerTuple<IClientCallback, Player>> copy;
        if (firstPlayerIndex >= 0) {
            copy = new ArrayList<ClientPlayerTuple<IClientCallback, Player>>(players.size());
            copy.add(players.get(firstPlayerIndex));
            copy.addAll(players.subList(0, firstPlayerIndex));
            copy.addAll(players.subList(firstPlayerIndex + 1, players.size()));
            players = copy;
        }
    }

    @Override
    public String getGameName() throws RemoteException {
        // TODO Auto-generated method stub
        return name;
    }

    @Override
    public void endTurn(String userName, TurnData turnData) throws RemoteException {
        this.word1Accepted = false;
        this.word2Accepted = false;
        currentTurn = currentTurn + 1;
        for (ClientPlayerTuple<IClientCallback, Player> tuple : players) {
            if (((Player) tuple.player).getUsername().equals(userName)) {
                ((Player) tuple.player).setTurn(false);
                server.matrice.get(turnData.row).get(turnData.column).setLetter(turnData.letter);
                System.out.println("positionRow" + turnData.row + " PositionColum" + turnData.column + " is set to :"
                        + turnData.letter);
                serverWindow.jTextArea1.append("\n" + "positionRow" + turnData.row + " PositionColum" + turnData.column
                        + " is set to :" + turnData.letter + "\n");
                changeTurnForOtherClients(turnData);
                break;
            }
        }
    }

    public void changeTurnForOtherClients(TurnData turnData) {
        List<Callable<UserNameBooleanTuple>> changeTurnTasks = new ArrayList<Callable<UserNameBooleanTuple>>();
        //boolean changeTurnForThisPlayer = false;
        turnData.nextUser = players.get(currentTurn % players.size()).player.getUsername();
        int i = 0;
        for (ClientPlayerTuple<IClientCallback, Player> tuple : players) {
            TurnData turnDataToBeSent;
            if (i == currentTurn % players.size()) {
                turnDataToBeSent = new TurnData(turnData);
                tuple.player.setIsTurn(true);
                turnDataToBeSent.isTurn = true;
            } else {
                turnDataToBeSent = turnData;
                turnDataToBeSent.isTurn = false;
            }
            ChangeTurnTask task = new ChangeTurnTask(turnDataToBeSent, tuple.client, tuple.player.getUsername());
            changeTurnTasks.add(task);
            i = i + 1;
        }

        doChangeTurnTasks(changeTurnTasks);
    }

    private boolean doChangeTurnTasks(List<Callable<UserNameBooleanTuple>> updateQueueTasks) {
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
            return doChangeTurnTasks(retryTasks);
        }
        return true;
    }

    public void sendUserExitMessageToUsers(String userName) {
        List<Callable<UserNameBooleanTuple>> userExitMessageTasks = new ArrayList<Callable<UserNameBooleanTuple>>();
        for (ClientPlayerTuple<IClientCallback, Player> tuple : players) {
            int i = 0;
            ClientExitedMessageTask task;
            //if (!tuple.player.getUsername().equals(userName)) {
            task = new ClientExitedMessageTask(tuple.client, userName);
            System.out.println(i);
            userExitMessageTasks.add(task);
            i++;
            //}
        }

        doUpdateScoreTasks(userExitMessageTasks);
    }

    private boolean doSendUserExitMessageToUsers(List<Callable<UserNameBooleanTuple>> userExitMessageTasks) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        List<Future<UserNameBooleanTuple>> results = forkJoinPool.invokeAll(userExitMessageTasks);

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
                retryTasks.add(userExitMessageTasks.stream()
                        .filter(task -> failedUser.equals(((UpdateQueueTask) task).userName)).findAny().orElse(null));
            }
            return doChangeTurnTasks(retryTasks);
        }
        return true;
    }

    @Override
    public void exit(String userName) {
        // TODO Auto-generated method stub
        ListIterator<ClientPlayerTuple<IClientCallback, Player>> it = players.listIterator();
        while (it.hasNext()) {
            if (it.next().player.getUsername().equals(userName)) {
                it.remove();
                break;
            }
        }
    }

    public void removeUserFromGame(String userName) {
        Iterator<ClientPlayerTuple<IClientCallback, Player>> i = players.iterator();
        while (i.hasNext()) {
            ClientPlayerTuple<IClientCallback, Player> tuple = i.next();
            if (tuple.player.getUsername().equals(userName)) {
                i.remove();
            }
        }
        sendUserExitMessageToUsers(userName);
    }
    boolean word1Accepted, word2Accepted;

    //@Override
    public int scoreCalculation(String userName, VoteResultTuple voteResult, ArrayList<String> words) throws RemoteException {
//        word1Accepted = false;
//        word2Accepted = false;
        int scoreAddition = 0;
        int finalScore = 0;
        if (voteResult.word1Count == (players.size() - 1)) {
            System.out.println(voteResult.word1Count + " clients says okay about word1");
            scoreAddition = scoreAddition + words.get(0).length();
//            this.word1Accepted = true;

        }
        if (voteResult.word2Count == (players.size() - 1)) {
            System.out.println(voteResult.word2Count + " clients says okay about word2");
            scoreAddition = scoreAddition + words.get(1).length();
//            this.word2Accepted = true;
        }
        for (ClientPlayerTuple<IClientCallback, Player> tuple : players) {
            if (tuple.player.getUsername().equals(userName)) {
                tuple.player.setScore(tuple.player.getScore() + scoreAddition);
                finalScore = tuple.player.getScore();
                break;
            }
        }
        return finalScore;
        /*
		for (ClientPlayerTuple<IClientCallback, Player> tuple : players) {
			if (((Player) tuple.player).getUsername().equals(userName)) {
				((Player) tuple.player).setScore(score);
				if (((IClientCallback) tuple.client).Vote("abc")) {
					System.out.println("Vote");
				} else {
					System.out.println("fail on send dataobject to server");
				}
				return ((Player) tuple.player).getScore();
			}
		}
         */
    }

     @Override
    public int Vote(String userName, ArrayList words) throws RemoteException {
        word1Accepted=false;
        word2Accepted=false;
        int score = 0;
        try {
            System.out.println("I got " + words.get(0).toString() + " and " + words.get(1).toString());
            
            String word1 = "";
            String word2 = "";
            if(!words.get(0).toString().equals("#")){
                word1 = words.get(0).toString();
            }
            if(!words.get(1).toString().equals("#")){
                word2 = words.get(1).toString();
            }
            /*word1 = words.get(0).toString();
            //System.out.println("This is word1: " + word1);
            if (words.size() > 1) {
                word2 = words.get(1).toString();
                // System.out.println("This is word2: " + word2);
            }*/

            serverWindow.jTextArea1.append("Request for voting by UserName: " + userName + "\n");
            // serverWindow.jTextArea1.append("Number of players to vote: " +
            // (players.size() - 1) + "\n");

            List<Callable<UserNameVoteBooleanTuple>> votingTasks = new ArrayList<Callable<UserNameVoteBooleanTuple>>();
            for (ClientPlayerTuple<IClientCallback, Player> tuple : players) {
                //int i = 0;
                VotingTask task;
                if (!tuple.player.getUsername().equals(userName)) {
                    task = new VotingTask(tuple.client, word1, word2, userName);
                    //System.out.println(i);
                    votingTasks.add(task);
                    //i++;
                }
            }

            VoteResultTuple voteResult = doVote(votingTasks, 0, 0);
            score = scoreCalculation(userName, voteResult, words);

            broadCastScore(score, userName);
            
            if (voteResult.word1Count == (players.size() - 1)) {
                System.out.println(voteResult.word1Count + " clients says okay about word1");
//                scoreAddition = scoreAddition + words.get(0).length();
                this.word1Accepted = true;
            }
            if (voteResult.word2Count == (players.size() - 1)) {
                System.out.println(voteResult.word2Count + " clients says okay about word2");
//                scoreAddition = scoreAddition + words.get(1).length();
                this.word2Accepted = true;
            }
            
//            hihglightNewWord(this.word1Accepted, this.word2Accepted, userName);
            hihglightNewWord(this.word1Accepted,this.word2Accepted,greenList1,greenList2, userName);

            return score;
        } catch (Exception e) {
            serverWindow.jTextArea1.append("Request for voting by UserName: " + userName + " No words selected for the voting" + "\n");

        }
        return score;
    }

    private void broadCastScore(int score, String userName) {

        List<Callable<UserNameBooleanTuple>> updateScoreTasks = new ArrayList<Callable<UserNameBooleanTuple>>();

        for (ClientPlayerTuple<IClientCallback, Player> tuple : players) {
            int i = 0;
            UpdateScoreTask task;
            if (!tuple.player.getUsername().equals(userName)) {
                task = new UpdateScoreTask(tuple.client, score, userName);
                System.out.println(i);
                updateScoreTasks.add(task);
                i++;
            }
        }

        doUpdateScoreTasks(updateScoreTasks);

    }

//    private void broadCastAcceptetWords() {
//        List<Callable<UserNameBooleanTuple>> updateAcceptedWordsTasks = new ArrayList<Callable<UserNameBooleanTuple>>();
//        for (ClientPlayerTuple<IClientCallback, Player> tuple : players) {
//            int i = 0;
//            UpdateAcceptedWordsTask task;
//            if (!tuple.player.getUsername().equals(userName)) {
//                task = new UpdateScoreTask(tuple.client, score, userName);
//                System.out.println(i);
//                updateScoreTasks.add(task);
//                i++;
//            }
//        }
//    }
    private boolean doUpdateScoreTasks(List<Callable<UserNameBooleanTuple>> updateScoreTasks) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        List<Future<UserNameBooleanTuple>> results = forkJoinPool.invokeAll(updateScoreTasks);

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
                retryTasks.add(updateScoreTasks.stream()
                        .filter(task -> failedUser.equals(((UpdateScoreTask) task).userName)).findAny().orElse(null));
            }
            return doUpdateScoreTasks(retryTasks);
        }
        return true;
    }

    private VoteResultTuple doVote(List<Callable<UserNameVoteBooleanTuple>> votingTasks, int word1Count, int word2Count) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        List<Future<UserNameVoteBooleanTuple>> results = forkJoinPool.invokeAll(votingTasks);

        List<String> retries = new ArrayList<String>();

        for (Future<UserNameVoteBooleanTuple> result : results) {
            try {
                if (!result.get().success) {
                    retries.add(result.get().userName);
                } else {
                    if (result.get().voteBool1) {
                        word1Count = word1Count + 1;
                    }

                    if (result.get().voteBool2) {
                        word2Count = word2Count + 1;
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (retries.size() > 0) {
            List<Callable<UserNameVoteBooleanTuple>> retryTasks = new ArrayList<Callable<UserNameVoteBooleanTuple>>();
            for (String failedUser : retries) {
                retryTasks.add(votingTasks.stream().filter(task -> failedUser.equals(((VotingTask) task).userName))
                        .findAny().orElse(null));
            }
            return doVote(retryTasks, word1Count, word2Count);
        }
        return new VoteResultTuple(word1Count, word2Count);
    }

    @Override
    public int isWord(String aword) throws RemoteException {
        System.out.println("hi");
        String key = "5b3637ad-865f-485f-ae9b-fcd904ca0c94"; // the key from API, DO NOT CHANGE
        String req = "https://www.dictionaryapi.com/api/v1/references/sd2/xml/"
                + // GET request format, DO NOT CHANGE
                aword.trim() + "?key=" + key;
        try {
            URL url = new URL(req);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) { //200 means okay response from dictionary provider
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                if (response.toString().contains("<entry id")) { //means the response contain an entry from dictionary
                    return 1; //word exists
                } else {
                    return 0; // word not exists
                }
            } else {
                return -1; //bad response code from GET request
            }
        } catch (MalformedURLException ex) {
            return -2; //somebody changed the request URL or API key
        } catch (IOException ex) {
            return -3; //not connected to internet
        }
    }

    @Override
    public int endGame(String userName) throws RemoteException {

        // System.out.println("in the endGame in the server");
        consecutivePasses = consecutivePasses + 1;
        System.out.println(consecutivePasses);
        if (consecutivePasses == players.size()) {
//			gameOver();

            String[] userNames = new String[players.size()];
            int i = 0;
            for (ClientPlayerTuple<IClientCallback, Player> tuple : players) {
                userNames[i] = tuple.player.getUsername();
                // tuple.client.UpdateQueue();
                i = i + 1;
            }

            List<Callable<UserNamePassBooleanTuple>> gameOverTask = new ArrayList<Callable<UserNamePassBooleanTuple>>();
            for (ClientPlayerTuple<IClientCallback, Player> tuple : players) {
                GameOverTask task = new GameOverTask(tuple.client, userName);
                gameOverTask.add(task);

            }
            doEndGameupdate(gameOverTask, userNames);

            //JOptionPane.showMessageDialog(null, "Game Over, Every Participant Pressed Pass", "Exit", JOptionPane.INFORMATION_MESSAGE);
        }
        return consecutivePasses;
    }

    private boolean doEndGameupdate(List<Callable<UserNamePassBooleanTuple>> updateQueueTasks, String[] userNames) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        List<Future<UserNamePassBooleanTuple>> results = forkJoinPool.invokeAll(updateQueueTasks);

        List<String> retries = new ArrayList<String>();

        for (Future<UserNamePassBooleanTuple> result : results) {
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
            List<Callable<UserNamePassBooleanTuple>> retryTasks = new ArrayList<Callable<UserNamePassBooleanTuple>>();
            for (String failedUser : retries) {
                retryTasks.add(updateQueueTasks.stream()
                        .filter(task -> failedUser.equals(((GameOverTask) task).userName)).findAny().orElse(null));
            }
            return doEndGameupdate(retryTasks, userNames);
        }
        return true;
    }

    @Override
    public void setConsecutivePasses() {
        System.out.println("in the setConsecutivePasses MEthod");
        consecutivePasses = 0;
        System.out.println(consecutivePasses);

    }

    private void hihglightNewWord(boolean word1Accepted, boolean word2Accepted, List<int[]> greenList1, List<int[]> greenList2, String userName) {
        /*int[] temp = new int[2];
        temp[0] = 2;
        temp[1] = 3;
        myList.add(temp);*/
        List<Callable<UserNameBooleanTuple>> updateTasks = new ArrayList<Callable<UserNameBooleanTuple>>();
        for (ClientPlayerTuple<IClientCallback, Player> tuple : players) {
            HighlightWordTask task = new HighlightWordTask(tuple.client, word1Accepted, word2Accepted, greenList1, greenList2, userName);
            updateTasks.add(task);
        }
        broadcastHighlight(updateTasks);
    }

    private boolean broadcastHighlight(List<Callable<UserNameBooleanTuple>> updateTasks) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        List<Future<UserNameBooleanTuple>> results = forkJoinPool.invokeAll(updateTasks);

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
                retryTasks.add(updateTasks.stream()
                        .filter(task -> failedUser.equals(((HighlightWordTask) task).userName)).findAny().orElse(null));
            }
            return broadcastHighlight(retryTasks);
        }
        return true;
    }
    List<int[]> greenList1 = new ArrayList<int[]>();
    List<int[]> greenList2 = new ArrayList<int[]>();

    @Override
    public void updateGreenList(
            int word1X1coordinate,
            int word1X2coordinate,
            int word1Y1coordinate,
            int word1Y2coordinate,
            int word2X1coordinate,
            int word2X2coordinate,
            int word2Y1coordinate,
            int word2Y2coordinate)
            throws RemoteException {

        // if (this.word1Accepted) {
        //up to down
        int x1 = word1X1coordinate;
        int y1 = word1Y1coordinate;

        int deltaX = word1X2coordinate - word1X1coordinate;
        for (int i = 0; i <= deltaX; i++) {
            int[] temp1 = new int[2];
            temp1[0] = x1 + i; //y coordinate
            temp1[1] = y1; //x coordinate
            System.out.println("word1 " + temp1[0] + " " + temp1[1]);
            greenList1.add(temp1);
        }
        //}
        //if (this.word2Accepted) {
        int x2 = word2X1coordinate;
        int y2 = word2Y1coordinate;
        int deltaY = word2Y2coordinate - word2Y1coordinate;
        for (int i = 0; i <= deltaY; i++) {
            int[] temp2 = new int[2];
            temp2[0] = x2; //y coordinate
            temp2[1] = y2 + i; //x coordinate
            System.out.println("word2 " + temp2[0] + " " + temp2[1]);
            greenList2.add(temp2);
        }
        //}        
    }

}
