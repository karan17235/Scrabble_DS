package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author mgoudarzi
 */
public class RegisterAndLoginFunctions {

    static final String JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    static final String DB_URL = "jdbc:derby:memory:test;create=true";
    static Connection connection;
    static Statement statement;
    ServerWindow server = new ServerWindow(true);
    
    public RegisterAndLoginFunctions (){
        try {
            createTable();
            //server.jTextArea1.append("The Table of Users is Created: The username is the key value"+"\n");
        } catch (SQLException ex) {
            server.jTextArea1.append("The Table of Users is not created and a problem occured"+"\n");
            Logger.getLogger(RegisterAndLoginFunctions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public RegisterAndLoginFunctions(boolean s){
        
    }

    public void createTable() throws SQLException {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection(DB_URL);
            connection.createStatement().execute("Create TABLE USERS(username varchar(100) PRIMARY KEY,password varchar(100))");

            System.out.println("connected");
        } catch (ClassNotFoundException e) {
            Logger.getLogger(RegisterAndLoginFunctions.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public String Register(String username, String password) throws SQLException, ClassNotFoundException {
        String message="";
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver"); // load class with static parameters to JVM
            connection = DriverManager.getConnection(DB_URL);
            statement = connection.createStatement();

            String userword = username.trim();
            String sql = "INSERT INTO USERS(username,password)" + "VALUES ('" + userword + "','" + password + "')";
            statement.executeUpdate(sql);
             message = "The user successfully added to the DataBase";
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            message = "The username currently exists in the Database";
            

        } finally {

            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return message;
    }

    public void remove(String wor1) throws SQLException {
        try {

            String a = wor1.trim();
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection(DB_URL);

            String sql1 = "DELETE FROM USERS WHERE word = ?";
            PreparedStatement stmt = connection.prepareStatement(sql1);
            stmt.setString(1, a);
            stmt.executeUpdate();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Boolean query(String wor1) throws SQLException, ClassNotFoundException {

        String a = wor1.trim();
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection(DB_URL);
            statement = connection.createStatement();
            String sql1 = "SELECT * FROM USERS WHERE username = ?";
            PreparedStatement stmt = connection.prepareStatement(sql1);
            stmt.setString(1, a);
            ResultSet rs = stmt.executeQuery();

            String password = " ";
            while (rs.next()) {

                password = rs.getString("password");

            }
            if(password.equals(" ")){
                return true;
            }else{
                return false;
            }
            
        } finally {

            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
    
    
    
    
    
        public String query1(String wor1) throws SQLException, ClassNotFoundException {

        String a = wor1.trim();
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection(DB_URL);
            statement = connection.createStatement();
            String sql1 = "SELECT * FROM USERS WHERE username = ?";
            PreparedStatement stmt = connection.prepareStatement(sql1);
            stmt.setString(1, a);
            ResultSet rs = stmt.executeQuery();

            String password = " ";
            while (rs.next()) {

                password = rs.getString("password");

            }
                return password;
            
        } finally {

            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
    
    
    
    
    

//    public void update(String wor1, String mean) throws SQLException, ClassNotFoundException {
//
//        try {
//
//            Class.forName("org.apache.derby.jdbc.EmbeddedDriver"); // load class with static parameters to JVM
//            connection = DriverManager.getConnection(DB_URL);
//            statement = connection.createStatement();
//            String word = wor1.trim();
//            String password = mean.trim();
//            String query = "UPDATE USERS set meaning =? WHERE word =?";
//            PreparedStatement stmt = connection.prepareStatement(query);
//            stmt.setString(1, password);
//            stmt.setString(2, word);
//            stmt.executeUpdate();
//
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//
//        } finally {
//            try {
//                if (statement != null) {
//                    statement.close();
//                }
//            } catch (SQLException e) {
//            }
//            try {
//                if (connection != null) {
//                    connection.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    public List<String> view() throws SQLException, ClassNotFoundException {
//
//        List<String> result = new ArrayList<>();
//        try {
//            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
//            connection = DriverManager.getConnection(DB_URL);
//            statement = connection.createStatement();
//            String sql1 = "SELECT * FROM USERS";
//            PreparedStatement stmt = connection.prepareStatement(sql1);
//            ResultSet rs = stmt.executeQuery();
//
//            String username = "";
//            String password = "";
//            while (rs.next()) {
//                String temp = "";
//                username = rs.getString("username");
//                password = rs.getString("password");
//                temp = "" + username + "    " + password;
//                result.add(temp);
//
//            }
// 
//            return result;
//        } finally {
//
//            try {
//                if (statement != null) {
//                    statement.close();
//                }
//            } catch (SQLException e) {
//            }
//            try {
//                if (connection != null) {
//                    connection.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }

}
