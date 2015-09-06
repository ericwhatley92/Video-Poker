import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class HandUIMain extends JPanel implements ActionListener {
	
	private Deck deck;
	private CardButton[] cardButtons;
	private Card[] cardHand;;
	private JButton draw;
	private final int MAXCARDS = 5;
	private int drawn;
	private int earned;
	private JPanel panel;
	private int[] values = new int[13];
	private bettingGrid grid;
	
	public HandUIMain() {
		setBackground(new Color(8,138,75));
		setLayout(new GridBagLayout());
		GridBagConstraints P = new GridBagConstraints();
		GridBagConstraints D = new GridBagConstraints();
		GridBagConstraints G = new GridBagConstraints();
		P.gridy=1;
		D.gridy=2;
		G.gridy=0;
		G.anchor = GridBagConstraints.NORTH;
		P.anchor = GridBagConstraints.CENTER;
		D.anchor = GridBagConstraints.SOUTH;
		panel = new JPanel();
		panel.setLayout(new GridLayout(1,5));
		
		grid = new bettingGrid(1000, 10);
		deck = new Deck();
		cardButtons = new CardButton[MAXCARDS];
		for(int i = 0; i < cardButtons.length; i++) {
			cardButtons[i] = new CardButton(deck.Draw());
			cardButtons[i].addActionListener(this);
			panel.add(cardButtons[i]);
			cardButtons[i].setIcon(new ImageIcon
								  ("Cards/" + cardButtons[i].getCard().getValue() +
								  cardButtons[i].getCard().getSuit() + ".png"));
		}
		draw = new JButton("Draw");
		draw.addActionListener(this);
		draw.setSize(50,100);
		add(grid, G);
		add(panel,P);
		add(draw,D);
		drawn = 0;
	}
	
	public void actionPerformed(ActionEvent e) {
		if(drawn <= 1 && e.getSource() == draw) {
			//draw five
			if(drawn == 1) { drawFive(); }
			
			//get cards selected for pickup and game winnings
			if(drawn == 0) { 
				//updates the cards selected for pickup
				for(int i = 0; i < cardButtons.length; i++) {
					if(cardButtons[i].getCard().getChecked()) {
						cardButtons[i].setCard(deck.Draw());
						cardButtons[i].setIcon(new ImageIcon
									  ("Cards/" + cardButtons[i].getCard().getValue() +
									  cardButtons[i].getCard().getSuit() + ".png"));
					}
				}
				getGameWinnings(); 
			}
			
			drawn++;
			
			//reset drawn and shuffle the deck
			if(drawn == 2) {
				drawn = 0;
				deck.Shuffle();
			}
		}
				
		//control structure to flip the cards
		if(e.getSource() instanceof CardButton && drawn < 1) {
			CardButton source = (CardButton)e.getSource();
			if(!source.getCard().getChecked()) {
				source.getCard().setChecked(true);
				source.setIcon(new ImageIcon("Cards/back.png"));
			}
			else if(source.getCard().getChecked()) {
				source.getCard().setChecked(false);
				source.setIcon(new ImageIcon
								("Cards/" + source.getCard().getValue() +
								source.getCard().getSuit() + ".png"));
			}
		}
	}


/***********************PRIVATE FUNCTIONS***********************/

	//function to calculate the game winnings
	private void getGameWinnings() {
		int newTotal;
		int multiplier = checkHand();
		int total = grid.getTotal();
		int bet = grid.getBetAmount();
		if(multiplier == 0) { 
			newTotal = total - bet;
		}
		else {
			newTotal = total + (bet * multiplier);
		}
		grid.setTotal(newTotal);
	}
	
	

	private void drawFive() {
		for(int i = 0; i < cardButtons.length; i++) {
		
			cardButtons[i].setCard(deck.Draw());
			
			if(cardButtons[i].getCard().getChecked()) { 
				cardButtons[i].getCard().setChecked(false);
			}
			cardButtons[i].setIcon(new ImageIcon
						  ("Cards/" + cardButtons[i].getCard().getValue() +
						  cardButtons[i].getCard().getSuit() + ".png"));
		}
	}
	
	
/*************************************************************FIGURE OUT THE HAND*******************************************************/


/****************************************************/
	
	private void getHand() {
		cardHand = new Card[MAXCARDS];
		for(int i = 0; i < cardHand.length; i++) {
			cardHand[i] = cardButtons[i].getCard();
		}
		sort();
	}

    private void sort() { // this sorts the Card[] in preparation to check for a straight
		for(int i = 0; i <= cardHand.length - 1; i++) {
			int min = i;
			for(int j = i + 1; j < cardHand.length; j++) {
				if(cardHand[j].getValue() < cardHand[min].getValue()) {
					min = j;
				}
				Card temp = cardHand[min];
				cardHand[min] = cardHand[i];
				cardHand[i] = temp;
			}
		}
		merge();
    }
	
    private void merge() { // takes the card[] and puts it into the int[] representing the values
        int number = 0;
		for(int i = 0; i < 13; i++) {values[i] = 0; }
        for(int i = 0; i < MAXCARDS; i++) {
            number = cardHand[i].getValue() - 2;
            values[number] = values[number] + 1;
        }
    }
	
/****************************************************/
	

/*********************************************************************************************/	
	
	//sort the array first before using this method
	private int checkHand() {
		getHand(); 
        if(checkStraight() && checkSuit() && cardHand[3].getValue() == 13) //checks for a royal flush
			return 250;  
        else if(checkStraight() && checkSuit()) // check for straight flush
			return 50;  
        else if(checkFour()) // check for four of a kind
			return 25;  
        else if(checkFullHouse()) // check for full house
			return 9; 
        else if(checkSuit()) // check for flush
			return 6; 
        else if(checkStraight()) // check for straight
			return 4; 
        else if(checkThree()) // check for three of a kind
			return 3;
        else if(checkTwoPair()) // check for two pair
			return 2; 
        else if(checkPair())// check for jacks or better pair
			return 1; 
        else
			return 0; 
    }

/*********************************************************************************************/	

	
/****************************************************/
	
    // this method is called in the checkHand method
    private boolean checkStraight() { //checks for a straight returning true or false       
        if(cardHand[4].getValue() == cardHand[3].getValue() + 1 &&
           cardHand[3].getValue() == cardHand[2].getValue() + 1 &&
           cardHand[2].getValue() == cardHand[1].getValue() + 1 &&
           cardHand[1].getValue() == cardHand[0].getValue() + 1) 
            return true;
        else if(cardHand[0].getValue() == 2 && cardHand[1].getValue() == 3 &&
                cardHand[2].getValue() == 4 && cardHand[3].getValue() == 5 &&
                cardHand[4].getValue() == 14)
            return true;
        else
            return false;
    }
    //this method is called in the checkHand method
    private boolean checkSuit() { // checks for a flush
        if(cardHand[4].getSuit() == cardHand[3].getSuit() &&
           cardHand[3].getSuit() == cardHand[2].getSuit() &&
           cardHand[2].getSuit() == cardHand[1].getSuit() &&
           cardHand[1].getSuit() == cardHand[0].getSuit())
            return true;
        else
            return false;
    }
    
    private boolean checkFour() { //values is an int[] representing the number of times a value is in the current hand
        boolean check = false;
        for(int j = 0; j < values.length; j++){
            if(values[j] == 4) {
                check = true;
            }
        }
        return check;       
    }
     
    private boolean checkFullHouse() { // checks for a full house
        boolean three = false;
        boolean two = false;
        boolean check = false;
        for(int i = 0; i < values.length; i++) {
            if(values[i] == 3){
                three = true;
            }
            else if(values[i] == 2) {
                two = true;
            }
        }
        if(three && two){
            check = true;
        }
        return check;
    }
    
    private boolean checkThree() { // checks for a three of a kind
        boolean check = false;
        for(int i = 0; i < values.length; i++){
            if(values[i] == 3){
                check = true;
            }
        }
        return check;
    }
    
    private boolean checkTwoPair() { // checks for two pair
        int count = 0;
        for(int i = 0; i < values.length; i++){
            if(values[i] == 2){
                count++;
            }
        }
        if(count == 2){
            return true;
        }
        else{
            return false;
        }       
    }
    
    private boolean checkPair() {
        int count = 0;
        int index = 0;
        for(int i = 0; i < values.length; i++){
            if(values[i] == 2){
                count++;
                index = i;
            }
        }
        if(index > 8){
            return true;
        }
        else{
            return false;
        }
    }
/****************************************************/

/***************************************************************/
	
	public static void main(String[] args) {
		JFrame f = new JFrame("Video Poker");
		HandUIMain poker = new HandUIMain();
		f.add(poker);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setVisible(true);
	}
}