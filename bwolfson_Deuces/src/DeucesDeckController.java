import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.model.Move;
import ks.common.model.MultiDeck;
import ks.common.model.Pile;


/**
 * Controls all mouse actions with the deck.
 * Creation date: (11/11/01 10:57:40 PM)
 * @author: George T. Heineman (heineman@cs.wpi.edu)
 */
public class DeucesDeckController extends SolitaireReleasedAdapter {
	/** The game. */
	protected Deuces theGame;

	/** The WastePile of interest. */
	protected Pile waste;

	/** The Deck of interest. */
	protected MultiDeck stock;

	/**
	 * KlondikeDeckController constructor comment.
	 */
	public DeucesDeckController(Deuces theGame, MultiDeck d, Pile wastePile) {
		super(theGame);

		this.theGame = theGame;
		this.waste = wastePile;
		this.stock = d;
	}

	/**
	 * Coordinate reaction to the beginning of a Drag Event. In this case,
	 * no drag is ever achieved, and we simply deal upon the pres.
	 */
	public void mousePressed (java.awt.event.MouseEvent me) {

		// Attempting a DealFourCardMove
		Move m = new StockToWaste (stock, waste);
		if (m.doMove(theGame)) {
			theGame.pushMove (m);     // Successful DealFour Move
			theGame.refreshWidgets(); // refresh updated widgets.
		}
	}

}