import java.awt.event.MouseEvent;
import ks.client.gamefactory.GameWindow;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.tests.KSTestCase;

/**
 * Test cases for Deuces implementation of Solitaire.
 * @author Barrett
 *
 */

public class Test extends KSTestCase {
	
	Deuces game; /** the Deuces game object. **/ 
	GameWindow gw; /** the game window. **/
	
	@Override
	/**
	 * initializes the game for test
	 */
	protected void setUp(){
		game = new Deuces();
		gw = Main.generateWindow(game, Deck.OrderBySuit);
		
	}
	
	/**
	 * safely closes the game without leaving running instances.
	 */
	@Override
	protected void tearDown(){
		gw.dispose();
	}
	
	/**
	 * tests drawing a card from the stock into the waste.
	 */
	public void testStockToWaste(){
		Card topCard = game.stock.peek();
		StockToWaste move = new StockToWaste(game.stock, game.waste);
		assertTrue(move.valid(game));
		
		move.doMove(game);
		assertEquals(85, game.stock.count());
		assertEquals(topCard, game.waste.peek());
		int value = game.getNumLeft().getValue();
		assertEquals(85, value);
		
		move.undo(game);
		assertEquals(86, game.stock.count());
	}
	
	/**
	 * tests moving a one card column from a tableau column to a foundation pile.
	 */
	public void testTableauToFoundation(){
		Column col = new Column();
		col.add(game.tableaus[1].get());
		TableauToFoundation move = new TableauToFoundation(game.tableaus[1], col, game.foundations[3],col.count());
		assertTrue(move.valid(game));
		move.doMove(game);
		assertEquals(game.getScoreValue(),1);
	}
	
	/**
	 * tests moving a one card column from a tableau column to another tableau column.
	 */
	public void testTableauToTableau(){
		Column col = new Column();
		col.add(game.tableaus[2].get());
		TableauToTableau move = new TableauToTableau(game.tableaus[2], col, game.tableaus[3], col.count());
		assertTrue(move.valid(game));
		move.doMove(game);
		assertEquals(game.tableaus[2].count(), 0);
		assertEquals(game.tableaus[3].count(), 2);
	}
	
	/**
	 * tests moving a card from a tableau to another tableau,
	 * emptying a tableau space, drawing a card from the stock into the waste,
	 * then moving this card from the waste into the newly opened tableau space.
	 */
	public void testWasteToTableau(){
		//empty tab[8] (move to tab[9])
		Column col = new Column();
		col.add(game.tableaus[8].get());
		TableauToTableau move = new TableauToTableau(game.tableaus[8], col, game.tableaus[9], col.count());
		move.doMove(game);
		//draw card from stock to waste
		StockToWaste move2 = new StockToWaste(game.stock, game.waste);
		move2.doMove(game);
		//move drawn card in waste pile to tab[8]
		WasteToTableau wtt = new WasteToTableau(game.waste, game.waste.get(), game.tableaus[8]);
		assertTrue(wtt.valid(game));
		move.doMove(game);
	}
	
	/**
	 * tests moving a card from the waste pile to a non-empty tableau.
	 */
	public void testWasteToTableau2(){
		Card c = new Card(Card.FIVE, Card.CLUBS);
		game.waste.add(c);
		WasteToTableau wtt = new WasteToTableau(game.waste, game.waste.get(), game.tableaus[4]);
		assertTrue(wtt.valid(game));	
		wtt.doMove(game);
	}
	
	/**
	 * tests moving a card from the waste pile to one of the foundation piles.
	 */
	public void testWasteToFoundation(){
		Card c = new Card(Card.THREE, Card.SPADES);
		game.waste.add(c);
		WasteToFoundation wtf = new WasteToFoundation(game.waste, game.waste.get(), game.foundations[0]);
		assertTrue(wtf.valid(game));	
		wtf.doMove(game);
	}
	
	/**
	 * tests the deck controller by creating a press and verifying correct behavior.
	 */
	public void testDeckController() {

		// first create a mouse event
		MouseEvent pr = createPressed (game, game.stockView, 0, 0);
		game.stockView.getMouseManager().handleMouseEvent(pr);
		
		assertEquals (new Card(Card.QUEEN, Card.CLUBS), game.waste.peek());
		assertTrue (game.undoMove());
		assertTrue (game.waste.empty());
		
	}
	
	/**
	 * tests tableau controller by creating a mouse press on one tableau, a release on another
	 * whose top card can accept that activated by first press.
	 */
	public void testTableauController(){
		//create a mouse press event
		MouseEvent pr = createPressed(game, game.tableauViews[1], 0,0);
		game.tableauViews[1].getMouseManager().handleMouseEvent(pr);
		
		MouseEvent rel = createReleased(game, game.tableauViews[2], 0,0);
		game.tableauViews[2].getMouseManager().handleMouseEvent(rel);
		
		assertEquals(new Card(Card.THREE, Card.CLUBS), game.tableaus[2].peek());
		assertTrue(game.undoMove());
		assertEquals(game.tableaus[2].count(),1);
	}
	
	/**
	 * tests foundation controller by dragging a card from a tableau to a foundation
	 * pile which it fits upon.
	 */
	public void testFoundationController(){
		//create a mouse press event
		MouseEvent pr = createPressed(game, game.tableauViews[1], 0,0);
		game.tableauViews[1].getMouseManager().handleMouseEvent(pr);
		
		MouseEvent rel = createReleased(game, game.foundationViews[3], 0,0);
		game.foundationViews[3].getMouseManager().handleMouseEvent(rel);
		
		assertEquals(new Card(Card.THREE, Card.CLUBS), game.foundations[3].peek());
		assertTrue(game.undoMove());
		assertEquals(game.tableaus[3].count(),1);
	}
	
}
