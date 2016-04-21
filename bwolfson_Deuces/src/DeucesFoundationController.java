import java.awt.event.MouseEvent;

import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.CardView;
import ks.common.view.ColumnView;
import ks.common.view.Container;
import ks.common.view.PileView;
import ks.common.view.Widget;


public class DeucesFoundationController extends java.awt.event.MouseAdapter {
	/** The Deuces Game. */
	protected Deuces theGame;

	/** The specific Foundation pileView being controlled. */
	protected PileView src;
	/**
	 * FoundationController constructor comment.
	 */
	public DeucesFoundationController(Deuces theGame, PileView foundation) {
		super();

		this.theGame = theGame;
		this.src = foundation;
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

		/** Recover the from BuildablePile OR waste Pile */
		Widget fromWidget = c.getDragSource();
		if (fromWidget == null) {
			System.err.println ("FoundationController::mouseReleased(): somehow no dragSource in container.");
			c.releaseDraggingObject();
			return;
		}

		// Determine the To Pile
		Pile foundation = (Pile) src.getModelElement();
		Move move;
		//Card moved from waste pile to the foundation
		if(fromWidget.getModelElement()instanceof Pile){
			Pile waste = (Pile)fromWidget.getModelElement();
			CardView cardView = (CardView) draggingWidget;
			Card theCard = (Card) cardView.getModelElement();
			move = new WasteToFoundation(waste, theCard, foundation);
		}
		else{//column moving from tableaus
			Column source = (Column)fromWidget.getModelElement();
			ColumnView cv = (ColumnView)draggingWidget;
			Column col = (Column)cv.getModelElement();
			move = new TableauToFoundation(source, col, foundation, col.count());
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
}
