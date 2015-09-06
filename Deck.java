import java.util.Stack;
import java.util.Random;
import java.io.*;
import javax.swing.*;
public class Deck {

	private int[] values = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}; //14 = ace
	private char[] suits = {'d', 'h', 's', 'c'};
	private Stack<Card> stack = null; 
	private Card[] cards = null;
	private final int MAXCARDS = 52;
	
	public Deck() {
		/*	Makes and places each card into an array	*/
		cards = new Card[MAXCARDS];
		int cardIndex = 0;
		for(int i = 0; i < values.length; i++) {
			for(int j = 0; j < suits.length; j++) {
				Card c = new Card(values[i], suits[j]);
				cards[cardIndex] = c;
				cardIndex++;	
			}
		}
		
		ShufflePush();
	}
	
	public void Shuffle() { ShufflePush(); }
	
	public Card Draw() { return stack.pop(); }
	

/*****************PRIVATE FUNCTIONS*****************/

	private void ShufflePush() {
		/*	Creates the stack	*/
		stack = new Stack<Card>();
		
		/*	Shuffles the cards	*/
		Random r = new Random();
		for(int i = 0; i < 1000; i++) {
			int x = r.nextInt(MAXCARDS);
			int y = r.nextInt(MAXCARDS);
			Card temp = cards[x];
			cards[x] = cards[y];
			cards[y] = temp;
		}
		
		/*	Pushes the cards into a stack	*/
		for(int i = 0; i < MAXCARDS; i++) {
			stack.push(cards[i]);
		}
	}
	
	public static void main(String[] args) {
		Deck d = new Deck();
	}
}