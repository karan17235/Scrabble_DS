/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientscrabble;

/**
 *
 * @author dadin
 */
public class CheckListItem {
    private String label;
    private boolean isSelected = false;

    public CheckListItem(String label) {
      this.label = label;
    }

    public boolean isSelected() {
      return isSelected;
    }

    public void setSelected(boolean isSelected) {
      this.isSelected = isSelected;
    }

    @Override
    public String toString() {
      return label;
    }
}
