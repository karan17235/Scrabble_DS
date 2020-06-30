/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.sql.Timestamp;

import clientscrabble.*;

/**
 *
 * @author mgoudarzi
 */
public class Player {
    private String username;
    private String password;
    private int score=0;
    private boolean turn;
    private Timestamp timestamp;
    private boolean isTurn;
    private boolean active;
    

public void setIsTurn (boolean t){
	isTurn=t;
}
public boolean getIsTurn (){
 return isTurn;
}        

public void setTimeStamp (Timestamp t){
	timestamp=t;
}
public Timestamp getTimeStamp (){
 return timestamp;
}
    
public void setUsername (String s){
    username=s;
}
public String getUsername (){
 return username;
}


public void setPassword (String p){
    password=p;
}

public String getPassword(){
    return password;
}


public void setScore(int s){
    score=score+s;
}

public int getScore(){
    return score;
}

public boolean getTurn(){
    return turn;
}

public void setTurn(boolean s){
    turn=s;
}
public void setActive(boolean s){
        active=s;
}
public boolean getActive(){
    return active;
}
  
}

