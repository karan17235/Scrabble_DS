/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author mgoudarzi
 */
public class SynchronizedFunctions {

    RegisterAndLoginFunctions regfunc = new RegisterAndLoginFunctions();

    public String Register(String username, String password) throws SQLException, ClassNotFoundException {
        String message="";
        synchronized (regfunc) {
            message=regfunc.Register(username, password);
            return message;
        }
    }

    public Boolean query(String word) throws SQLException, ClassNotFoundException {
        synchronized (regfunc) {
            Boolean result = regfunc.query(word);
            return result;
        }
    }
    
    
        public String query1(String word) throws SQLException, ClassNotFoundException {
        synchronized (regfunc) {
            String result = regfunc.query1(word);
            return result;
        }
    }
    

    public String remove(String word) throws SQLException, ClassNotFoundException {
        synchronized (regfunc) {
            regfunc.remove(word);
            return "User Successfully Removed!";
        }
    }
    

//    public List<String> view() throws SQLException, ClassNotFoundException {
//        synchronized (regfunc) {
//            List<String> result = new ArrayList<>();
//            result = regfunc.view();
//            return result;
//
//        }
//
//}
}
