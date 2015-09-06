import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.io.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

public class bettingGrid extends JPanel implements ActionListener {
	private JPanel panel;
	private JButton options;
	private JTextField bet;
	private JLabel betLabel;
	private String [] handValue = {"HAND", "Royal Flush", "Straight Flush", "Four of a Kind", "Full House", "Flush", "Straight", "Three of a Kind", "Two Pair", "Jacks or Higher     "};
	private String[] multiply = {"MULTIPLIER", "250", "50", "25", "9", "6", "4", "3", "2", "1"};
	private JMenu menu;
	private JMenuBar bar;
	private JMenuItem newGame;
	private JMenuItem loadGame;
	private JMenuItem saveGame;
//	private HandUI poker;
	private int betAmount=10;
	private JLabel amount[] = new JLabel[9];
	private JLabel totalLabel;
	private int total;

	public bettingGrid(int total, int betAmount){
		//poker = new HandUI();
		startNewGame(total, betAmount/*, poker*/);
	}
	
	//has board settings
	public void startNewGame(int total, int betAmount/*, HandUI poker*/) {
		setBackground(new Color(8,138,75));
		setLayout (new GridBagLayout());
		GridBagConstraints P = new GridBagConstraints();
		GridBagConstraints X = new GridBagConstraints();
		GridBagConstraints B = new GridBagConstraints();
		GridBagConstraints T = new GridBagConstraints();
		GridBagConstraints Y = new GridBagConstraints();
		P.gridy=0;
		X.gridy=2;
		T.gridy=2;
		T.gridx=0;
		X.gridx=0;
		B.gridy=3;
		Y.gridy=1;
		P.anchor = GridBagConstraints.NORTH;
		T.anchor = GridBagConstraints.WEST;
		X.anchor = GridBagConstraints.CENTER;
		B.anchor = GridBagConstraints.SOUTH;
		Y.anchor = GridBagConstraints.CENTER;
		panel = new JPanel();
		panel.setLayout(new GridLayout(10,3));

		for (int i=0; i<10; i++){
			for(int j=0; j<3;j++){
				if (j==0){
					JLabel hand = new JLabel (handValue[i]);
					Border border = BorderFactory.createLineBorder(Color.BLACK);
					hand.setBorder(border);
					panel.add(hand);
				}
				else if(j==1){
					JLabel multiplier = new JLabel(multiply[i]);
					Border border = BorderFactory.createLineBorder(Color.BLACK);
					multiplier.setBorder(border);
					panel.add(multiplier);
				}
				else if (j==2 && i==0){
					JLabel bet = new JLabel( "PAYOUT");
					Border border = BorderFactory.createLineBorder(Color.BLACK);
					bet.setBorder(border);
					panel.add(bet);
				}
				else if(j==2 && i>0){
					amount[i-1]=new JLabel(String.valueOf(this.betAmount));
					Border border = BorderFactory.createLineBorder(Color.BLACK);
					amount[i-1].setBorder(border);
					panel.add(amount[i-1]);
				}
			}
		}
		//this.poker=poker;
		//poker.setBackground(new Color(8,138,75));
		bet = new JTextField("10", 3);
		bet.addActionListener(this);
		betLabel = new JLabel("Enter your bet (between 10 and 500) and press enter:");
		this.total = total;
		totalLabel = new JLabel("Total:  $ " + total);
		add(panel, P);
		add(betLabel, Y);
		add(totalLabel,T);
		add(bet, X);
		//add(poker, B);
		
		options = new JButton("Options");
		options.addActionListener(this);
		bar = new JMenuBar();
		//setJMenuBar(bar);
		menu = new JMenu("Options");
		bar.add(menu);
		newGame = new JMenuItem("New Game");
		loadGame = new JMenuItem("Load Game");
		saveGame = new JMenuItem("Save Game");
		newGame.addActionListener(this);
		loadGame.addActionListener(this);
		saveGame.addActionListener(this);
		menu.add(newGame);
		menu.add(loadGame);
		menu.add(saveGame);
		repaint();
		for (int g=0; g<amount.length; g++){
			amount[g].setText(String.valueOf(betAmount*Integer.parseInt(multiply[g+1])));
		}
	}
	/**********GETTERS&SETTERS**********/
	//public HandUI getHandUI() {return poker;}
	public void setTotal(int t) {
		total = t;
		totalLabel.setText("Total: " + String.valueOf(t));
	}
	public int getTotal() {return total;}
	public int getBetAmount() {return betAmount;}
	/***********************************/
    //save, load, and new functions
	public void saveToFile() {
		try {
			File file = new File("videoPoker.sav");
			file.delete();
			file.createNewFile();
			ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
			//outputStream.writeObject(poker);
			outputStream.writeObject(betAmount);
			outputStream.writeObject(total);
			outputStream.close();
		}
		catch (IOException i) {
			try {
				File file = new File("videoPoker.sav");
				file.createNewFile();
			}
			catch (IOException j) {
				JOptionPane.showMessageDialog(this,"Unable to create save file.");
			}
		}
	}
	
	public void loadFromFile() {
		try {
			FileInputStream file = new FileInputStream("videoPoker.sav");
			ObjectInputStream in = new ObjectInputStream(file);
			//poker = (HandUI) in.readObject();
			betAmount = (int) in.readObject();
			total = (int) in.readObject();
			startNewGame(total, betAmount/*, poker*/);
			in.close();
			file.close();
			revalidate();
			repaint();
		}
		catch (IOException i) {
			JOptionPane.showMessageDialog(this,"Save File not Found.");
		}
		catch (ClassNotFoundException v) {
			JOptionPane.showMessageDialog(this,"Save File Corrupted.");
		}
	}
	
	//starts a new game with the default settings, saves game to file if clicked, and loads saved game if clicked.
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JMenuItem){
			JMenuItem item = (JMenuItem) e.getSource();
			if(item.getText().equalsIgnoreCase("New Game")){
				JOptionPane.showMessageDialog(this,"I'm starting a new game now.");
			//	dispose();
				bettingGrid window = new bettingGrid(1000, 0);
			//	window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				window.setVisible(true);
			//	window.pack();
			}
			if(item.getText().equalsIgnoreCase("Save Game")) {
				saveToFile();

				
			}
			if(item.getText().equalsIgnoreCase("Load Game")) {
				loadFromFile();

			}
		}
		else {
			try { 
				betAmount = Integer.parseInt(bet.getText());
				if(betAmount > 500) { 
					betAmount = 500;
					bet.setText(String.valueOf(500));
				}
				else if(betAmount < 10) {
					betAmount = 10;
					bet.setText(String.valueOf(10));
				}
			} 
			catch(NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Please enter a number!");
			}
			for (int g=0; g<amount.length; g++){
				amount[g].setText(String.valueOf(betAmount*Integer.parseInt(multiply[g+1])));
			}
		}	
	}
	
	//End Save, Load, and New Functions
	public static void main(String[] args) {
		bettingGrid window = new bettingGrid(1000,0);
		//window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	//	window.pack();
	}
}
