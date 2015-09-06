import javax.imageio.*;
import java.io.*;
import javax.swing.*;

public class Card {
	private int value;
	private char suit;
	private boolean checked;
	
	public Card() {
		value = 0;
		suit = 'n'; //none
		checked = false;
	}
	
	public Card(int v, char s) {
		value = v;
		suit = s;
		checked = false;
	}
	
	public void setValue(int v) { value = v; }
	public void setSuit(char s) { suit = s; }
	public void setChecked(boolean c) { checked = c; }
	public int getValue() { return value; }
	public char getSuit() { return suit; }
	public boolean getChecked() { return checked; }
	
}