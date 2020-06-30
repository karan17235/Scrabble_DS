/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.rmi.RemoteException;
import java.util.concurrent.Callable;
import remote.IClientCallback;

/**
 *
 * @author mgoudarzi
 */
public class VotingTask implements Callable<UserNameVoteBooleanTuple> {

	public IClientCallback client;
	public String word1;
	public String word2;
	public String userName;
        public boolean word2Flag=false;

	VotingTask(IClientCallback client, String word1, String word2, String userName) {
		this.client = client;
                this.word1 = word1;
                this.word2 = word2;
		/*this.word1 = word1;
                if(!word2.equals("")){
                    this.word2 = word2;
                    word2Flag=true;
                
                }*/
		this.userName = userName;
	}

    public UserNameVoteBooleanTuple call() {
        try {
            if(!this.word1.equals("") && !this.word2.equals("")) {
                System.out.println("voting task condition 1");
                boolean response1 = client.Vote(word1);
                boolean response2 = client.Vote(word2);
                return new UserNameVoteBooleanTuple(userName, response1, response2, true);
            } else if(!this.word1.equals("") && this.word2.equals("")){
                System.out.println("voting task condition 2");
                boolean response1 = client.Vote(word1);
                return new UserNameVoteBooleanTuple(userName, response1, false, true);
            } else if(this.word1.equals("") && !this.word2.equals("")){
                System.out.println("voting task condition 3");
                boolean response2 = client.Vote(word2);
                return new UserNameVoteBooleanTuple(userName, false, response2, true);
            } else {
                System.out.println("voting task condition 4");
                return new UserNameVoteBooleanTuple(userName, false, false, true);
            }
            
            
            
            /*boolean response1 = client.Vote(word1);
            if (word2Flag == true) {
                boolean response2 = client.Vote(word2);
                return new UserNameVoteBooleanTuple(userName, response1, response2, true);
            }
            return new UserNameVoteBooleanTuple(userName, response1, true);*/
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new UserNameVoteBooleanTuple(userName, false, false, false);
    }

}