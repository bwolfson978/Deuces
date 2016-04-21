import java.awt.Dimension;

import ks.common.controller.SolitaireMouseMotionAdapter;
import ks.common.games.Solitaire;
import ks.common.games.SolitaireUndoAdapter;
import ks.common.model.Card;
import ks.common.model.Stack;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.model.MultiDeck;
import ks.common.model.Pile;
import ks.common.view.CardImages;
import ks.common.view.ColumnView;
import ks.common.view.DeckView;
import ks.common.view.IntegerView;
import ks.common.view.PileView;
import ks.launcher.Main;


public class Deuces extends Solitaire {
	
	//inherits score and numLeft from Solitaire base class
	MultiDeck stock;
	Pile waste;
	Pile[] foundations = new Pile[8];
	Column[] tableaus = new Column[10];
	DeckView stockView;
	PileView wasteView;
	PileView[] foundationViews = new PileView[8];
	ColumnView[] tableauViews = new ColumnView[10];
	IntegerView scoreView;
	IntegerView numLeftView;
	
	@Override
	public String getName() {
		return "bwolfson_Deuces";
	}

	@Override
	public boolean hasWon() {
		if(score.getValue() == 96){
			return true;
		}
		return false;
	}

	@Override
	public void initialize() {
		// initialize model
		initializeModel(getSeed());
		initializeView();
		initializeControllers();
		updateScore(0);
		updateNumberCardsLeft (86);
	}

	private void initializeControllers() {
		// Initialize Controllers for DeckView
		stockView.setMouseAdapter(new DeucesDeckController (this, stock, waste));
		stockView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
		stockView.setUndoAdapter (new SolitaireUndoAdapter(this));
		
		wasteView.setMouseAdapter(new DeucesWasteController(this,wasteView));
		wasteView.setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
		wasteView.setUndoAdapter(new SolitaireUndoAdapter(this));
		
		//initialize foundations
		for(int i = 0; i < 8; i++){
			foundationViews[i].setMouseAdapter(new DeucesFoundationController(this, foundationViews[i]));
			foundationViews[i].setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
			foundationViews[i].setUndoAdapter(new SolitaireUndoAdapter(this));

		}
		
		//initialize tableaus
		for(int i = 0; i < 10; i++){
			tableauViews[i].setMouseAdapter(new DeucesTableauController(this, tableauViews[i]));
			tableauViews[i].setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
			tableauViews[i].setUndoAdapter(new SolitaireUndoAdapter(this));

		}
		
	}

	private void initializeView() {
		CardImages ci = getCardImages();

		stockView = new DeckView (stock);
		stockView.setBounds (400,600, ci.getWidth(), ci.getHeight());
		container.addWidget (stockView);

		// foundations
		// create PileViews
		for (int pileNum = 1; pileNum < 9; pileNum++) {
			foundationViews[pileNum-1] = new PileView (foundations[pileNum-1]);
			foundationViews[pileNum-1].setBounds ((20*pileNum + (pileNum-1)*ci.getWidth())+90, 100, ci.getWidth(), ci.getHeight());
			container.addWidget (foundationViews[pileNum-1]);
		}

		// tableaus
		// create ColumnViews, one after the other.
		for (int pileNum = 1; pileNum < 11; pileNum++) {
			tableauViews[pileNum-1] = new ColumnView (tableaus[pileNum-1]);
			tableauViews[pileNum-1].setBounds (20*(pileNum) + (pileNum-1)*ci.getWidth(), 210, ci.getWidth(), ci.getHeight() + 200);
			container.addWidget (tableauViews[pileNum-1]);
		}

		wasteView = new PileView (waste);
		wasteView.setBounds (400 + ci.getWidth(), 600, ci.getWidth(), ci.getHeight());
		container.addWidget (wasteView);

		scoreView = new IntegerView (getScore());
		scoreView.setBounds (300, 10, 160, 60);
		container.addWidget (scoreView);

		numLeftView = new IntegerView (getNumLeft());
		numLeftView.setFontSize (14);
		numLeftView.setBounds (500, 20, 160, 60);
		container.addWidget (numLeftView);
		
	}

	private void initializeModel(int seed) {
		stock = new MultiDeck(2);
		stock.create(seed);
		waste = new Pile("waste");
		model.addElement(waste);
		
		//initialize foundations
		Stack aux = new Stack(); //will contain the drawn cards that aren't twos
		int foundationNum = 0;
		Card d1 = new Card(Card.TWO, Card.SPADES);
		Card d2 = new Card(Card.TWO, Card.HEARTS);
		Card d3 = new Card(Card.TWO, Card.DIAMONDS);
		Card d4 = new Card(Card.TWO, Card.CLUBS);
		
		//search stock for twos
		for(int j = 0; j < 104; j++){
			Card c = stock.get();
			if(c.equals(d1) || c.equals(d2) || c.equals(d3) || c.equals(d4)){
				foundations[foundationNum] = new Pile();
				foundations[foundationNum].add(c);
				model.addElement(foundations[foundationNum]);
				System.out.println(foundationNum);
				foundationNum++;
			}
			else{
				aux.add(c);
			}
		}
		
		stock.push(aux);
		model.addElement(stock);
		
		//initialize tableaus
		for(int i = 0; i < 10; i++){
			Stack s = new Stack();
			s.add(stock.get());
			tableaus[i] = new Column(s);
			model.addElement(tableaus[i]);
		}
	}
	
	/**
	 * create bigger window to compensate for growing tableau columns.
	 */
	public Dimension getPreferredSize() {
		// bigger starting dimensions for Deuces version...
		return new Dimension(1000, 1000);
	}
	
	/** Code to launch solitaire variation. */
	public static void main (String []args) {
		// Seed is to ensure we get the same initial cards every time.
		// Here the seed is to "order by suit."
		
		Main.generateWindow(new Deuces(), Deck.OrderBySuit);
	}
		
}


