import javax.swing.*;

public class CardButton extends JButton {

	private Card card;
		
	public CardButton(Card c) {
		card = c;
	}
	
	public Card getCard() { return card; }
	public void setCard(Card c) { card = c; }
}