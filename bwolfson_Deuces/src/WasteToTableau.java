import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Pile;

/**
 * Move card from top of waste pile to top of a Tableau Column
 * @author Barrett
 *
 */
public class WasteToTableau extends Move{
	Pile waste;
	Column target;
	Card cardBeingDragged;
	
	public WasteToTableau(Pile from, Card cardBeingDragged, Column to){
		this.waste = from;
		this.target = to;
		this.cardBeingDragged = cardBeingDragged;
	}

	@Override
	public boolean doMove(Solitaire game) {
		if(!valid(game)){return false;}
		target.add(cardBeingDragged);
		return true;
	}

	@Override
	public boolean undo(Solitaire game) {
		waste.add(target.get());
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		if(target.count() == 0){return true;}
		int targetRank = target.rank();
		int targetSuit = target.suit();
		int rank = cardBeingDragged.getRank();
		int suit = cardBeingDragged.getSuit();
		if(cardBeingDragged.getRank() == targetRank - 1 &&
				cardBeingDragged.getSuit() == targetSuit){
			return true;
		}
		else if(rank == 13 && targetRank == 1 && suit == targetSuit){
			return true;
		}
		
		return false;
	}
	
}