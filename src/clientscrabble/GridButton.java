/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientscrabble;


import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

/**
 *
 * @author mgoudarzi
 */
//Button that has an associated Tile
public class GridButton extends javax.swing.JButton {

    private int positionRow;
    private int positionColumn;
    private char letter;
    private int buttonState;
    
    public final static int NOT_ASSIGNED = 0;
    public final static int ASSIGNED = 1;
   // private JButton b;

    public GridButton(int x , int y,char z) {
        positionColumn = x;
        positionRow = y;
        letter=z;
        buttonState=NOT_ASSIGNED;
        
    }
    public GridButton(){
        
    }


    
    public void setPositionRow (int x){
        positionRow =x;
    }
        public void setPositionColumn (int y){
        positionColumn =y;
    }
        
        
    public int getPositionRow (){
       return positionRow;  
    }
    
    
            
    public int getPositionColumn(){
       return  positionColumn;
    }
        
    
    public void setLetter (char x){
        letter=x;
        String s=""+x;
        setText(s);
    }
     public void setLetter (char x, boolean b){
        letter=x;
        String s=""+x;
        //setText(s);
    }
    public char getLetter (){
        String s=getText(); 
            try {
             letter = s.charAt(0);
             return letter;
            }catch (Exception e){
                return ' ';
            }
    }
    
    public void setButtonState (int x){
        buttonState=x;
        
    }
    public int getButtonState (){
        return buttonState;
        
    }    

}
