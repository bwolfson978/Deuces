import java.awt.event.MouseEvent;

import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.CardView;
import ks.common.view.ColumnView;
import ks.common.view.Container;
import ks.common.view.Widget;


public class DeucesTableauController extends java.awt.event.MouseAdapter {
	/** The Deuces Game. */
	protected Deuces theGame;

	/** The specific Tableau pileView being controlled. */
	protected ColumnView src;
	/**
	 * FoundationController constructor comment.
	 */
	public DeucesTableauController(Deuces theGame, ColumnView src) {
		super();
		this.theGame = theGame;
		this.src = src;
	}
	/**
	 * Coordinate reaction to the completion of a Drag Event.
	 * <p>
	 * A bit of a challenge to construct the appropriate move, because cards
	 * can be dragged both from the WastePile (as a CardView object) and the 
	 * BuildablePileView (as a ColumnView).
	 * @param me java.awt.event.MouseEvent
	 */
	public void mouseReleased(MouseEvent me) {
		Container c = theGame.getContainer(); 
		
		/** Return if there is no card being dragged chosen. */
		Widget draggingWidget = c.getActiveDraggingObject();
		if (draggingWidget == Container.getNothingBeingDragged()) {
			System.err.println ("FoundationController::mouseReleased() unexpectedly found nothing being dragged.");
			c.releaseDraggingObject();		
			return;
		}

		/** Recover the from Column OR waste Pile */
		Widget fromWidget = c.getDragSource();
		if (fromWidget == null) {
			System.err.println ("FoundationController::mouseReleased(): somehow no dragSource in container.");
			c.releaseDraggingObject();
			return;
		}

		Column tableau = (Column)src.getModelElement();
		Move move;
		//if came from waste pile - just single card
		if(fromWidget.getModelElement() instanceof Pile){
			CardView cardView = (CardView) draggingWidget;
			Card theCard = (Card) cardView.getModelElement();
			Pile waste = (Pile)fromWidget.getModelElement();
			move = new WasteToTableau(waste, theCard, tableau);
		}
		else{ //came from another tableau - column
			ColumnView colView = (ColumnView)draggingWidget;
			Column col = (Column)colView.getModelElement();
			Column source = (Column)fromWidget.getModelElement();
			move = new TableauToTableau(source, col, tableau, col.count());
		}

		if(move.doMove(theGame)){
			theGame.pushMove(move);
		}
		else{
			fromWidget.returnWidget(draggingWidget);
		}
		
		c.releaseDraggingObject();
		c.repaint();
	}
	
	/**
	 * Coordinate reaction to the beginning of a Drag Event.
	 * <p>
	 * @param me java.awt.event.MouseEvent
	 */
	public void mousePressed(MouseEvent me) {
	 
		// The container manages several critical pieces of information; namely, it
		// is responsible for the draggingObject; in our case, this would be a CardView
		// Widget managing the card we are trying to drag between two piles.
		Container c = theGame.getContainer();
		
		/** Return if there is no column to be chosen. */
		Column col = (Column) src.getModelElement();
		
		if (col.count() == 0) {
			c.releaseDraggingObject();
			return;
		}
	
		// Get a card to move from PileView. Note: this returns a CardView.
		// Note that this method will alter the model for PileView if the condition is met.
		ColumnView colView = src.getColumnView(me);
		
		// an invalid selection of some sort.
		if (colView == null) {
			c.releaseDraggingObject();
			return;
		}
		
		// If we get here, then the user has indeed clicked on the top card in the PileView and
		// we are able to now move it on the screen at will. For smooth action, the bounds for the
		// cardView widget reflect the original card location on the screen.
		Widget w = c.getActiveDraggingObject();
		if (w != Container.getNothingBeingDragged()) {
			System.err.println ("WastePileController::mousePressed(): Unexpectedly encountered a Dragging Object during a Mouse press.");
			return;
		}
	
		// Tell container which object is being dragged, and where in that widget the user clicked.
		c.setActiveDraggingObject (colView, me);
		
		// Tell container which source widget initiated the drag
		c.setDragSource (src);
	
		// The only widget that could have changed is ourselves. If we called refresh, there
		// would be a flicker, because the dragged widget would not be redrawn. We simply
		// force the WastePile's image to be updated, but nothing is refreshed on the screen.
		// This is patently OK because the card has not yet been dragged away to reveal the
		// card beneath it.  A bit tricky and I like it!
		src.redraw();
	}
}