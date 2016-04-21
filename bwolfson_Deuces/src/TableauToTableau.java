import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Stack;

/**
 * Move card from top of waste pile to top of a Tableau Column
 * @author Barrett
 *
 */
public class TableauToTableau extends Move{
	Column from;
	Column target;
	Column colBeingDragged;
	int sizeOfCol;
	
	public TableauToTableau(Column from, Column colBeingDragged, Column to, int sizeOfCol){
		this.from = from;
		this.target = to;
		this.colBeingDragged = colBeingDragged;
		this.sizeOfCol = sizeOfCol;
	}

	@Override
	public boolean doMove(Solitaire game) {
		if(!valid(game)){return false;}
		target.push(colBeingDragged);
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
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		if(target.empty()){return true;}
		int targetRank = target.rank();
		int targetSuit = target.suit();
		//bottom card of dragging column suit and rank
		System.out.println("count: " + colBeingDragged.count());
		int rank = colBeingDragged.peek(0).getRank();
		System.out.println("rank: " + rank);
		int suit = colBeingDragged.peek(0).getSuit();
		System.out.println("suit: " + suit);
		if(rank == targetRank - 1 && suit == targetSuit){
			return true;
		}
		else if(rank == 13 && targetRank == 1 && suit == targetSuit){
			return true;
		}
		
		return false;
		
	}
	
}