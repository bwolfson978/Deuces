import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.model.Stack;

/**
 * Move card from top of waste pile to top of a foundation pile
 * @author Barrett
 *
 */
public class TableauToFoundation extends Move{
	Column from;
	Pile target;
	Column colBeingDragged;
	int sizeOfCol;
	
	public TableauToFoundation(Column from, Column colBeingDragged, Pile to, int sizeOfCol){
		this.from = from;
		this.target = to;
		this.colBeingDragged = colBeingDragged;
		this.sizeOfCol = sizeOfCol;
	}

	@Override
	public boolean doMove(Solitaire game) {
		if(!valid(game)){return false;}
		int sizeOfCol = colBeingDragged.count();
		for(int i = 0; i < sizeOfCol; i++){
			Card c = colBeingDragged.get();
			target.add(c);
		}
		game.updateScore(sizeOfCol);
		return true;
	}

	@Override
	public boolean undo(Solitaire game) {
		Stack aux = new Stack();
		//push moved column onto auxillary stack
		for(int i = 0; i < sizeOfCol; i++){
			Card c = target.get();
			aux.add(c);
		}
		//now push back onto original column in correct order
		for(int i = 0; i < sizeOfCol; i++){
			Card c = aux.get();
			from.add(c);
		}
		game.updateScore(-sizeOfCol);
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		int targetRank = target.rank();
		int targetSuit = target.suit();
		//bottom card of dragging column suit and rank
		System.out.println("count: " + colBeingDragged.count());
		int rank = colBeingDragged.peek().getRank();
		System.out.println("rank: " + rank);
		int suit = colBeingDragged.peek().getSuit();
		System.out.println("suit: " + suit);
		if(rank == targetRank + 1 && suit == targetSuit){
			return true;
		}
		else if(rank == 1 && targetRank == 13 && suit == targetSuit){
			return true;
		}
		
		return false;
		
	}
	
}