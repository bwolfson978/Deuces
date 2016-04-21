import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.Pile;

/**
 * Move card from top of waste pile to top of a foundation pile
 * @author Barrett
 *
 */
public class WasteToFoundation extends Move{
	Pile waste;
	Pile target;
	Card cardBeingDragged;
	
	public WasteToFoundation(Pile from, Card cardBeingDragged, Pile to){
		this.waste = from;
		this.target = to;
		this.cardBeingDragged = cardBeingDragged;
	}

	@Override
	public boolean doMove(Solitaire game) {
		if(!valid(game)){return false;}
		target.add(cardBeingDragged);
		game.updateScore(+1);
		return true;
	}

	@Override
	public boolean undo(Solitaire game) {
		waste.add(target.get());
		game.updateScore(-1);
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		int targetRank = target.rank();
		int targetSuit = target.suit();
		int rank = cardBeingDragged.getRank();
		int suit = cardBeingDragged.getSuit();
		if(rank == targetRank + 1 && suit == targetSuit){
			return true;
		}
		else if(rank == 1 && targetRank == 13 && suit == targetSuit){
			return true;
		}
		
		return false;
	}
	
}
