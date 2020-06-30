/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author mgoudarzi
 */
public class MatriceCell {
	private int positionRow;
	private int positionColumn;
	private char letter;
	private int buttonState;

	public final static int NOT_ASSIGNED = 0;
	public final static int ASSIGNED = 1;
	// private JButton b;

	public void setPositionRow(int x) {
		positionRow = x;
	}

	public void setPositionColumn(int y) {
		positionColumn = y;
	}

	public int getPositionRow() {
		return positionRow;
	}

	public int getPositionColumn() {
		return positionColumn;
	}

	public void setLetter(char x) {
		letter = x;
		// String s=""+x;
		// setText(s);
	}

	public char getLetter() {
		return letter;
	}

//    public void setButtonState (int x){
//        buttonState=x;
//        
//    }
//    public int getButtonState (){
//        return buttonState;

//    }    

}
