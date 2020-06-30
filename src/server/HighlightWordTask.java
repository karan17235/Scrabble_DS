/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import remote.IClientCallback;

/**
 *
 * @author dadin
 */
class HighlightWordTask implements Callable<UserNameBooleanTuple>{

    IClientCallback client;
    List<int[]> greenList1, greenlist2;
    boolean word1Accepted;
    boolean word2Accepted;
    String userName;
    
    HighlightWordTask(IClientCallback client,
            boolean word1Accepted,
            boolean word2Accepted,
            List<int[]> greenList1,
            List<int[]> greenList2,
            String userName) {
        this.client = client;
        this.greenList1 = greenList1;
        this.greenlist2 = greenList2;
        this.word1Accepted = word1Accepted;
        this.word2Accepted = word2Accepted;
        this.userName = userName;
    }

    @Override
    public UserNameBooleanTuple call() {
        try {
            client.HighlightWords(word1Accepted, word2Accepted, greenList1, greenlist2);
            return new UserNameBooleanTuple(userName, true);
        } catch (RemoteException ex) {
            Logger.getLogger(HighlightWordTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new UserNameBooleanTuple(userName, true);
    }
    
}
